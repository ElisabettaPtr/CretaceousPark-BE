package com.petraccia.elisabetta.CretaceousPark.service;

import com.petraccia.elisabetta.CretaceousPark.dto.TypeServiceDTO;
import com.petraccia.elisabetta.CretaceousPark.exception.BadRequestException;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.mapper.TypeServiceMapper;
import com.petraccia.elisabetta.CretaceousPark.model.Bookable;
import com.petraccia.elisabetta.CretaceousPark.model.NonBookable;
import com.petraccia.elisabetta.CretaceousPark.model.TypeService;
import com.petraccia.elisabetta.CretaceousPark.repository.TypeServiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TypeServiceService {

    private final TypeServiceRepository typeServiceRepository;

    public TypeServiceService(TypeServiceRepository typeServiceRepository) {
        this.typeServiceRepository = typeServiceRepository;
    }

    public List<TypeServiceDTO> getAllTypeServices() {
        return typeServiceRepository.findAll()
                .stream()
                .map(TypeServiceMapper::toDTO)
                .toList();
    }

    public TypeServiceDTO getTypeServiceById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        TypeService typeService = typeServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TypeService not found with id: " + id));

        return TypeServiceMapper.toDTO(typeService);
    }

    public TypeServiceDTO saveTypeService(TypeServiceDTO typeServiceDTO) {
        if (typeServiceDTO.getType() == null) {
            throw new BadRequestException("Availability status (type) must not be null.");
        }

        TypeService typeServiceToSave = TypeServiceMapper.toEntity(typeServiceDTO);
        TypeService savedTypeService = typeServiceRepository.save(typeServiceToSave);
        return TypeServiceMapper.toDTO(savedTypeService);
    }

    @Transactional
    public void deleteTypeServiceById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        TypeService typeService = typeServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TypeService not found with id: " + id));

        // Stacca la relazione dai Bookable
        if (typeService.getBookableServices() != null) {
            for (Bookable bookable : typeService.getBookableServices()) {
                bookable.setTypeService(null);
            }
        }

        // Stacca la relazione dai NonBookable
        if (typeService.getNonBookableServices() != null) {
            for (NonBookable nonBookable : typeService.getNonBookableServices()) {
                nonBookable.setTypeService(null);
            }
        }

        // Elimina il TypeService
        typeServiceRepository.deleteById(id);
    }

    public TypeServiceDTO updateTypeService(Long id, TypeServiceDTO typeServiceDTO) {
        TypeService existingTypeService = typeServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TypeService not found with id: " + id));

        if (typeServiceDTO.getType() != null) {
            existingTypeService.setType(typeServiceDTO.getType());
        }

        TypeService updatedTypeService = typeServiceRepository.save(existingTypeService);
        return TypeServiceMapper.toDTO(updatedTypeService);
    }

}
