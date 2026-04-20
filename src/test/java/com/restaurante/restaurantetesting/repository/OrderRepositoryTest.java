package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.Order;
import com.restaurante.restaurantetesting.model.Restaurant;
import com.restaurante.restaurantetesting.model.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@DisplayName("Tests de pedidos en BD")
class OrderRepositoryTest {
    //restaurantRepository
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    OrderRepository orderRepository;

    //Declarar DATOS para las pruebas
    Restaurant restaurante1;
    Order order1;

    @BeforeEach
    void setUp(){
        //INICIALIZAR DATOS de prueba
        restaurante1 = restaurantRepository.save(Restaurant.builder().name("Margaretha").build());

        //CREAR y GUARDAR un pedido en BD
        order1 = orderRepository.save(Order.builder().restaurant(restaurante1).numPeople(2).tableNumber(1).build());
        //Platos
        //Lineas pedido orderline
    }

    @Test
    void verificarValoresPorDefecto(){
        //FECHA Y ESTADO pendiente
        assertNotNull(order1.getId());
        assertNotNull(order1.getDate());
        assertEquals(LocalDateTime.now().toLocalDate(),order1.getDate().toLocalDate());
        assertEquals(OrderStatus.PENDIENTE, order1.getStatus());
    }

    @Test
    void findAllByRestaurant(){
        List<Order> pedidos = orderRepository.findByRestaurantId(restaurante1.getId());
        assertEquals(1, pedidos.size());
    }

    @Test
    void calculateTotalPrice(){
        Double totalPrice =  orderRepository.calculateTotalPrice(order1.getId());
        assertEquals(50.0, totalPrice);
    }















}