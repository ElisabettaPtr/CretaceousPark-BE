package com.petraccia.elisabetta.CretaceousPark.repository;

import com.petraccia.elisabetta.CretaceousPark.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

    Optional<Restaurant> findByName(String name);

}
