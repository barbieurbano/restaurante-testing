package com.restaurante.restaurantetesting;

import com.restaurante.restaurantetesting.model.Dish;
import com.restaurante.restaurantetesting.model.Restaurant;
import com.restaurante.restaurantetesting.model.Review;
import com.restaurante.restaurantetesting.model.enums.DishType;
import com.restaurante.restaurantetesting.model.enums.FoodType;
import com.restaurante.restaurantetesting.repository.DishRepository;
import com.restaurante.restaurantetesting.repository.RestaurantRepository;
import com.restaurante.restaurantetesting.repository.ReviewRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class RestauranteTestingApplication {

    public static void main(String[] args) {

        SpringApplication.run(RestauranteTestingApplication.class, args);
//        //Introducir DATOS DEMO para que el HTML tenga datos.
//        RestaurantRepository restaurantRepository = context.getBean(RestaurantRepository.class);
//        DishRepository dishRepository = context.getBean(DishRepository.class);
//        ReviewRepository reviewRepository = context.getBean(ReviewRepository.class);
//
//        restaurantRepository.save(Restaurant.builder().name("Roque de los pescadores").averagePrice(35.5).active(false).foodType(FoodType.JAPANESE).build());
//        restaurantRepository.save(Restaurant.builder().name("La Sirena").averagePrice(18.5).active(false).foodType(FoodType.SPANISH).build());
//        restaurantRepository.save(Restaurant.builder().name("La Capitana").averagePrice(15.4).active(false).foodType(FoodType.MEXICAN).build());
//        restaurantRepository.save(Restaurant.builder().name("La Ola").averagePrice(9.7).active(false).foodType(FoodType.MEXICAN).build());
//        restaurantRepository.save(Restaurant.builder().name("Margaretha").averagePrice(12.0).active(false).foodType(FoodType.JAPANESE).build());
//        Restaurant restaurant6 = restaurantRepository.save(Restaurant.builder().name("Casa Marcos").averagePrice(14.3).active(true).foodType(FoodType.ARGENTINIAN).build());
//        restaurantRepository.save(Restaurant.builder().name("Restaurante Mahoh").averagePrice(28.9).active(true).foodType(FoodType.ARGENTINIAN).build());
//        restaurantRepository.save(Restaurant.builder().name("Restaurante Villaverde").averagePrice(12.0).active(true).foodType(null).build());
//
//        dishRepository.save(Dish.builder()
//                .name("Pizza caprichosa")
//                .price(12.3)
//                .restaurant(restaurant6)
//                .active(true)
//                .type(DishType.MAIN_COURSE)
//                .imageUrl("https://es.wikipedia.org/wiki/Archivo:Pizza_capricciosa.jpg")
//                .build());
//
//        reviewRepository.saveAll(List.of(
//                Review.builder().title("Perfecto").content("Todo espectacular").rating(5).restaurant(restaurant6).build(),
//                Review.builder().title("Espectacular").content("Todo espectacular").rating(5).restaurant(restaurant6).build(),
//                Review.builder().title("Magnifico").content("La cocina de los dioses").rating(5).restaurant(restaurant6).build(),
//                Review.builder().title("Bueno, ni tan mal").content("La comida podria mejorar, la atencion muy buena").rating(3).restaurant(restaurant6).build(),
//                Review.builder().title("Malo").content("La comida estaba quemada").rating(1).restaurant(restaurant6).build()));
    }
}
