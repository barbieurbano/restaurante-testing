package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
}