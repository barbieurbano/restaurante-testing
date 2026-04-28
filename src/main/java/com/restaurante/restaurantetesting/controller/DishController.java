package com.restaurante.restaurantetesting.controller;

import com.restaurante.restaurantetesting.model.Review;
import com.restaurante.restaurantetesting.repository.DishRepository;
import com.restaurante.restaurantetesting.repository.ReviewRepository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@AllArgsConstructor
public class DishController {
    private final DishRepository dishRepository;
    private final ReviewRepository reviewRepository;//una vez se crea el repositorio no va a cambiar por eso el final

    @GetMapping("dish/{id}") // Alomejor para un ADMIN si le sirve ver todas las reviews
    public String dishDetail(Model model, @PathVariable  Long id){
        model.addAttribute("dish", dishRepository.findById(id).orElseThrow());
        model.addAttribute("reviews", reviewRepository.findByDish_IdOrderByCreationDateDesc(id));
        return "";
    }
}



