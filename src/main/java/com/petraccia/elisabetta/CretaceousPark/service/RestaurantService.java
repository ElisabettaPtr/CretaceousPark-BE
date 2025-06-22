package com.petraccia.elisabetta.CretaceousPark.service;

import com.petraccia.elisabetta.CretaceousPark.dto.RestaurantDTO;
import com.petraccia.elisabetta.CretaceousPark.exception.BadRequestException;
import com.petraccia.elisabetta.CretaceousPark.exception.ResourceNotFoundException;
import com.petraccia.elisabetta.CretaceousPark.mapper.RestaurantMapper;
import com.petraccia.elisabetta.CretaceousPark.model.Restaurant;
import com.petraccia.elisabetta.CretaceousPark.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public List<RestaurantDTO> getAllRestaurants() {
        return restaurantRepository.findAll()
                .stream()
                .map(RestaurantMapper::toDTO)
                .toList();
    }

    public RestaurantDTO getRestaurantById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));

        return RestaurantMapper.toDTO(restaurant);
    }

    public RestaurantDTO getRestaurantByName(String name) {
        if (name == null) {
            throw new BadRequestException("Restaurant name must not be null.");
        }

        Restaurant restaurant = restaurantRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with name: " + name));

        return RestaurantMapper.toDTO(restaurant);
    }

    public RestaurantDTO saveRestaurant(RestaurantDTO restaurantDTO) {
        if (restaurantDTO.getName() == null) {
            throw new BadRequestException("Restaurant name must not be null.");
        }

        if (restaurantDTO.getKitchenType() == null) {
            throw new BadRequestException("Restaurant kitchen type must not be null.");
        }

        Restaurant restaurantToSave = RestaurantMapper.toEntity(restaurantDTO);
        Restaurant savedRestaurant = restaurantRepository.save(restaurantToSave);
        return RestaurantMapper.toDTO(savedRestaurant);
    }

    @Transactional
    public void deleteRestaurantById(Long id) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }

        Restaurant restaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));

        restaurantRepository.deleteById(id);
    }

    public RestaurantDTO updateRestaurant(Long id, RestaurantDTO restaurantDTO) {
        if (id == null) {
            throw new BadRequestException("ID must not be null.");
        }
        if (restaurantDTO == null) {
            throw new BadRequestException("Restaurant data must not be null.");
        }

        Restaurant existingRestaurant = restaurantRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Restaurant not found with id: " + id));


        existingRestaurant.setName(restaurantDTO.getName());
        existingRestaurant.setKitchenType(restaurantDTO.getKitchenType());

        Restaurant updatedrestaurant = restaurantRepository.save(existingRestaurant);
        return RestaurantMapper.toDTO(updatedrestaurant);
    }

}
