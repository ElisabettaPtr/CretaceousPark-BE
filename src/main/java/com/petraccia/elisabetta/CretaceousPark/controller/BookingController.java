package com.petraccia.elisabetta.CretaceousPark.controller;

import com.petraccia.elisabetta.CretaceousPark.dto.BookingDTO;
import com.petraccia.elisabetta.CretaceousPark.service.BookingService;
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
@RequestMapping("/api/booking")
public class BookingController {

    private final BookingService bookingService;
    private final AuthUserService authUserService;

    @Autowired
    public BookingController(BookingService bookingService, AuthUserService authUserService) {
        this.bookingService = bookingService;
        this.authUserService = authUserService;
    }

    /* ENDPOINT PER ADMIN */

    // ADMIN solo: lista di tutti i booking
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<BookingDTO> getAllBooking() {
        return bookingService.getAllBookings();
    }

    // ADMIN solo: booking per ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookingDTO> getBookingById(@PathVariable Long id) {
        BookingDTO bookingDTO = bookingService.getBookingById(id);
        if (bookingDTO != null) {
            return ResponseEntity.ok(bookingDTO);
        }
        return ResponseEntity.notFound().build();
    }

    // ADMIN solo: booking per planner ID
    @GetMapping("/planner-bookings/{plannerId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<BookingDTO>> getBookingsByPlannerId(@PathVariable Long plannerId) {
        List<BookingDTO> bookings = bookingService.getBookingsByPlannerId(plannerId);
        return ResponseEntity.ok(bookings);
    }

    // ADMIN solo: salva booking generico
    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public BookingDTO saveBooking(@RequestBody BookingDTO bookingDTO) {
        return bookingService.saveBooking(bookingDTO);
    }

    // ADMIN solo: aggiorna booking
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookingDTO> updateBooking(@PathVariable Long id, @RequestBody BookingDTO bookingDTO) {
        BookingDTO updatedBooking = bookingService.updateBooking(id, bookingDTO);
        if (updatedBooking != null) {
            return ResponseEntity.ok(updatedBooking);
        }
        return ResponseEntity.notFound().build();
    }

    // ADMIN solo: cancella booking
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id) {
        bookingService.deleteBookingById(id);
        String message = "Booking with ID " + id + " deleted successfully.";
        return ResponseEntity.ok(message);
    }

    /* ENDPOINT PER CUSTOMER */

    // CUSTOMER può vedere i propri booking: recupera AuthUser tramite userDetails
    @GetMapping("/my-bookings")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<BookingDTO>> getMyBookings(@AuthenticationPrincipal UserDetails userDetails) {
        AuthUser authUser = authUserService.findByUsername(userDetails.getUsername());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        List<BookingDTO> bookings = bookingService.getBookingsByCustomerAuthUserId(authUser.getId());
        return ResponseEntity.ok(bookings);
    }

    // CUSTOMER può salvare booking per sé, collegandolo al proprio planner
    @PostMapping("/add-customer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingDTO> saveBookingForCustomer(
            @RequestBody BookingDTO bookingDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        AuthUser authUser = authUserService.findByUsername(userDetails.getUsername());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // Supponendo che BookingService possa ottenere il plannerId associato all’utente
        Long plannerId = bookingService.getPlannerIdByAuthUserId(authUser.getId());
        if (plannerId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }

        bookingDTO.setPlannerId(plannerId);

        BookingDTO savedBooking = bookingService.saveBooking(bookingDTO);
        return ResponseEntity.ok(savedBooking);
    }

    // CUSTOMER può aggiornare solo i propri booking
    @PutMapping("/update-customer/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BookingDTO> updateBookingForCustomer(
            @PathVariable Long id,
            @RequestBody BookingDTO bookingDTO,
            @AuthenticationPrincipal UserDetails userDetails) {

        AuthUser authUser = authUserService.findByUsername(userDetails.getUsername());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean ownsBooking = bookingService.isBookingOwnedByAuthUser(id, authUser.getId());
        if (!ownsBooking) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        BookingDTO updatedBooking = bookingService.updateBooking(id, bookingDTO);
        if (updatedBooking == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedBooking);
    }

    // CUSTOMER può cancellare solo i propri booking
    @DeleteMapping("/delete-customer/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<String> deleteBookingForCustomer(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        AuthUser authUser = authUserService.findByUsername(userDetails.getUsername());
        if (authUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        boolean ownsBooking = bookingService.isBookingOwnedByAuthUser(id, authUser.getId());
        if (!ownsBooking) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Not authorized to delete this booking.");
        }

        bookingService.deleteBookingById(id);
        return ResponseEntity.ok("Booking with ID " + id + " deleted successfully.");
    }
}

