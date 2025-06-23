package com.petraccia.elisabetta.CretaceousPark.controller;

import com.petraccia.elisabetta.CretaceousPark.dto.DayTypeDTO;
import com.petraccia.elisabetta.CretaceousPark.service.DayTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/day-type")
public class DayTypeController {

    private final DayTypeService dayTypeService;

    @Autowired
    public DayTypeController(DayTypeService dayTypeService) {
        this.dayTypeService = dayTypeService;
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<DayTypeDTO> getAllDayTypes(){
        return dayTypeService.getAllDayTypes();
    }

    @GetMapping("/id/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DayTypeDTO> getDayTypeById(@PathVariable Long id) {
        DayTypeDTO dayTypeDTO = dayTypeService.getDayTypeById(id);

        if (dayTypeDTO != null) {
            return ResponseEntity.ok(dayTypeDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DayTypeDTO> getDayTypeByName(@PathVariable String name) {
        DayTypeDTO dayTypeDTO = dayTypeService.getDayTypeByName(name.toLowerCase());

        if (dayTypeDTO != null) {
            return ResponseEntity.ok(dayTypeDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public DayTypeDTO saveDayType(@RequestBody DayTypeDTO dayTypeDTO) {
        return dayTypeService.saveDayType(dayTypeDTO);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DayTypeDTO> updateDayType(@PathVariable Long id, @RequestBody DayTypeDTO dayTypeDTO) {
        DayTypeDTO updatedDayType = dayTypeService.updateDayType(id, dayTypeDTO);

        if (updatedDayType != null) {
            return ResponseEntity.ok(updatedDayType);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteDayType(@PathVariable Long id) {
        dayTypeService.deleteDayTypeById(id);
        String message = "DayType with ID " + id + " deleted successfully.";

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
