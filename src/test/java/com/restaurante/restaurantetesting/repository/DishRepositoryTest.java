package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.Dish;
import com.restaurante.restaurantetesting.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
class DishRepositoryTest {
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    DishRepository dishRepository;

    Restaurant restaurant;
    Restaurant restaurant2;

    @BeforeEach
    void setUp(){
        //Dos restaurantes
        restaurant = Restaurant.builder().name("Roque de los pescadores").active(true).build();
        restaurant2 = Restaurant.builder().name("La luna").active(true).build();
        restaurantRepository.saveAll(List.of(restaurant, restaurant2));

        //Agregar precio a Dish, poner precios desordenados
        var plato1 = Dish.builder().name("Plato 1").price(92.0).restaurant(restaurant).build();
        var plato2 = Dish.builder().name("Plato 2").price(73.0).restaurant(restaurant).build();
        var plato3 = Dish.builder().name("Plato 3").price(6.0).restaurant(restaurant).build();
        var plato4 = Dish.builder().name("Plato 4").price(12.0).restaurant(restaurant).build();
        var plato5 = Dish.builder().name("Plato 5").price(45.0).restaurant(restaurant).build();
        var plato6 = Dish.builder().name("Plato 6").price(3.99).restaurant(restaurant).build();
        dishRepository.saveAll(List.of(plato1, plato2, plato3, plato4, plato5, plato6));
    }
    @Test
    void findByRestaurant_Id(){
        List<Dish> platos = dishRepository.findByPriceLessThanEqual(10.0);
        assertEquals(2, platos.size());
        for(Dish dish : platos){
            assertTrue(dish.getPrice() <= 10.0);
        }
    }

    @Test
    void findOrderedByPriceAsc(){
        List<Dish> platos = dishRepository.findOrderedByPriceAsc();
        assertEquals(6, platos.size());

        for(int i = 0; platos.size() > i; i++){
            Double precioActual = platos.get(i).getPrice();
            Double precioSiguiente = platos.get(i + 1).getPrice();
            assertTrue(precioActual <= precioSiguiente);
        }
    }





















}