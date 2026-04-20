package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByRestaurantId(Long id);

    // Calcular precio total de un pedido en base a sus líneas de pedido
    // calculate total price based on order lines
    // JPQL
    @Query("select sum(lineaPedido.dish.price * lineaPedido.quantity) from OrderLine lineaPedido where lineaPedido.order.id = ?1")
    Double calculateTotalPrice(Long orderId);
}