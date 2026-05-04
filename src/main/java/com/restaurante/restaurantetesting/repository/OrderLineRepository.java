package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
    List<OrderLine> findByOrderId(Long id);
}