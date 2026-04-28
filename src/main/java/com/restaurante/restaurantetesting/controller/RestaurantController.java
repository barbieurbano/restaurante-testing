package com.restaurante.restaurantetesting.controller;

import com.restaurante.restaurantetesting.model.Dish;
import com.restaurante.restaurantetesting.model.Restaurant;
import com.restaurante.restaurantetesting.model.Review;
import com.restaurante.restaurantetesting.repository.DishRepository;
import com.restaurante.restaurantetesting.repository.RestaurantRepository;
import com.restaurante.restaurantetesting.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;
    private final ReviewRepository reviewRepository;
    //OrderRepository
    //EmployeeRepository


    @GetMapping("restaurants") //CONTROLADOR
    public String restaurants(Model model){

        //Ejemplos DATOS (porque teniamos datos en testing solamente)

        model.addAttribute("restaurants", restaurantRepository.findAll());
        model.addAttribute("saludo", "Bienvenido a la lista de restaurantes");
        return "restaurant/restaurant-list";
    }

    @GetMapping("/restaurants/{id}")
    public String restaurantDetail(@PathVariable Long id, Model model){
        Optional<Restaurant> restauranteOptional = restaurantRepository.findById(id);

        if(restauranteOptional.isPresent()){

            model.addAttribute("restaurant", restauranteOptional.get());
            List<Dish> platos = dishRepository.findByRestaurant_Id(id);
            model.addAttribute("dishes", platos);
            //Traer y encargar empleados
            //Traer y cargar las review
            //reviewRepository.findByRestaurantId
            List<Review> reviews = reviewRepository.findByRestaurant_IdOrderByCreationDateDesc(id); //trae todas las resenias ordenando de mas nuevo a mas antiguo

            return "restaurant/restaurant-detail";
        }
        else{
            return "redirect:/restaurants";
        }
    }
}
