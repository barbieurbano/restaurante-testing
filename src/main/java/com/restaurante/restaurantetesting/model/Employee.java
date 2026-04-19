package com.restaurante.restaurantetesting.model;

import com.restaurante.restaurantetesting.model.enums.WorkLevel;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@Entity
@Table(name = "employees")
@AllArgsConstructor
@ToString
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(unique = true)
    private String nif;

    @Column(name = "active")
    private Boolean active;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('JUNIOR', 'SENIOR') DEFAULT 'SENIOR'")
    private WorkLevel level = WorkLevel.SENIOR;

    private LocalDate startDate = LocalDate.now();
    @ToString.Exclude
    @ManyToOne
    private Restaurant restaurant;

}
