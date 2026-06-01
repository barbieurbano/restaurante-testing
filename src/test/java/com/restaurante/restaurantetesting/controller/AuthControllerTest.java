package com.restaurante.restaurantetesting.controller;

import com.restaurante.restaurantetesting.dto.RegisterForm;
import com.restaurante.restaurantetesting.model.User;
import com.restaurante.restaurantetesting.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;


//carga solo la capa web de ese controller, sin BD ni contexto completo. Es más rápido
@WebMvcTest(controllers = AuthController.class) // carga solo este controller (rápido)

//desactiva los filtros de Security. Por eso acá no hace falta .with(csrf()) en el POST: no hay protección CSRF activa.
@AutoConfigureMockMvc(addFilters = false) // sin activar Security
public class AuthControllerTest {
    //reemplaza el UserService real por un mock (un doble de prueba). Así el test no toca la base de datos; controlamos qué devuelve.
    @MockitoBean
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @Test
    public void loginTest() throws Exception {
        mockMvc.perform(get("/login")) //acción: GET a /login.
                .andExpect(status().isOk()) //verifica HTTp 200.
                .andExpect(view().name("auth/login")); //verifica que devuelve la vista auth/login.
        verifyNoInteractions(userService); //verifica (Mockito) que el controller no llamó al userService en ningún momento (mostrar el login no debe tocar el servicio).
    }

    @Test
    public void registerTest() throws Exception {
        //preparación del mock: "cuando se llame a register() con cualquier RegisterForm, devolvé este usuario falso". No se guarda nada real.
        when(userService.register(any(RegisterForm.class))).thenReturn(User.builder().username("elpepe").build());

        mockMvc.perform(post("/register") //acción: POST con los 4 campos del formulario.
                        .param("username", "elpepe")
                        .param("email", "elpepe@gmail.com")
                        .param("password", "user")
                        .param("passwordConfirm", "user")
                )
                .andExpect(status().is3xxRedirection()) //verifica que responde con redirección.
                .andExpect(redirectedUrl("/login")) //verifica que redirige exactamente a /login.
                .andExpect(flash().attribute("message", "Cuenta creada correctamente, inicia sesión."));
        //verifica que dejó ese mensaje flash (el que mostramos tras registrarse). Por eso tuvimos que igualar el texto en el controller.

        //verifica (Mockito) que el controller sí llamó a userService.register(...) exactamente una vez.
        verify(userService).register(any(RegisterForm.class));
    }

}
