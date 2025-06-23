package com.petraccia.elisabetta.CretaceousPark.controller;

import com.petraccia.elisabetta.CretaceousPark.dto.ZoneDTO;
import com.petraccia.elisabetta.CretaceousPark.service.ZoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zone")
public class ZoneController {

    private final ZoneService zoneService;

    @Autowired
    public ZoneController(ZoneService zoneService) {
        this.zoneService = zoneService;
    }

    @GetMapping("/list")
    public List<ZoneDTO> getAllZones(){
        return zoneService.getAllZones();
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<ZoneDTO> getZoneById(@PathVariable Long id) {
        ZoneDTO zoneDTO = zoneService.getZoneById(id);

        if (zoneDTO != null) {
            return ResponseEntity.ok(zoneDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<ZoneDTO> getZoneByName(@PathVariable String name) {
        ZoneDTO zoneDTO = zoneService.getZoneByName(name);

        if (zoneDTO != null) {
            return ResponseEntity.ok(zoneDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ZoneDTO saveZone(@RequestBody ZoneDTO zoneDTO) {
        return zoneService.saveZone(zoneDTO);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ZoneDTO> updateZone(@PathVariable Long id, @RequestBody ZoneDTO zoneDTO) {
        ZoneDTO updatedZone = zoneService.updateZone(id, zoneDTO);

        if (updatedZone != null) {
            return ResponseEntity.ok(updatedZone);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteZone(@PathVariable Long id) {
        zoneService.deleteZoneById(id);
        String message = "Zone with ID " + id + " deleted successfully.";

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
