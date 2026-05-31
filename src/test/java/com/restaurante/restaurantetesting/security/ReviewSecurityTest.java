package com.restaurante.restaurantetesting.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.security.test.context.support.WithMockUser;

import com.restaurante.restaurantetesting.repository.ReviewRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest // Activa Spring
@AutoConfigureMockMvc // Activa MockMvc para testing de controller
@Transactional
public class ReviewSecurityTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    //este solamente testea siendo anonino, como anonimo puedes llamar al listado de review, esto esta bien en el segurity config, pone permitAll()
    //Debe dar un 200
    void list_anonymous_200() throws Exception{
        mockMvc.perform(get("/reviews"))
                .andExpect(status().isOk());
    }
    @Test
    //Si es una apirest deberia ser un 401, ahora con mvc es una redireccion al login
    //
    void list_anonymous_401() throws Exception{
        mockMvc.perform(get("/reviews/new"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
        //Si es una apirest deberia ser un 401, ahora con mvc es una redireccion al login
        //
    void delete_anonymous_401() throws Exception{
        mockMvc.perform(get("/reviews/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
    //Ver detail deberia ser un 200, acceder a un detalle de reviews podriamos hacer un mock
    //post de intentar siendo anonimo poner una review, no te debe de dejar
    //
    //user normal me debe dejar ver y enviar reviews, si intentas hacer un post que puedas ver la pantalla de y que puedes enviar el formulario siendo un usuario.
    //Entro a un formulario con un usuario, como le digo que sot el usuario 1, pasandoselo en el get o configurar
    //con withUserDetails("user0")
    @Test
    void new_user_200() throws Exception{
        mockMvc.perform(
                get("/reviews/new").with(user("pepe").roles("USER"))
        )       .andExpect(status().isOk());
    }

    @WithMockUser(username = "pepe", roles = {"USER"})
    @Test
    void new_user_200_con_anotacion () throws Exception {
        mockMvc.perform(
                get("/reviews/new")
        ).andExpect(status().isOk()); //con un usuario USER logueado, la pantalla del formulario de nueva reseña se puede ver (200).
    }
    @Test
    @DisplayName("POST / reviews siendo ANONUMOUS")
    void post_user_401() throws Exception {
        long  countBefore = reviewRepository.count();
        mockMvc.perform(post("/reviews")
                .param("title", "OK")
                .param("rating", "5")
        ).andExpect(status().isForbidden()); //la seguridad bloquea el POST anónimo con 403 (no lo deja pasar).

        assertEquals(countBefore, reviewRepository.count()); //la cantidad de reseñas no cambió, confirmando que el intento anónimo no guardó nada.
    }


}
