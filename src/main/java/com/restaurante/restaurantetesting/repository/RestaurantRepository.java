package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
List<Restaurant> findAllByActiveTrue();
//Como es un solo elemento usamos el optional
Optional<Restaurant> findByAndActiveTrue(Long id);
}