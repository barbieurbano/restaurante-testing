package com.restaurante.restaurantetesting.controller;

import com.restaurante.restaurantetesting.model.Dish;
import com.restaurante.restaurantetesting.model.Order;
import com.restaurante.restaurantetesting.model.OrderLine;
import com.restaurante.restaurantetesting.model.Restaurant;
import com.restaurante.restaurantetesting.model.enums.OrderStatus;
import com.restaurante.restaurantetesting.repository.DishRepository;
import com.restaurante.restaurantetesting.repository.OrderLineRepository;
import com.restaurante.restaurantetesting.repository.OrderRepository;
import com.restaurante.restaurantetesting.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class OrderControllerTest {
//Estos son test de integracion porque cargas todas las capas, sprint base de datos, controladores
//cuando tengamos servicios ahi es donde se hacen los test unitarios
    @Autowired MockMvc mockMvc;
    @Autowired RestaurantRepository restaurantRepo;
    @Autowired DishRepository dishRepo;
    @Autowired OrderRepository orderRepo;
    @Autowired OrderLineRepository orderLineRepo;

    Restaurant restaurant;
    Dish ensalada, lentejas, flan;
    Order order;

    @BeforeEach
    void setUp(){
        restaurant = restaurantRepo.save(Restaurant.builder().name("Restaurante 1").build());
        ensalada = dishRepo.save(Dish.builder().name("Ensalada").price(10d).restaurant(restaurant).build());
        lentejas = dishRepo.save(Dish.builder().name("Lentejas").price(15d).restaurant(restaurant).build());
        flan = dishRepo.save(Dish.builder().name("Flan").price(5d).restaurant(restaurant).build());
        order = orderRepo.save(Order.builder().restaurant(restaurant).status(OrderStatus.PENDING).numPeople(2).build());
    }

    //Las operaciones CRUD si son obligatorios los test, list, detail y create
    //porque mockMvc te obligaba que si salta una excepcion te da un mensaje de esa exception
    @Test
    @DisplayName("GET / orders")
    void list() throws Exception {
    }

    @Test
    @DisplayName("GET /orders/{id}")
    void detail() throws Exception{
    }

    //Verifica que se ha creado un orderline
    @DisplayName("GET /orders/new?restaurantId={id}")
    @Test
    void newOrder() throws Exception{

    }

    //Cuando creas en este necesitas pasarle como param los numero de comensales, sugerencias. viaja en el cuerpo de la peticion
    @DisplayName("POST /orders")
    @Test
        void createOrder() throws Exception{
    }

    @Test
    @DisplayName("POST /orders/{orderId}/lines?dishId={id}")
    void createLine()throws Exception{

    }
    @Test
    @DisplayName("GET /orders/{orderId}/lines/{lineId}/delete")
    void deleteLine() throws Exception{

    }

    @Test
    @DisplayName("POST /orders/{orderId}/lines/{lineId}/delete")
    void updateLine(){

    }
    @Test
    @DisplayName("POST /orders/{orderId}/finish?tip=0")
    void finishOrder()  throws Exception{

    }
}
