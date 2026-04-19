package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}