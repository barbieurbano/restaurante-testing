package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLineRepository extends JpaRepository<OrderLine, Long> {
}