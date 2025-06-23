package com.petraccia.elisabetta.CretaceousPark.controller;

import com.petraccia.elisabetta.CretaceousPark.dto.ShowDTO;
import com.petraccia.elisabetta.CretaceousPark.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/show")
public class ShowController {

    private final ShowService showService;

    @Autowired
    public ShowController(ShowService showService) {
        this.showService = showService;
    }

    @GetMapping("/list")
    public List<ShowDTO> getAllShows(){
        return showService.getAllShows();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ShowDTO> getShowById(@PathVariable Long id) {
        ShowDTO showDTO = showService.getShowById(id);

        if (showDTO != null) {
            return ResponseEntity.ok(showDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ShowDTO> getShowByName(@PathVariable String name) {
        ShowDTO showDTO = showService.getShowByName(name);

        if (showDTO != null) {
            return ResponseEntity.ok(showDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/zone-shows/{zoneId}")
    public ResponseEntity<List<ShowDTO>> getShowsByZoneId(@PathVariable Long zoneId) {
        List<ShowDTO> shows = showService.getShowsByZoneId(zoneId);
        return ResponseEntity.ok(shows);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ShowDTO saveShow(@RequestBody ShowDTO showDTO) {
        return showService.saveShow(showDTO);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShowDTO> updateShow(@PathVariable Long id, @RequestBody ShowDTO showDTO) {
        ShowDTO updatedShow = showService.updateShow(id, showDTO);

        if (updatedShow != null) {
            return ResponseEntity.ok(updatedShow);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteShow(@PathVariable Long id) {
        showService.deleteShowById(id);
        String message = "Show with ID " + id + " deleted successfully.";

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
