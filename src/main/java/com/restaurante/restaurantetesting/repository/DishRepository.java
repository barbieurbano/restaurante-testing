package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DishRepository extends JpaRepository<Dish, Long> {

    List<Dish> findByRestaurant_Id(Long id);

    List<Dish> findByPriceLessThanEqual(Double price);

    //trae los platos de un restaurante ordenados por precio. Se usa en el detalle del pedido, para mostrarle al usuario la carta y que elija platos.
    List<Dish> findByRestaurantIdOrderByPrice(Long id);

    @Query("select d from Dish d order by d.price") // asc por defecto
    List<Dish> findOrderedByPriceAsc();
}