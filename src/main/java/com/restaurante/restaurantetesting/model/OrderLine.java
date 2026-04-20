package com.restaurante.restaurantetesting.model;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "order_lines")
@AllArgsConstructor
@ToString
public class OrderLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer quantity;

    @ToString.Exclude
    @ManyToOne
    private Order order;

    @ToString.Exclude
    @ManyToOne
    private Dish dish;
}
