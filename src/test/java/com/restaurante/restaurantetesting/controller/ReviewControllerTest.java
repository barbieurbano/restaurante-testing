package com.restaurante.restaurantetesting.controller;

import com.restaurante.restaurantetesting.model.Review;
import com.restaurante.restaurantetesting.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest//Activa Sping
@AutoConfigureMockMvc // Activa MockMvc para testing de controller
@Transactional
public class ReviewControllerTest {

    @Autowired
    ReviewRepository reviewRepository;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        review1 = reviewRepository.save(Review.builder().title("Restaurante muy acogedor").rating(5).build());
    }

    @Test
    void deleteReview() throws Exception{
        Long id = review1.getId();
        assertTrue(reviewRepository.existsById(id));

        mockMvc.perform(get("/reviews/delete/" + id))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/reviews"))
                //Con el model se perderia, se supone que con el flush te conserva
                .andExpect(flash().attribute("message", "Borrado exitosamente"));

        assertFalse(reviewRepository.existsById(id));
    }

}
