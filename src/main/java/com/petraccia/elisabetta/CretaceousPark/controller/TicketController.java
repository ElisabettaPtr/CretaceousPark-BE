package com.petraccia.elisabetta.CretaceousPark.controller;

import com.petraccia.elisabetta.CretaceousPark.dto.PlannerDTO;
import com.petraccia.elisabetta.CretaceousPark.dto.TicketDTO;
import com.petraccia.elisabetta.CretaceousPark.exception.BadRequestException;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.service.CustomerService;
import com.petraccia.elisabetta.CretaceousPark.service.PlannerService;
import com.petraccia.elisabetta.CretaceousPark.service.TicketService;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.service.AuthUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final AuthUserService authUserService;
    private final CustomerService customerService;
    private final PlannerService plannerService;

    public TicketController(TicketService ticketService, AuthUserService authUserService,
                            CustomerService customerService, PlannerService plannerService) {
        this.ticketService = ticketService;
        this.authUserService = authUserService;
        this.customerService = customerService;
        this.plannerService = plannerService;
    }

    /* ENDPOINTS FOR ADMIN */

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDTO) {
        TicketDTO createdTicket = ticketService.saveTicket(ticketDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, @RequestBody TicketDTO ticketDTO) {
        TicketDTO updatedTicket = ticketService.updateTicket(id, ticketDTO);
        return ResponseEntity.ok(updatedTicket);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicketById(id);
        return ResponseEntity.ok("Ticket deleted successfully.");
    }

    /* ENDPOINTS FOR ADMIN AND CUSTOMER */

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        List<TicketDTO> tickets = ticketService.getAllShows();
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        AuthUser authUser = authUserService.findByUsername(userDetails.getUsername());
        if (authUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        TicketDTO ticketDTO = ticketService.getTicketById(id);

        if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
            Long customerId = customerService.getCustomerByAuthUserId(authUser.getId()).getId();
            List<PlannerDTO> planners = plannerService.getPlannersByCustomerId(customerId);
            boolean ownsPlanner = planners.stream()
                    .anyMatch(planner -> planner.getId().equals(ticketDTO.getPlannerId()));

            if (!ownsPlanner) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        return ResponseEntity.ok(ticketDTO);
    }

    /* ENDPOINTS FOR CUSTOMER */

    @GetMapping("/my-tickets")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<TicketDTO>> getMyPlannerTickets(@AuthenticationPrincipal UserDetails userDetails) {
        AuthUser authUser = authUserService.findByUsername(userDetails.getUsername());
        if (authUser == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        Long customerId = customerService.getCustomerByAuthUserId(authUser.getId()).getId();
        List<PlannerDTO> planners = plannerService.getPlannersByCustomerId(customerId);

        List<TicketDTO> tickets = new ArrayList<>();
        for (PlannerDTO planner : planners) {
            tickets.addAll(ticketService.getTicketsByPlannerId(planner.getId()));
        }

        return ResponseEntity.ok(tickets);
    }

    @PostMapping("/buy/{ticketId}/{plannerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> buyTicket(@PathVariable Long ticketId,
                                               @PathVariable Long plannerId,
                                               @AuthenticationPrincipal UserDetails userDetails) {
        AuthUser authUser = authUserService.findByUsername(userDetails.getUsername());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {
            Long customerId = customerService.getCustomerByAuthUserId(authUser.getId()).getId();
            TicketDTO purchasedTicket = ticketService.buyTicket(ticketId, plannerId, customerId);
            return ResponseEntity.ok("Ticket sold successfully.");
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}
