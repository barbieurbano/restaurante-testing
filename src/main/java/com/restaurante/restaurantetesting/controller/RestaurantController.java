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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String restaurants(
            Model model,
            //El price debe ser el que indicas en la url
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String title
    ){

        //Ejemplos DATOS (porque teniamos datos en testing solamente)

        model.addAttribute("restaurants", restaurantRepository.findActiveFiltering(price, title));
        model.addAttribute("saludo", "Bienvenido a la lista de restaurantes");
        return "restaurant/restaurant-list";
    }

    @GetMapping("/restaurants/{id}")
    public String restaurantDetail(@PathVariable Long id, Model model){
        Optional<Restaurant> restauranteOptional = restaurantRepository.findByAndActiveTrue(id);

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

    @GetMapping("/restaurants/deactivate/{id}")
    public String deactivateRestaurant(@PathVariable Long id, Model model){
        //El optional porque puede no existir, entonces para que no te de nulo
//        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
//        if (restaurantOptional.isPresent()){
//            Restaurant restaurant =  restaurantOptional.get();
//            restaurant.setActive(false);
//            restaurantRepository.save(restaurant);
//        }
//        return "redirect:/restaurants";

        //Forma 2(optional)

        restaurantRepository.findById(id).ifPresent(restaurant -> {
            restaurant.setActive(false);
            restaurantRepository.save(restaurant);
        });
        return "redirect:/restaurants";

        //PASO 1: Navegar a formulario de creacion de restaurante, luego se necesita uno parecito para editar pasandole el id
        @GetMapping("restaurants/new")
        public String navigateToForm(Model model){
            model.addAttribute("restaurant", new Restaurant());
            return"restaurants/restaurant-form";
        }
        //PASO 2: Recibir los datos del formulario de restaurante, los datos viajan por body
        @PostMapping()
        public String
    }
    //@GetMapping("bookings/confirm/{id}")
    //@GetMapping("bookings/cancel")



}
