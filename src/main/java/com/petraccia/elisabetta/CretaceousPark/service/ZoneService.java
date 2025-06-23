package com.petraccia.elisabetta.CretaceousPark.service;

import com.petraccia.elisabetta.CretaceousPark.dto.ZoneDTO;
import com.petraccia.elisabetta.CretaceousPark.exception.BadRequestException;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.mapper.ZoneMapper;
import com.petraccia.elisabetta.CretaceousPark.model.Attraction;
import com.petraccia.elisabetta.CretaceousPark.model.Show;
import com.petraccia.elisabetta.CretaceousPark.model.Zone;
import com.petraccia.elisabetta.CretaceousPark.repository.ZoneRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ZoneService {

    private final ZoneRepository zoneRepository;

    public ZoneService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    public List<ZoneDTO> getAllZones() {
        return zoneRepository.findAll()
                .stream()
                .map(ZoneMapper::toDTO)
                .toList();
    }

    public ZoneDTO getZoneById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + id));

        return ZoneMapper.toDTO(zone);
    }

    public ZoneDTO getZoneByName(String name) {
        if (name == null) {
            throw new BadRequestException("Name must not be null.");
        }

        String normalizedInputName = name.toLowerCase().replaceAll("\\s+", "");

        Zone zone = zoneRepository.findAll().stream()
                .filter(z -> z.getName() != null)
                .filter(z -> z.getName().toLowerCase().replaceAll("\\s+", "").equals(normalizedInputName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with name: " + name));

        return ZoneMapper.toDTO(zone);
    }

    public ZoneDTO saveZone(ZoneDTO zoneDTO) {
        if (zoneDTO.getName() == null) {
            throw new BadRequestException("Zone name must not be null.");
        }

        if (zoneDTO.getDescription() == null) {
            throw new BadRequestException("Zone description must not be null.");
        }

        Zone zoneToSave = ZoneMapper.toEntity(zoneDTO, null, null);
        Zone savedZone = zoneRepository.save(zoneToSave);
        return ZoneMapper.toDTO(savedZone);
    }

    @Transactional
    public void deleteZoneById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + id));

        // Stacca la relazione dalle attrazioni
        if (zone.getAttractions() != null) {
            for (Attraction attraction : zone.getAttractions()) {
                attraction.setZone(null);
            }
        }

        // Stacca la relazione dagli show
        if (zone.getShows() != null) {
            for (Show show : zone.getShows()) {
                show.setZone(null);
            }
        }

        zoneRepository.deleteById(id);
    }

    public ZoneDTO updateZone(Long id, ZoneDTO zoneDTO) {
        if (id == null) {
            throw new BadRequestException("Zone ID must not be null.");
        }

        if (zoneDTO == null) {
            throw new BadRequestException("Zone data must not be null.");
        }

        Zone existingZone = zoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + id));

        if (zoneDTO.getName() != null) {
            existingZone.setName(zoneDTO.getName());
        }

        if (zoneDTO.getDescription() != null) {
            existingZone.setDescription(zoneDTO.getDescription());
        }

        Zone updatedZone = zoneRepository.save(existingZone);
        return ZoneMapper.toDTO(updatedZone);
    }

}
