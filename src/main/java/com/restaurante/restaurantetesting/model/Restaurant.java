package com.restaurante.restaurantetesting.model;

import com.restaurante.restaurantetesting.model.enums.FoodType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter // lombok
@Setter // lombok
@ToString // lombok
@NoArgsConstructor // lombok: crea el constructor vacío sin argumentos
@AllArgsConstructor // lombok: crea el constructor con todos los params
@Builder
@Table(name = "Restaurantes")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private Double averagePrice;

    @Builder.Default
    @Column(columnDefinition = "BOOLEAN DEFAULT true") // Por defecto true
    private Boolean active = true;

    @Enumerated(EnumType.STRING)
    private FoodType foodType;

    public Restaurant(String name, Double averagePrice) {
        this.name = name;
        this.averagePrice = averagePrice;
        this.active = true;
    }

    public Restaurant(String name) {
        this.name = name;
    }
}
