package com.restaurante.restaurantetesting.controller;

import com.restaurante.restaurantetesting.model.Dish;
import com.restaurante.restaurantetesting.model.Restaurant;
import com.restaurante.restaurantetesting.model.enums.DishType;
import com.restaurante.restaurantetesting.repository.DishRepository;
import com.restaurante.restaurantetesting.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Para que borre los datos al
public class DishControllerTest {

    @Autowired MockMvc mockMvc;
    @Autowired DishRepository dishRepo;
    @Autowired RestaurantRepository restaurantRepo;

    Restaurant restaurant;
    Restaurant restaurant2;
    Dish dish;

    @BeforeEach
    void setUp(){
        restaurant = restaurantRepo.save(Restaurant.builder().name("Restaurante 1").build());
        restaurant2 = restaurantRepo.save(Restaurant.builder().name("Restaurante 2").build());
        dish =dishRepo.save(Dish.builder().name("Plato 1").restaurant(restaurant).build());
    }

    //Deberia probar que carga todos los platos
    @Test
    void list(){
        Assertions.fail("Pendiente test detail");
    }


    @Test
    void detail() throws Exception{
        Assertions.fail("Pendiente test detail");
    }
//Se esta simulando que se esta rellenando un formulario entero.
//simula que un admin envía el formulario de crear un plato (POST /dishes), y verifica que (1) redirige y (2) el plato quedó guardado en la BD con
//  sus datos.
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    void createDish() throws Exception {
        //Primero un count de cuantos platos hay antes, luego con el mockmvc performan post y luego un count + 1
        long antes = dishRepo.count();

        mockMvc.perform(post("/dishes").with(csrf()) // POST + token CSRF (seguridad)
                        .param("name", "Plato test")
                        .param("description", "Plato description test")
                        .param("type", DishType.DESSERT.toString())
                        .param("restaurant", restaurant.getId().toString())
        ).andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("/dishes/*")); // esperamos una redirección a /dishes (algo)

        assertEquals(antes + 1, dishRepo.count()); // Ahora verificamos que hay exactamente UNO MAS.

        Dish creado = dishRepo.findAll().getLast(); //trae el último plato de la BD (el recién creado)

        // Aqui erificamos que cada dato que mandaste en el formulario se guardó bien: el nombre, el precio (10d = el double 10.0), la descripción y el tipo (el enum
        //  DESSERT). Si el controlador guardara mal algún campo, el assert correspondiente falla y te dice exactamente cuál.
        assertEquals("Plato test", dish.getName());
        assertEquals(10d, creado.getPrice());
        assertEquals("Plato description test", creado.getDescription());

        //Verifica que el plato quedó asociado al restaurante correcto (el que mandaste por param("restaurant", ...)). Comparás por id porque es lo que identifica
        //de forma única al restaurante.
        assertEquals(DishType.DESSERT, creado.getType());
        assertEquals(restaurant.getId(), creado.getRestaurant().getId());

        //Aqui falta los type
        //Faltan los an uxpecte
        //Restaurant
        //Assertions.fail("Pendiente test crear nuevo Plato");
    }

//Seria ideal tener dos platos para verifica que edita, QUE HACE:  crea un plato en setUp(), lo edita mandando el mismo id con datos nuevos, y verifica que no se creó otro (la cantidad no cambia) y que los datos
//quedaron actualizados.
    @WithMockUser(username = "admin", roles = "ADMIN")
    @Test
    void editDish() throws Exception {
        //En este podemo crear algo en la bd y luego traer de bd y cambiarle el restaurante por ejemplo
        //MOCKMVC perform post cambniando nombre proecio restaurante apuntando al mismo id

        Long dishId = dish.getId(); // el id del plato creado en setUp()

        //IMPORTANTE En editar, la cantidad de platos tiene que quedar IGUAL (no +1). Esto prueba que el controlador MODIFICO el
        //plato existente en vez de crear uno nuevo. Si por error creara otro, este assert fallaría.

        long countBefore = dishRepo.count();

        mockMvc.perform(post("/dishes").with(csrf())
                    .param("id", dishId.toString()) // mismo id => editar, no crear
                    .param("name", "plato description test editado")
                    .param("type",DishType.MAIN_COURSE.toString())
                    .param("restaurant", restaurant2.getId().toString())
        ).andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/dishes/" + dishId));

        assertEquals(countBefore, dishRepo.count());   // MISMA cantidad (no creó otro)

        //Vuelve a traer de la BD el plato por su id (el mismo que editaste). Lo recargás desde la base para confirmar que los cambios se guardaron de verdad, no
        //   en memoria.
        Dish editado = dishRepo.findById(dishId).orElseThrow();

        assertEquals("Plato name test editado", editado.getName());
        assertEquals(9d, editado.getPrice());
        assertEquals("Plato description test editado", editado.getDescription());
        assertEquals(DishType.MAIN_COURSE, editado.getType()); //HASTA ACA, Verifican que los datos se actualizaron a los nuevos valores que enviaste (nombre nuevo, precio 9, descripción nueva, tipo MAIN_COURSE)
        assertEquals(restaurant2.getId(), editado.getRestaurant().getId()); //Verifica que el plato cambió de restaurante: en setUp() estaba en restaurant, y al editar lo mandaste a restaurant2. Confirma que esa reasignación
        //funcionó.

    }

}
