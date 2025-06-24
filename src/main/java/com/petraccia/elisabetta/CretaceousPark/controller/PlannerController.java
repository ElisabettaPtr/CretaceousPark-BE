package com.petraccia.elisabetta.CretaceousPark.controller;

import com.petraccia.elisabetta.CretaceousPark.dto.PlannerDTO;
import com.petraccia.elisabetta.CretaceousPark.service.CustomerService;
import com.petraccia.elisabetta.CretaceousPark.service.PlannerService;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.model.AuthUser;
import com.petraccia.elisabetta.CretaceousPark.spring_jwt.service.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planner")
public class PlannerController {

    private final PlannerService plannerService;
    private final AuthUserService authUserService;
    private final CustomerService customerService;

    @Autowired
    public PlannerController(PlannerService plannerService,
                             AuthUserService authUserService,
                             CustomerService customerService) {
        this.plannerService = plannerService;
        this.authUserService = authUserService;
        this.customerService = customerService;
    }

    /* ENDPOINT PER ADMIN */

    // ADMIN solo: lista di tutti i planner
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<PlannerDTO> getAllPlanners() {
        return plannerService.getAllPlanners();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlannerDTO> getPlannerById(@PathVariable Long id) {
        PlannerDTO plannerDTO = plannerService.getPlannerById(id);
        if (plannerDTO != null) {
            return ResponseEntity.ok(plannerDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public PlannerDTO savePlanner(@RequestBody PlannerDTO plannerDTO) {
        return plannerService.savePlanner(plannerDTO);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlannerDTO> updatePlanner(@PathVariable Long id, @RequestBody PlannerDTO plannerDTO) {
        PlannerDTO updatedPlanner = plannerService.updatePlanner(id, plannerDTO);
        if (updatedPlanner != null) {
            return ResponseEntity.ok(updatedPlanner);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deletePlanner(@PathVariable Long id) {
        plannerService.deletePlanner(id);
        return ResponseEntity.ok("Planner with ID " + id + " deleted successfully.");
    }

    /* ENDPOINT PER CUSTOMER */

    @GetMapping("/my-planners")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<PlannerDTO>> getAllPlannersForCustomer(@AuthenticationPrincipal UserDetails userDetails) {
        AuthUser authUser = authUserService.findByUsername(userDetails.getUsername());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long customerId = customerService.getCustomerByAuthUserId(authUser.getId()).getId();
        List<PlannerDTO> planners = plannerService.getPlannersByCustomerId(customerId);
        return ResponseEntity.ok(planners);
    }

    @PostMapping("/add-my-planner")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PlannerDTO> savePlannerForCustomer(
            @RequestBody PlannerDTO plannerDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        AuthUser authUser = authUserService.findByUsername(userDetails.getUsername());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Long customerId = customerService.getCustomerByAuthUserId(authUser.getId()).getId();
        plannerDTO.setCustomerId(customerId);

        PlannerDTO savedPlanner = plannerService.savePlanner(plannerDTO);
        return ResponseEntity.ok(savedPlanner);
    }

    @PutMapping("/update-my-planner/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<PlannerDTO> updatePlannerForCustomer(
            @PathVariable Long id,
            @RequestBody PlannerDTO plannerDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        AuthUser authUser = authUserService.findByUsername(userDetails.getUsername());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean ownsPlanner = plannerService.isPlannerOwnedByAuthUser(id, authUser.getId());
        if (!ownsPlanner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Long customerId = customerService.getCustomerByAuthUserId(authUser.getId()).getId();
        plannerDTO.setCustomerId(customerId);

        PlannerDTO updatedPlanner = plannerService.updatePlanner(id, plannerDTO);
        if (updatedPlanner == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(updatedPlanner);
    }

    @DeleteMapping("/delete-my-planner/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> deletePlannerForCustomer(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        AuthUser authUser = authUserService.findByUsername(userDetails.getUsername());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean ownsPlanner = plannerService.isPlannerOwnedByAuthUser(id, authUser.getId());
        if (!ownsPlanner) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to delete this planner.");
        }

        plannerService.deletePlanner(id);
        return ResponseEntity.ok("Planner with ID " + id + " deleted successfully.");
    }
}

