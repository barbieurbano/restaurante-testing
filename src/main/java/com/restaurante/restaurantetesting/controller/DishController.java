package com.restaurante.restaurantetesting.controller;

import com.restaurante.restaurantetesting.model.Dish;
import com.restaurante.restaurantetesting.model.Review;
import com.restaurante.restaurantetesting.repository.DishRepository;
import com.restaurante.restaurantetesting.repository.RestaurantRepository;
import com.restaurante.restaurantetesting.repository.ReviewRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@AllArgsConstructor
public class DishController {
    private final DishRepository dishRepository;
    private final ReviewRepository reviewRepository;//una vez se crea el repositorio no va a cambiar por eso el final
    private final RestaurantRepository restaurantRepository;

    @GetMapping("dishes")
    public String dishes(Model model){
        model.addAttribute("dishes", dishRepository.findAll());
        return "dishes/dish-list";
    }

    //traemos la entidad y las asociaciones que tenga podemos tener alergenos, ingredientes, photos
    @GetMapping("dishes/{id}") // Alomejor para un ADMIN si le sirve ver todas las reviews
    public String dishDetail(Model model, @PathVariable  Long id){
        model.addAttribute("dish", dishRepository.findById(id).orElseThrow());
        model.addAttribute("reviews", reviewRepository.findByDish_IdOrderByCreationDateDesc(id));
        return "dishes/dish-detail";
    }

    //GetMapping ("dishes/new")
    //create e sun metodo que te desplaza a la pantalla new y ver un v-formulario qe te deja ver un selector de many to one,como lo tiene con el Restaurant

    //Que se puede hacer un building , y si quieres cargar ingredientes (si los tuvieramos), alergenos para poder mostrarlo en el formulario
    //Si quieres cargar los restaurantes, debes cargarlos aqui
    //Luego de hacer esto probarlo, tenemos que tener el html y un boton que lo llame. Aunque desde el DishControllerTest ya se podria testear.
    //Para testearlo seria con un VIEW Y UN MODEL.
    @GetMapping("dishes/new")
    public String navigateToForm(Model model){
        model.addAttribute("dish", new Dish());
        model.addAttribute("restaurants", restaurantRepository.findAll());
        return "dishes/dish-form";
    }

    //GetMapping("dishes/edit/{id}")
    @GetMapping("dishes/edit/{id}")
    public String editDish(@PathVariable Long id, Model model){
        model.addAttribute("dish",dishRepository.findById(id).orElseThrow());
        model.addAttribute("restaurants", restaurantRepository.findAll());
        return "dishes/dish-form";
    }


    //@PostMapping("dishes") necesita el ModelAttribute para que extraiga los datos del formulario,
    //Con el dish.getId() lo estoy mandando a la pantalla del detalle de eso que se cargo
    @PostMapping("dishes")
    public String save(@ModelAttribute Dish dish){
        dishRepository.save(dish);
        return "redirect:/dishes/" + dish.getId();
    }

}



