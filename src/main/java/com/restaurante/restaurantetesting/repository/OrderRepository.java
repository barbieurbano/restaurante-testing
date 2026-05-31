package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.Order;
import com.restaurante.restaurantetesting.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByRestaurantId(Long id);

    // Calcular precio total de un pedido en base a sus líneas de pedido
    // calculate total price based on order lines
    // JPQL


    //trae los pedidos de un usuario concreto. El _ le dice a Spring que navegue la asociación user.id (pedido → user → id). Sirve para que un cliente
    //  vea solo sus pedidos.
    List<Order> findByUser_Id(Long id);

    @Query("""
      select COALESCE(sum(lineaPedido.dish.price * lineaPedido.quantity), 0.0)
      from OrderLine lineaPedido
      where lineaPedido.order.id = ?1
      """)

    Double calculateTotalPrice(Long orderId);


}