package com.restaurante.restaurantetesting.controller;

import com.restaurante.restaurantetesting.model.Dish;
import com.restaurante.restaurantetesting.model.Restaurant;
import com.restaurante.restaurantetesting.model.Review;
import com.restaurante.restaurantetesting.repository.DishRepository;
import com.restaurante.restaurantetesting.repository.RestaurantRepository;
import com.restaurante.restaurantetesting.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest//Activa Sping
@AutoConfigureMockMvc // Activa MockMvc para testing de controller
@Transactional
public class ReviewControllerTest {

    @Autowired ReviewRepository reviewRepository;
    @Autowired MockMvc mockMvc;
    @Autowired DishRepository dishRepo;

    Restaurant restaurant;
    Dish dish;
    Review review1;
    @Autowired
    private RestaurantRepository restaurantRepo;

    @BeforeEach
    void setUp(){
        review1 = reviewRepository.save(Review.builder().title("Restaurante muy acogedor").rating(5).build());
        dish = dishRepo.save(Dish.builder().name("Plato 1").price(10d).build());
        restaurant = restaurantRepo.save(Restaurant.builder().build());
    }

    @WithMockUser(username = "user", roles = "USER")   // ← (2) usuario logueado
    @Test
    void createReviewDish() throws Exception  {
        long countBefore = reviewRepository.count(); //Guarda cuántas reseñas hay antes del POST, para después comparar.
        mockMvc.perform(post("/reviews").with(csrf()) // ← (1) token CSRF
                .param("title", "OK")
                .param("rating", "5")
                .param("content", "OKOK")
                .param("dish",dish.getId().toString()))
                .andExpect(status().is3xxRedirection());

//El andExpect Comprueba que la respuesta sea una redirección 3xx. Esto confirma
//  que el controlador, después de guardar, hizo return "redirect:..." (no devolvió un error ni una página). Es la prueba de que el POST se procesó bien.

        assertEquals(countBefore + 1, reviewRepository.count()); //Verifica que ahora hay exactamente una reseña más que antes. Es la prueba de que el POST realmente guardó una reseña nueva en la BD

        Review reviewDB = reviewRepository.findAll().getLast(); //Trae la última reseña guardada (la que se acaba de crear) para poder inspeccionar sus datos.

        //Verifican que cada dato que mandaste en el formulario se guardó correctamente
        //Si el controlador guardara mal algún campo, el assert correspondiente te diría exactamente cuál.
        assertEquals("OK", reviewDB.getTitle());
        assertEquals(5, reviewDB.getRating());
        assertEquals("OKOK", reviewDB.getContent());

    }


    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void deleteReview() throws Exception {
        Long id = review1.getId();
        assertTrue(reviewRepository.existsById(id));

        mockMvc.perform(get("/reviews/delete/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews"))
                //Con el model se perderia, se supone que con el flush te conserva
                .andExpect(flash().attribute("message", "Borrado exitosamente"));

        assertFalse(reviewRepository.existsById(id));
    }

    //En el caso de movie n
    @Test
    void createReviewRestaurants() throws Exception {

    }


}
