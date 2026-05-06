package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.Restaurant;
import com.restaurante.restaurantetesting.model.enums.FoodType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
List<Restaurant> findAllByActiveTrue();
//Como es un solo elemento usamos el optional
Optional<Restaurant> findByAndActiveTrue(Long id);
//Esto es JPQL --> Es como sql pero orientado a entidades de Java
    //El ?1 es un parametro posicional es mas optimo porq es mas rapido pero
    @Query("""
        SELECT r FROM Restaurant r
        WHERE r.active = true
        AND (:price IS NULL OR r.averagePrice <= :price)      
        AND (:title IS NULL OR LOWER(r.name) LIKE LOWER(concat('%', :title ,'%')))  
        AND (:footType IS NULL OR r.foodType = :foodType ) 
        """)
    List<Restaurant> findActiveFiltering(
            @Param("price") Double price,
            @Param("title") String title),
            @Param("foodType") FoodType foodType);

}