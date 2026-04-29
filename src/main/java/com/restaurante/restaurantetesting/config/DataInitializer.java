package com.restaurante.restaurantetesting.config;

import com.restaurante.restaurantetesting.model.Dish;
import com.restaurante.restaurantetesting.model.Restaurant;
import com.restaurante.restaurantetesting.model.enums.DishType;
import com.restaurante.restaurantetesting.model.enums.FoodType;
import com.restaurante.restaurantetesting.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

//No tiene un rol especifico
@Component//Para que los framework (Spring) entienda que tiene que hacer con el codigo, similar a services
@AllArgsConstructor
@Profile("!test") //hay que activarlos en properties
public class DataInitializer implements CommandLineRunner {
    private DishRepository dishRepository;
    private RestaurantRepository restaurantRepository;
    private OrderRepository orderRepository;
    private OrderLineRepository orderLineRepository;
    private ReviewRepository reviewRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Hola desde data initializer");
        //mejor save para quedarnos con las asociaciones

        if(restaurantRepository.count() > 0) return;

        var r1 = restaurantRepository.save(Restaurant.builder().name("R1").foodType(FoodType.ARGENTINIAN).averagePrice(45.5).build());
        var r2 = restaurantRepository.save(Restaurant.builder().name("R2").foodType(FoodType.JAPANESE).averagePrice(30.0).build());
        var r3 = restaurantRepository.save(Restaurant.builder().name("R3").foodType(FoodType.SPANISH).averagePrice(25.6).build());

        var p1 = dishRepository.save(Dish.builder().type(DishType.STARTER).name("Plato 1").price(9.99).restaurant(r1).build());
        var p2 = dishRepository.save(Dish.builder().type(DishType.MAIN_COURSE).name("Plato 2").price(29.99).restaurant(r2).build());
        var p3 = dishRepository.save(Dish.builder().type(DishType.DESSERT).name("Plato 3").price(19.99).restaurant(r2).build());


    }
}
