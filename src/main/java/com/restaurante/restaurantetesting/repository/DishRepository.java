package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Long> {
}