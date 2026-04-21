package com.restaurante.restaurantetesting.controller;

import com.restaurante.restaurantetesting.model.Restaurant;
import com.restaurante.restaurantetesting.repository.RestaurantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest // Activa spring para el testing, Esto suele ser lento
@AutoConfigureMockMvc //Para poder usar el objeto mockMvc(clase de utilidad), lo habilita y configura
    //@Transactional Descarta los cambios introducidos para que el siguiente TEST comience limpio
class RestaurantControllerTest {
    //restaurant repository, si no nos daria un nullExpointerExcepction
    @Autowired
    RestaurantRepository restaurantRepository; // Para crear datos DEMO
    //Utilidad de TESTING para lanzar peticiones a controladores en test.
    @Autowired
    MockMvc mockMvc; // hay que hacerle una autowired porque hay que inyectarlo, lanza peticiones al controlador

    @BeforeEach
    void setUp() {
        //Crear datos DEMO con el setUp que ya hemos visto, tenemos que comentar los datos que pusimos en MAIN
        restaurantRepository.deleteAll();
        restaurantRepository.saveAll(List.of(
                Restaurant.builder().name("Los pinxitos").averagePrice(32.5).build(),
                Restaurant.builder().name("Sushi Bialet Masse").averagePrice(20.4).build(),
                Restaurant.builder().name("Don Pepe").averagePrice(15.6).build(),
                Restaurant.builder().name("Los tres mosqueteros").averagePrice(13.8).build()
        ));

    }

    @Test
    void restaurantsFull() throws Exception {
        //MOCK es el que te deja hacer peticiones HTTP y testearlas
        //Invocar endpoint http://localhost:8080/restaurantes
        //Este metodo te deja lanzar una peticion, indicarle a donde queremos que apunte la peticion
        //La peticion --> localhost:8080/restaurantes
        //Perform ejecuta una peticion y devuelve un tipo que permite encadenar acciones seguidas como hacer asserciones(de que esperar una respuesta), suelen ser get(es el unico que hemos visto),put, post
        //Patron Factory
        mockMvc.perform(get("/restaurantes"))
                .andExpect(status().isOk())
                .andExpect(view().name("restaurant-list"))
                .andExpect(model().attributeExists("restaurantes"))
                .andExpect(model().attribute("restaurantes", hasSize(4)));

    }

    @Test
    void restaurantsEmpty(){
        //mockMvc.perform();
    }
}