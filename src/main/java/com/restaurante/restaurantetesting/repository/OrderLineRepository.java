package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
    List<OrderLine> findByOrderId(Long id); //todas las líneas de un pedido.

    //busca la línea de un pedido para un plato concreto. Devuelve Optional porque puede no existir. Sirve para: si ya
    //ediste ese plato, sumar 1 a la cantidad en vez de crear una línea duplicada.
    List<OrderLine> findByOrder_Id(Long id);

    Optional<OrderLine> findByOrder_IdAndDish_Id(Long id, Long dishId);  // una línea concreta (pedido + plato)

    @Query("""
          SELECT SUM(ol.quantity * ol.dish.price)
          FROM OrderLine ol where ol.order.id = ?1
          """)

    //suma cantidad × precio de todas las líneas (el mismo cálculo que tenés en OrderRepository, pero el profe lo usa desde acá en el controlador)
    Double calculateTotalPrice(Long orderId);
}