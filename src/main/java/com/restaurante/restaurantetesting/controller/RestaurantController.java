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
import org.springframework.web.bind.annotation.*;

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

//Pide al repositorio los restaurantes activos (filtrando por precio/título si vienen en la URL) y muestra la lista
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
        return "restaurants/restaurant-list";
    }
//busca UN restaurante activo por id; si existe, carga sus platos y reseñas y muestra el detalle; si no existe, redirige a la lista.
    @GetMapping("/restaurants/{id}")
    public String restaurantDetail(@PathVariable Long id, Model model){
        Optional<Restaurant> restauranteOptional = restaurantRepository.findByIdAndActiveTrue(id);

        if(restauranteOptional.isPresent()){

            model.addAttribute("restaurant", restauranteOptional.get());
            List<Dish> platos = dishRepository.findByRestaurant_Id(id);
            model.addAttribute("dishes", platos);
            //Traer y encargar empleados
            //Traer y cargar las review
            //reviewRepository.findByRestaurantId
            List<Review> reviews = reviewRepository.findByRestaurant_IdOrderByCreationDateDesc(id); //trae todas las resenias ordenando de mas nuevo a mas antiguo
            model.addAttribute("reviews", reviews);

            return "restaurants/restaurant-detail";
        }
        else{
            return "redirect:/restaurants";
        }
    }
// busca UN restaurante por id y, si existe, le pone active = false y lo guarda (borrado "lógico", no se elimina de la BD)
    @GetMapping("/restaurants/deactivate/{id}")
    public String deactivateRestaurant(@PathVariable Long id, Model model) {
        //El optional porque puede no existir, entonces para que no te de nulo
//        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(id);
//        if (restaurantOptional.isPresent()){
//            Restaurant restaurant =  restaurantOptional.get();
//            restaurant.setActive(false);
//            restaurantRepository.save(restaurant);
//        }
//        return "redirect:/restaurants";

        //Forma 2 (optional)

        restaurantRepository.findById(id).ifPresent(restaurant -> {
            restaurant.setActive(false);
            restaurantRepository.save(restaurant);
        });
        return "redirect:/restaurants";
    }

    // crea UN Restaurant vacío y muestra el formulario
    //PASO 1: Navegar a formulario de creacion de restaurante, luego se necesita uno parecito para editar pasandole el id
    @GetMapping("restaurants/new")
    public String navigateToForm (Model model){
        model.addAttribute("restaurant", new Restaurant());
        return"restaurants/restaurant-form";
    }


    //PASO 2: Recibir los datos del formulario de restaurante, los datos viajan por body
    @PostMapping("restaurants")
    public String save(@ModelAttribute Restaurant restaurant){
        restaurantRepository.save(restaurant);
        return"redirect:/restaurants";
    }
    //Reutiliza el mismo formulario (restaurant-form), pero en vez de un restaurante vacío, le pasa el existente para que el form aparezca relleno
    @GetMapping("restaurants/edit/{id}")
    public String editRestaurant(@PathVariable Long id, Model model) {
        model.addAttribute("restaurant", restaurantRepository.findById(id).orElseThrow());
        return "restaurants/restaurant-form";
    }

    //@GetMapping("bookings/confirm/{id}")
    //@GetMapping("bookings/cancel")
    }


