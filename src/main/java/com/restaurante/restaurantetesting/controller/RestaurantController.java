package com.restaurante.restaurantetesting.controller;

import com.restaurante.restaurantetesting.model.Restaurant;
import com.restaurante.restaurantetesting.repository.RestaurantRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    //DishRepository
    //OrderRepository
    //EmployeeRepository

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

    @GetMapping("/restaurantes/{id}")
    public String restaurantDetail(@PathVariable Long id, Model model){
        Optional<Restaurant> optional = restaurantRepository.findById(id);

        if(optional.isPresent()){
            model.addAttribute("restaurante", optional.get());
            return "restaurant-detail";
        }
        else{
            return "redirect:/restaurantes";
        }
    }
}
