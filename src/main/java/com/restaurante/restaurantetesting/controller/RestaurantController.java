package com.restaurante.restaurantetesting.controller;

import com.restaurante.restaurantetesting.repository.RestaurantRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;

    public RestaurantController(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("restaurantes")
    public String restaurants(Model model){

        //Ejemplos DATOS (porque teniamos datos en testing solamente)

        model.addAttribute("restaurantes", restaurantRepository.findAll());
        model.addAttribute("saludo", "Bienvenido a la lista de restaurantes");
        return "restaurant-list";
    }
}
