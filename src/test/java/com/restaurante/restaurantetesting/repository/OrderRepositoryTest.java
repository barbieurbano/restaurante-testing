package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.Order;
import com.restaurante.restaurantetesting.model.Restaurant;
import com.restaurante.restaurantetesting.model.Dish;
import com.restaurante.restaurantetesting.model.OrderLine;
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
    @Autowired
    DishRepository dishRepository;
    @Autowired
    OrderLineRepository orderLineRepository;

    //Declarar DATOS para las pruebas
    Restaurant restaurante1;
    Order order1;

    @BeforeEach
    void setUp(){
        //INICIALIZAR DATOS de prueba
        restaurante1 = restaurantRepository.save(Restaurant.builder().name("Margaretha").build());

        //CREAR y GUARDAR un pedido en BD
        order1 = orderRepository.save(Order.builder().restaurant(restaurante1).numPeople(2).tableNumber(1).build());
        //Platos (con precios)
        Dish ensalada = dishRepository.save(Dish.builder().name("Ensalada").price(10.0).restaurant(restaurante1).build());
        Dish lentejas = dishRepository.save(Dish.builder().name("Lentejas").price(15.0).restaurant(restaurante1).build());

        //Lineas pedido orderline:  10*2 + 15*2 = 50
        orderLineRepository.save(OrderLine.builder().order(order1).dish(ensalada).quantity(2).build());
        orderLineRepository.save(OrderLine.builder().order(order1).dish(lentejas).quantity(2).build());

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
//Esto hace: pide el precio total del pedido (la suma de precio × cantidad de cada línea de pedido) y espera 50.0. Pero tu setUp() crea el pedido
//  sin líneas ni platos (mirá los comentarios //Platos //Lineas pedido orderline que dejaste pendientes). Sin líneas, la suma da null/0, no 50.
    @Test
    void calculateTotalPrice(){
        Double totalPrice =  orderRepository.calculateTotalPrice(order1.getId());
        assertEquals(50.0, totalPrice); //confirma que la consulta suma correctamente las líneas del pedido (10×2 + 15×2). Si la consulta calculara mal, ofaltaran líneas, el número no daría 50.
    }















}