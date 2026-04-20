package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findByPriceLessThanEqual(Double price);

    @Query("select d from Dish d order by d.price") // asc por defecto
    List<Dish> findOrderedByPriceAsc();
}