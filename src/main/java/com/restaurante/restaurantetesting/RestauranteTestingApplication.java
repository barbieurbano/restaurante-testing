package com.restaurante.restaurantetesting;

import com.restaurante.restaurantetesting.model.Restaurant;
import com.restaurante.restaurantetesting.repository.RestaurantRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestauranteTestingApplication {

    public static void main(String[] args) {

        var context = SpringApplication.run(RestauranteTestingApplication.class, args);
        RestaurantRepository restaurantRepository = context.getBean(RestaurantRepository.class);
        restaurantRepository.save(Restaurant.builder().name("Roque de los pescadores").averagePrice(35.5).build());
        restaurantRepository.save(Restaurant.builder().name("La Sirena").averagePrice(18.5).build());
        restaurantRepository.save(Restaurant.builder().name("La Capitana").averagePrice(15.4).build());
        restaurantRepository.save(Restaurant.builder().name("La Ola").averagePrice(9.7).build());
        restaurantRepository.save(Restaurant.builder().name("Margaretha").averagePrice(12.0).build());
        restaurantRepository.save(Restaurant.builder().name("Casa Marcos").averagePrice(14.3).build());
        restaurantRepository.save(Restaurant.builder().name("Restaurante Mahoh").averagePrice(28.9).build());
    }
}
