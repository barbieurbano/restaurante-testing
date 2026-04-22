package com.restaurante.restaurantetesting.controller;

import com.restaurante.restaurantetesting.model.Dish;
import com.restaurante.restaurantetesting.model.Restaurant;
import com.restaurante.restaurantetesting.repository.DishRepository;
import com.restaurante.restaurantetesting.repository.RestaurantRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Controller
public class RestaurantController {

    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;
    //OrderRepository
    //EmployeeRepository


    public RestaurantController(RestaurantRepository restaurantRepository, DishRepository dishRepository) {
        this.restaurantRepository = restaurantRepository;
        this.dishRepository = dishRepository;
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
        Optional<Restaurant> restauranteOptional = restaurantRepository.findById(id);

        if(restauranteOptional.isPresent()){
            model.addAttribute("restaurante", restauranteOptional.get());
            List<Dish> platos = dishRepository.findByRestaurant_Id(id);
            model.addAttribute("Dishes", platos);
            //Traer y encargar empleados
            //Traer y cargar las review
            //
            return "restaurant-detail";
        }
        else{
            return "redirect:/restaurantes";
        }
    }
}
