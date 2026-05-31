package com.restaurante.restaurantetesting.model;

import com.restaurante.restaurantetesting.model.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "orders")
@AllArgsConstructor
@ToString
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default // Permite que Lombok lea el valor que asignamos a esta propiedad
    private LocalDateTime date = LocalDateTime.now();

    private Integer tableNumber;
    private Integer numPeople;

    //Debería calcularse en base a sus OrderLine
    private Double totalPrice;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private OrderStatus status = OrderStatus.PENDING;

    @Column(length = 500)
    private String userSuggestions;

    private Double tip;

    @ToString.Exclude
    @ManyToOne
    private User user;

    @ToString.Exclude
    @ManyToOne
    private Restaurant restaurant;

}
