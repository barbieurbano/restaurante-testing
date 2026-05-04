package com.restaurante.restaurantetesting.config;

import com.restaurante.restaurantetesting.model.*;
import com.restaurante.restaurantetesting.model.enums.DishType;
import com.restaurante.restaurantetesting.model.enums.FoodType;
import com.restaurante.restaurantetesting.model.enums.OrderStatus;
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

        //Restaurantes
        var r1 = restaurantRepository.save(Restaurant.builder().name("Restaurante 1").foodType(FoodType.ARGENTINIAN).averagePrice(45.5).build());
        var r2 = restaurantRepository.save(Restaurant.builder().name("Restaurante 2").foodType(FoodType.JAPANESE).averagePrice(30.0).build());
        var r3 = restaurantRepository.save(Restaurant.builder().name("Restaurante 3").foodType(FoodType.SPANISH).averagePrice(25.6).build());

        //Platos
        var p1 = dishRepository.save(Dish.builder().type(DishType.STARTER).name("Plato 1").price(9.99).restaurant(r1).build());
        var p2 = dishRepository.save(Dish.builder().type(DishType.MAIN_COURSE).name("Plato 2").price(29.99).restaurant(r2).build());
        var p3 = dishRepository.save(Dish.builder().type(DishType.DESSERT).name("Plato 3").price(19.99).restaurant(r2).build());
        var p4 = dishRepository.save(Dish.builder().type(DishType.DESSERT).name("Plato 3").price(19.99).restaurant(r2).build());
        var p5 = dishRepository.save(Dish.builder().type(DishType.DESSERT).name("Plato 3").price(19.99).restaurant(r2).build());
        var p6 = dishRepository.save(Dish.builder().type(DishType.DESSERT).name("Plato 3").price(19.99).restaurant(r2).build());

        //Reviews
        var review1 = reviewRepository.save(Review.builder().title("OK").rating(3).restaurant(r1).build());
        var review2 = reviewRepository.save(Review.builder().title("Malisimo").rating(1).restaurant(r1).build());
        var review3 = reviewRepository.save(Review.builder().title("Espectacular").rating(5).restaurant(r1).build());

        var review4 = reviewRepository.save(Review.builder().title("OK").rating(4).dish(p1).restaurant(r1).build());
        var review5 = reviewRepository.save(Review.builder().title("OK").rating(4).dish(p1).restaurant(r1).build());
        var review6 = reviewRepository.save(Review.builder().title("OK").rating(4).dish(p1).restaurant(r1).build());

        //Pedidos y lineas pedido
        var order1 = orderRepository.save(Order.builder().numPeople(2).tableNumber(1).totalPrice(30.0).status(OrderStatus.PENDIENTE).restaurant(r1).build());
        var order2 = orderRepository.save(Order.builder().numPeople(4).tableNumber(2).totalPrice(60.0).status(OrderStatus.PAGADO).restaurant(r1).build());
        var order3 = orderRepository.save(Order.builder().numPeople(6).tableNumber(3).totalPrice(70.0).status(OrderStatus.PENDIENTE).restaurant(r1).build());

        var line1 = orderLineRepository.save(OrderLine.builder().quantity(2).dish(p1).order(order1).build());
        var line2 = orderLineRepository.save(OrderLine.builder().quantity(1).dish(p2).order(order1).build());
        var line3 = orderLineRepository.save(OrderLine.builder().quantity(3).dish(p3).order(order1).build());
        var line4 = orderLineRepository.save(OrderLine.builder().quantity(3).dish(p4).order(order2).build());
        var line5 = orderLineRepository.save(OrderLine.builder().quantity(3).dish(p5).order(order2).build());
        var line6 = orderLineRepository.save(OrderLine.builder().quantity(3).dish(p6).order(order2).build());


    }
}
