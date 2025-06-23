package com.petraccia.elisabetta.CretaceousPark.controller;

import com.petraccia.elisabetta.CretaceousPark.dto.BookableDTO;
import com.petraccia.elisabetta.CretaceousPark.service.BookableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookable-service")
public class BookableController {

    private final BookableService bookableService;

    @Autowired
    public BookableController(BookableService bookableService) {
        this.bookableService = bookableService;
    }

    @GetMapping("/list")
    public List<BookableDTO> getAllBookable(){
        return bookableService.getAllBookables();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<BookableDTO> getBookableById(@PathVariable Long id) {
        BookableDTO bookableDTO = bookableService.getBookableById(id);

        if (bookableDTO != null) {
            return ResponseEntity.ok(bookableDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<BookableDTO> getBookableByName(@PathVariable String name) {
        BookableDTO bookableDTO = bookableService.getBookableByName(name);

        if (bookableDTO != null) {
            return ResponseEntity.ok(bookableDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public BookableDTO saveBookable(@RequestBody BookableDTO bookableDTO) {
        return bookableService.saveBookable(bookableDTO);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BookableDTO> updateBookable(@PathVariable Long id, @RequestBody BookableDTO bookableDTO) {
        BookableDTO updatedBookable = bookableService.updateBookable(id, bookableDTO);

        if (updatedBookable != null) {
            return ResponseEntity.ok(updatedBookable);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteBookable(@PathVariable Long id) {
        bookableService.deleteBookableById(id);
        String message = "Bookable with ID " + id + " deleted successfully.";

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
