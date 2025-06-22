package com.petraccia.elisabetta.CretaceousPark.service;

import com.petraccia.elisabetta.CretaceousPark.dto.NonBookableDTO;
import com.petraccia.elisabetta.CretaceousPark.exception.BadRequestException;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.mapper.NonBookableMapper;
import com.petraccia.elisabetta.CretaceousPark.model.NonBookable;
import com.petraccia.elisabetta.CretaceousPark.model.TypeService;
import com.petraccia.elisabetta.CretaceousPark.repository.NonBookableRepository;
import com.petraccia.elisabetta.CretaceousPark.repository.TypeServiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NonBookableService {

    private final NonBookableRepository nonBookableRepository;
    private final TypeServiceRepository typeServiceRepository;

    @Autowired
    public NonBookableService(NonBookableRepository nonBookableRepository, TypeServiceRepository typeServiceRepository){
        this.nonBookableRepository = nonBookableRepository;
        this.typeServiceRepository = typeServiceRepository;
    }

    public List<NonBookableDTO> getAllNonBookables() {
        return nonBookableRepository.findAll()
                .stream()
                .map(NonBookableMapper::toDTO)
                .toList();
    }

    public NonBookableDTO getNonBookableById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        NonBookable nonBookable = nonBookableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Non-Bookable service not found with id: " + id));

        return NonBookableMapper.toDTO(nonBookable);
    }

    public NonBookableDTO getNonBookableByName(String name) {
        if (name == null) {
            throw new BadRequestException("Non-Bookable name must not be null.");
        }

        NonBookable nonBookable = nonBookableRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Non-Bookable service not found with name: " + name));

        return NonBookableMapper.toDTO(nonBookable);
    }

    public NonBookableDTO saveNonBookable(NonBookableDTO nonBookableDTO) {
        if (nonBookableDTO.getTypeServiceId() == null) {
            throw new BadRequestException("TypeService ID must not be null.");
        }

        TypeService typeService = typeServiceRepository.findById(nonBookableDTO.getTypeServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("TypeService not found with id: " + nonBookableDTO.getTypeServiceId()));

        NonBookable nonBookableToSave = NonBookableMapper.toEntity(nonBookableDTO, typeService);
        NonBookable savedNonBookable = nonBookableRepository.save(nonBookableToSave);
        return NonBookableMapper.toDTO(savedNonBookable);
    }

    @Transactional
    public void deleteNonBookableById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        NonBookable nonBookable = nonBookableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Non-Bookable not found with id: " + id));

        nonBookableRepository.deleteById(id);
    }

    public NonBookableDTO updateNonBookable(Long id, NonBookableDTO nonBookableDTO) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }
        if (nonBookableDTO == null) {
            throw new BadRequestException("Non-Bookable data must not be null.");
        }

        NonBookable existingNonBookable = nonBookableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Non-Bookable not found with id: " + id));

        existingNonBookable.setName(nonBookableDTO.getName());

        NonBookable updatedNonBookable = nonBookableRepository.save(existingNonBookable);
        return NonBookableMapper.toDTO(updatedNonBookable);
    }

}
