package com.restaurante.restaurantetesting.controller;

import com.restaurante.restaurantetesting.model.Review;
import com.restaurante.restaurantetesting.repository.DishRepository;
import com.restaurante.restaurantetesting.repository.RestaurantRepository;
import com.restaurante.restaurantetesting.repository.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@AllArgsConstructor
public class ReviewController {
    private final ReviewRepository reviewRepository; //una vez se crea el repositorio no va a cambiar por eso el final
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

//mete todas las reseñas en el modelo y muestra una página con la lista.
    @GetMapping("reviews") // Alomejor para un ADMIN si le sirve ver todas las reviews
    public String reviews(Model model){
        model.addAttribute("reviews", reviewRepository.findAll());
        return "reviews/review-list";
    }
//busca una reseña por su id y la muestra.
    /*
      - @PathVariable Long id → toma el {id} de la URL (ej: /reviews/5 → id=5).
  - findById(id) devuelve un Optional<Review> (una "caja" que puede estar vacía). .orElseThrow() saca la reseña de la caja, o lanza error 404 si no existe.
  Así no le pasás la "caja" a la vista, sino la reseña real.
    */

    @GetMapping("reviews/{id}")
    public String reviewDetail(Model model, @PathVariable Long id) {
        model.addAttribute("review", reviewRepository.findById(id).orElseThrow());
        return"reviews/review-detail";
    }
    //Meter lo del formulario tenemos que aprender aun
//borra la reseña y vuelve a la lista mostrando un mensaje.
    /*
      - redirect:/reviews le dice al navegador "andá a /reviews" (que dispara el método reviews() del Paso 2).
  - RedirectAttributes + addFlashAttribute → guarda el mensaje solo para la siguiente página (un "flash"). Por eso en el test se verifica con
  flash().attribute("message", ...).
    */
    @GetMapping("reviews/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes){
        reviewRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Borrado exitosamente");
        return "redirect:/reviews";
    }
//(mostrar el formulario), Según le pases ?restaurantId=1 o ?dishId=1, prepara una reseña vacía ya "apuntada" a ese restaurante o plato, y
//  muestra el formulario
    //Recibimos dos requestparam y en base al que reciba hare uno o hare otro

    /*
  - @RequestParam(required = false) Long restaurantId → lee el ?restaurantId=... de la URL; required=false significa que puede no venir.
  - Por eso hay dos if: si vino restaurante lo asigna, si vino plato lo asigna.
    */
    @GetMapping("reviews/new")
    public String newReview (
            Model model,
            @RequestParam(required = false) Long restaurantId,
            @RequestParam(required = false) Long dishId
    )
    { Review review = new Review();
        //Asignar el resturante o plato
        if(restaurantId != null)
            review.setRestaurant(restaurantRepository.findById(restaurantId).orElseThrow());
        if(dishId != null)
            review.setDish(dishRepository.findById(dishId).orElseThrow());
        model.addAttribute("review", review);
        return "reviews/review-form";
    }
    //Cuando se envía el formulario), guarda la reseña en la BD y redirige.
    //  - @ModelAttribute Review review → Spring arma automáticamente un objeto Review con los param del formulario (title, rating, content, dish...).
    //Podemos introductir logica para que el usuario no pueda hacer otra review si ya tiene hecha una por ejemplo.
    @PostMapping("reviews")
    public String createReview(@ModelAttribute Review review) {
        reviewRepository.save(review);
        if (review.getRestaurant() != null)
            return "redirect:/restaurants/" + review.getRestaurant().getId();
        if (review.getDish() != null)
            return "redirect:/dishes/" + review.getDish().getId();
        return "redirect:/reviews";
    }

}
