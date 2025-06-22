package com.petraccia.elisabetta.CretaceousPark.service;

import com.petraccia.elisabetta.CretaceousPark.dto.AttractionDTO;
import com.petraccia.elisabetta.CretaceousPark.exception.BadRequestException;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.mapper.AttractionMapper;
import com.petraccia.elisabetta.CretaceousPark.model.Attraction;
import com.petraccia.elisabetta.CretaceousPark.model.Zone;
import com.petraccia.elisabetta.CretaceousPark.repository.AttractionRepository;
import com.petraccia.elisabetta.CretaceousPark.repository.ZoneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttractionService {

    private final AttractionRepository attractionRepository;
    private final ZoneRepository zoneRepository;

    @Autowired
    public AttractionService(AttractionRepository attractionRepository, ZoneRepository zoneRepository) {
        this.attractionRepository = attractionRepository;
        this.zoneRepository = zoneRepository;
    }

    public List<AttractionDTO> getAllAttractions() {
        return attractionRepository.findAll()
                .stream()
                .map(AttractionMapper::toDTO)
                .toList();
    }

    public AttractionDTO getAttractionById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Attraction attraction = attractionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attraction not found with id: " + id));

        return AttractionMapper.toDTO(attraction);
    }

    public AttractionDTO getAttractionByName(String name) {
        if (name == null) {
            throw new BadRequestException("Attraction name must not be null.");
        }

        Attraction attraction = attractionRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Attraction not found with name: " + name));

        return AttractionMapper.toDTO(attraction);
    }

    public List<AttractionDTO> getAttractionsByZoneId(Long zoneId) {
        if (zoneId == null) {
            throw new BadRequestException("Zone ID must not be null.");
        }

        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + zoneId));

        List<Attraction> attractions = attractionRepository.findByZoneId(zoneId);

        if (attractions == null || attractions.isEmpty()) {
            throw new ResourceNotFoundException("No attractions found for Zone id: " + zoneId);
        }

        return attractions.stream()
                .map(AttractionMapper::toDTO)
                .toList();
    }

    public AttractionDTO saveAttraction(AttractionDTO attractionDTO) {
        if (attractionDTO.getZoneId() == null) {
            throw new BadRequestException("Zone ID must not be null.");
        }

        Zone zone = zoneRepository.findById(attractionDTO.getZoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + attractionDTO.getZoneId()));

        Attraction attractionToSave = AttractionMapper.toEntity(attractionDTO, zone);
        Attraction savedAttraction = attractionRepository.save(attractionToSave);
        return AttractionMapper.toDTO(savedAttraction);
    }

    @Transactional
    public void deleteAttractionById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Attraction attraction = attractionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attraction not found with id: " + id));

        attractionRepository.deleteById(id);
    }

    public AttractionDTO updateAttraction(Long id, AttractionDTO attractionDTO) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }
        if (attractionDTO == null) {
            throw new BadRequestException("Attraction data must not be null.");
        }

        Attraction existingAttraction = attractionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Attraction not found with id: " + id));

        Zone zone = null;
        if (attractionDTO.getZoneId() != null) {
            zone = zoneRepository.findById(attractionDTO.getZoneId())
                    .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + attractionDTO.getZoneId()));
        }

        existingAttraction.setName(attractionDTO.getName());
        existingAttraction.setDescription(attractionDTO.getDescription());
        existingAttraction.setDangerLevel(attractionDTO.getDangerLevel());
        existingAttraction.setZone(zone);

        Attraction updatedAttraction = attractionRepository.save(existingAttraction);
        return AttractionMapper.toDTO(updatedAttraction);
    }

}
