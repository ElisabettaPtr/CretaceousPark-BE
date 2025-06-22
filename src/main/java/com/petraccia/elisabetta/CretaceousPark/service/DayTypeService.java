package com.petraccia.elisabetta.CretaceousPark.service;

import com.petraccia.elisabetta.CretaceousPark.dto.DayTypeDTO;
import com.petraccia.elisabetta.CretaceousPark.exception.BadRequestException;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.mapper.DayTypeMapper;
import com.petraccia.elisabetta.CretaceousPark.model.DayType;
import com.petraccia.elisabetta.CretaceousPark.repository.DayTypeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DayTypeService {

    private final DayTypeRepository dayTypeRepository;

    @Autowired
    public DayTypeService(DayTypeRepository dayTypeRepository) {
        this.dayTypeRepository = dayTypeRepository;
    }

    public List<DayTypeDTO> getAllDayTypes() {
        return dayTypeRepository.findAll()
                .stream()
                .map(DayTypeMapper::toDTO)
                .toList();
    }

    public DayTypeDTO getDayTypeById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        DayType dayType = dayTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DayType  not found with id: " + id));

        return DayTypeMapper.toDTO(dayType);
    }

    public DayTypeDTO getDayTypeByName(String name) {
        if (name == null) {
            throw new BadRequestException("DayType name must not be null.");
        }

        DayType dayType = dayTypeRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("DayType not found with name: " + name));

        return DayTypeMapper.toDTO(dayType);
    }

    public DayTypeDTO saveDayType(DayTypeDTO dayTypeDTO) {
        if (dayTypeDTO.getName() == null) {
            throw new BadRequestException("DayType name must not be null.");
        }

        DayType dayTypeToSave = DayTypeMapper.toEntity(dayTypeDTO);
        DayType savedDayType = dayTypeRepository.save(dayTypeToSave);
        return DayTypeMapper.toDTO(savedDayType);
    }

    @Transactional
    public void deleteDayTypeById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        DayType dayType = dayTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DayType not found with id: " + id));

        dayTypeRepository.deleteById(id);
    }

    public DayTypeDTO updateDayType(Long id, DayTypeDTO dayTypeDTO) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }
        if (dayTypeDTO == null) {
            throw new BadRequestException("DayType data must not be null.");
        }

        DayType existingDayType = dayTypeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("DayType not found with id: " + id));

        existingDayType.setName(dayTypeDTO.getName());
        existingDayType.setOpenTime(dayTypeDTO.getOpenTime());
        existingDayType.setCloseTime(dayTypeDTO.getCloseTime());
        existingDayType.setOpen(dayTypeDTO.isOpen());

        DayType updatedDayType = dayTypeRepository.save(existingDayType);
        return DayTypeMapper.toDTO(updatedDayType);
    }

}
