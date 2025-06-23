package com.petraccia.elisabetta.CretaceousPark.service;

import com.petraccia.elisabetta.CretaceousPark.dto.ShowDTO;
import com.petraccia.elisabetta.CretaceousPark.exception.BadRequestException;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.mapper.ShowMapper;
import com.petraccia.elisabetta.CretaceousPark.model.Show;
import com.petraccia.elisabetta.CretaceousPark.model.Zone;
import com.petraccia.elisabetta.CretaceousPark.repository.ShowRepository;
import com.petraccia.elisabetta.CretaceousPark.repository.ZoneRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShowService {

    private final ShowRepository showRepository;
    private final ZoneRepository zoneRepository;

    public ShowService(ShowRepository showRepository, ZoneRepository zoneRepository) {
        this.showRepository = showRepository;
        this.zoneRepository = zoneRepository;
    }

    public List<ShowDTO> getAllShows() {
        return showRepository.findAll()
                .stream()
                .map(ShowMapper::toDTO)
                .toList();
    }

    public ShowDTO getShowById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + id));

        return ShowMapper.toDTO(show);
    }

    public ShowDTO getShowByName(String name) {
        if (name == null) {
            throw new BadRequestException("Show name must not be null.");
        }

        String normalizedInputName = name.toLowerCase().replaceAll("\\s+", "");

        Show show = showRepository.findAll().stream()
                .filter(s -> s.getName() != null)
                .filter(s -> s.getName().toLowerCase().replaceAll("\\s+", "").equals(normalizedInputName))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with name: " + name));

        return ShowMapper.toDTO(show);
    }


    public List<ShowDTO> getShowsByZoneId(Long zoneId) {
        if (zoneId == null) {
            throw new BadRequestException("Zone ID must not be null.");
        }

        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + zoneId));

        List<Show> shows = showRepository.findByZoneId(zoneId);

        if (shows == null || shows.isEmpty()) {
            throw new ResourceNotFoundException("No zhows found for Zone id: " + zoneId);
        }

        return shows.stream()
                .map(ShowMapper::toDTO)
                .toList();
    }

    public ShowDTO saveShow(ShowDTO showDTO) {
        if (showDTO.getZoneId() == null) {
            throw new BadRequestException("Zone ID must not be null.");
        }

        Zone zone = zoneRepository.findById(showDTO.getZoneId())
                .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + showDTO.getZoneId()));

        Show showToSave = ShowMapper.toEntity(showDTO, zone);
        Show savedShow = showRepository.save(showToSave);
        return ShowMapper.toDTO(savedShow);
    }

    @Transactional
    public void deleteShowById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Show show = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + id));

        showRepository.deleteById(id);
    }

    public ShowDTO updateShow(Long id, ShowDTO showDTO) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }
        if (showDTO == null) {
            throw new BadRequestException("Show data must not be null.");
        }

        Show existingShow = showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id: " + id));

        Zone zone = null;
        if (showDTO.getZoneId() != null) {
            zone = zoneRepository.findById(showDTO.getZoneId())
                    .orElseThrow(() -> new ResourceNotFoundException("Zone not found with id: " + showDTO.getZoneId()));
        }

        existingShow.setName(showDTO.getName());
        existingShow.setDescription(showDTO.getDescription());
        existingShow.setAttraction(showDTO.getAttraction());
        existingShow.setTime(showDTO.getTime());
        existingShow.setZone(zone);

        Show updatedShow = showRepository.save(existingShow);
        return ShowMapper.toDTO(updatedShow);
    }
}
