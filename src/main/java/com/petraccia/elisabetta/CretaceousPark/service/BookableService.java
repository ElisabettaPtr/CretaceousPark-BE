package com.petraccia.elisabetta.CretaceousPark.service;

import com.petraccia.elisabetta.CretaceousPark.dto.BookableDTO;
import com.petraccia.elisabetta.CretaceousPark.exception.BadRequestException;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.mapper.BookableMapper;
import com.petraccia.elisabetta.CretaceousPark.model.Bookable;
import com.petraccia.elisabetta.CretaceousPark.model.TypeService;
import com.petraccia.elisabetta.CretaceousPark.repository.BookableRepository;
import com.petraccia.elisabetta.CretaceousPark.repository.TypeServiceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookableService {

    private final BookableRepository bookableRepository;
    private final TypeServiceRepository typeServiceRepository;

    @Autowired
    public BookableService(BookableRepository bookableRepository, TypeServiceRepository typeServiceRepository){
        this.bookableRepository = bookableRepository;
        this.typeServiceRepository = typeServiceRepository;
    }

    public List<BookableDTO> getAllBookables() {
        return bookableRepository.findAll()
                .stream()
                .map(BookableMapper::toDTO)
                .toList();
    }

    public BookableDTO getBookableById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Bookable bookable = bookableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bookable service not found with id: " + id));

        return BookableMapper.toDTO(bookable);
    }

    public BookableDTO getBookableByName(String name) {
        if (name == null) {
            throw new BadRequestException("Bookable name must not be null.");
        }

        Bookable bookable = bookableRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Bookable service not found with name: " + name));

        return BookableMapper.toDTO(bookable);
    }

    public BookableDTO saveBookable(BookableDTO bookableDTO) {
        if (bookableDTO.getTypeServiceId() == null) {
            throw new BadRequestException("TypeService ID must not be null.");
        }

        TypeService typeService = typeServiceRepository.findById(bookableDTO.getTypeServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("TypeService not found with id: " + bookableDTO.getTypeServiceId()));

        Bookable bookableToSave = BookableMapper.toEntity(bookableDTO, typeService);
        Bookable savedBookable = bookableRepository.save(bookableToSave);
        return BookableMapper.toDTO(savedBookable);
    }

    @Transactional
    public void deleteBookableById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Bookable bookable = bookableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bookable not found with id: " + id));

        bookableRepository.deleteById(id);
    }

    public BookableDTO updateBookable(Long id, BookableDTO bookableDTO) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }
        if (bookableDTO == null) {
            throw new BadRequestException("Bookable data must not be null.");
        }

        Bookable existingBookable = bookableRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bookable not found with id: " + id));

        existingBookable.setName(bookableDTO.getName());
        existingBookable.setPrice(bookableDTO.getPrice());

        Bookable updatedBookable = bookableRepository.save(existingBookable);
        return BookableMapper.toDTO(updatedBookable);
    }

}
