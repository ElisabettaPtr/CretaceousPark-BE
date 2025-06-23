package com.petraccia.elisabetta.CretaceousPark.controller;

import com.petraccia.elisabetta.CretaceousPark.dto.AttractionDTO;
import com.petraccia.elisabetta.CretaceousPark.service.AttractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attraction")
public class AttractionController {

    private final AttractionService attractionService;

    @Autowired
    public AttractionController(AttractionService attractionService) {
        this.attractionService = attractionService;
    }

    @GetMapping("/list")
    public List<AttractionDTO> getAllAttractions(){
        return attractionService.getAllAttractions();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<AttractionDTO> getAttractionById(@PathVariable Long id) {
        AttractionDTO attractionDTO = attractionService.getAttractionById(id);

        if (attractionDTO != null) {
            return ResponseEntity.ok(attractionDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<AttractionDTO> getAttractionByName(@PathVariable String name) {
        String formattedName = name.toLowerCase();
        formattedName = formattedName.substring(0, 1).toUpperCase() + formattedName.substring(1);
        AttractionDTO attractionDTO = attractionService.getAttractionByName(formattedName);

        if (attractionDTO != null) {
            return ResponseEntity.ok(attractionDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/zone-attractions/{zoneId}")
    public ResponseEntity<List<AttractionDTO>> getAttractionsByZoneId(@PathVariable Long zoneId) {
        List<AttractionDTO> attractions = attractionService.getAttractionsByZoneId(zoneId);
        return ResponseEntity.ok(attractions);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public AttractionDTO saveAttraction(@RequestBody AttractionDTO attractionDTO) {
        return attractionService.saveAttraction(attractionDTO);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AttractionDTO> updateAttraction(@PathVariable Long id, @RequestBody AttractionDTO attractionDTO) {
        AttractionDTO updatedAttraction = attractionService.updateAttraction(id, attractionDTO);

        if (updatedAttraction != null) {
            return ResponseEntity.ok(updatedAttraction);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteAttraction(@PathVariable Long id) {
        attractionService.deleteAttractionById(id);
        String message = "Attraction with ID " + id + " deleted successfully.";

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
