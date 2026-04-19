package com.restaurante.restaurantetesting.model;

import com.restaurante.restaurantetesting.model.enums.DishType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "dishes")
@AllArgsConstructor
@ToString
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(length = 500) //Le extendemos hasta 500
    private String description; // por defecto 255

    private Double price;

    private Boolean active;

    // "/2026/04/15/profile-pepe.jpg
    // https://img.hosting.com/pepe.jpg
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private DishType type;

    @ManyToOne
    private Restaurant restaurant;
}
