package com.restaurante.restaurantetesting.controller;

import com.restaurante.restaurantetesting.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class ReviewController {
    private final ReviewRepository reviewRepository; //una vez se crea el repositorio no va a cambiar por eso el final

    @GetMapping("reviews") // Alomejor para un ADMIN si le sirve ver todas las reviews
    public String reviews(Model model){
        model.addAttribute("reviews", reviewRepository.findAll());
        return "";
    }

    @GetMapping("reviews/{id}")
    public String reviewDetail(Model model, @PathVariable Long id) {
        model.addAttribute("review", reviewRepository.findById(id));
        return"";
    }
    //Meter lo del formulario tenemos que aprender aun

    @GetMapping("reviews/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes){
        reviewRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Borrado exitosamente");
        return "redirect:";
    }

}
