package com.petraccia.elisabetta.CretaceousPark.controller;

import com.petraccia.elisabetta.CretaceousPark.dto.TypeServiceDTO;
import com.petraccia.elisabetta.CretaceousPark.service.TypeServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/type-service")
public class TypeServiceController {

    private final TypeServiceService typeServiceService;

    @Autowired
    public TypeServiceController(TypeServiceService typeServiceService) {
        this.typeServiceService = typeServiceService;
    }

    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')")
    public List<TypeServiceDTO> getAllTypeServices(){
        return typeServiceService.getAllTypeServices();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TypeServiceDTO> getTypeServiceById(@PathVariable Long id) {
        TypeServiceDTO typeServiceDTO = typeServiceService.getTypeServiceById(id);

        if (typeServiceDTO != null) {
            return ResponseEntity.ok(typeServiceDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('ADMIN')")
    public TypeServiceDTO saveType(@RequestBody TypeServiceDTO typeServiceDTO) {
        return typeServiceService.saveTypeService(typeServiceDTO);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TypeServiceDTO> updateTypeService(@PathVariable Long id, @RequestBody TypeServiceDTO typeServiceDTO) {
        TypeServiceDTO updatedTypeService = typeServiceService.updateTypeService(id, typeServiceDTO);

        if (updatedTypeService != null) {
            return ResponseEntity.ok(updatedTypeService);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTypeService(@PathVariable Long id) {
        typeServiceService.deleteTypeServiceById(id);
        String message = "TypeService with ID " + id + " deleted successfully.";

        return ResponseEntity.status(HttpStatus.OK).body(message);
    }

}
