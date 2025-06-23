package com.petraccia.elisabetta.CretaceousPark.controller;

import com.petraccia.elisabetta.CretaceousPark.dto.NonBookableDTO;
import com.petraccia.elisabetta.CretaceousPark.service.NonBookableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/non-bookable-service")
public class NonBookableController {

    private final NonBookableService nonBookableService;

    @Autowired
    public NonBookableController(NonBookableService nonBookableService) {
        this.nonBookableService = nonBookableService;
    }

    @GetMapping("/list")
    public List<NonBookableDTO> getAllNonBookable(){
        return nonBookableService.getAllNonBookables();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<NonBookableDTO> getNonBookableById(@PathVariable Long id) {
        NonBookableDTO nonBookableDTO = nonBookableService.getNonBookableById(id);

        if (nonBookableDTO != null) {
            return ResponseEntity.ok(nonBookableDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<NonBookableDTO> getNonBookableByName(@PathVariable String name) {
        NonBookableDTO nonBookableDTO = nonBookableService.getNonBookableByName(name);

        if (nonBookableDTO != null) {
            return ResponseEntity.ok(nonBookableDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public NonBookableDTO saveNonBookable(@RequestBody NonBookableDTO nonBookableDTO) {
        return nonBookableService.saveNonBookable(nonBookableDTO);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<NonBookableDTO> updateNonBookable(@PathVariable Long id, @RequestBody NonBookableDTO nonBookableDTO) {
        NonBookableDTO updatedNonBookable = nonBookableService.updateNonBookable(id, nonBookableDTO);

        if (updatedNonBookable != null) {
            return ResponseEntity.ok(updatedNonBookable);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteNonBookable(@PathVariable Long id) {
        nonBookableService.deleteNonBookableById(id);
        String message = "Non-Bookable with ID " + id + " deleted successfully.";

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
