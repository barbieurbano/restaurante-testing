package com.restaurante.restaurantetesting.model;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reviews")
@ToString(exclude = {"restaurant", "dish"}) //es lo mismo que @ToString.Exclude
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String content;

    private Integer rating;

    @Builder.Default
    private LocalDateTime creationDate = LocalDateTime.now();

    //asociaciones
    @ManyToOne
    private Restaurant restaurant;
    @ManyToOne
    private Dish dish;
}
