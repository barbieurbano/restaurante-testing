package com.restaurante.restaurantetesting.repository;

import com.restaurante.restaurantetesting.model.Restaurant;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@DisplayName("Restaurante repositorio operaciones de BD")
class RestaurantRepositoryTest {
    @Autowired //Mecanismo de inyecccion de dependencias pondremos 1 por cada repositorio que necesitemos
    RestaurantRepository restaurantRepository;

    @Test
    void guardarRestaurante(){
        Restaurant restaurante = new Restaurant();
        restaurante.setName("Sabores Argentinos");
        restaurante.setAveragePrice(35.0);
        restaurante = restaurantRepository.save(restaurante);

        assertNotNull(restaurante.getId());
        assertEquals("Sabores Argentinos", restaurante.getName());
    }

    @Test
    void buscarRestaurantes_Vacio(){
        //Traer todos los restaurantes de la BD
        List<Restaurant> restaurantes = restaurantRepository.findAll();
        assertNotNull(restaurantes);
        System.out.println(restaurantes);

        assertTrue(restaurantes.size() == 0);
        assertTrue(restaurantes.isEmpty());
    }

    @Test
    void buscarRestaurantes_ConDatos(){
        Restaurant r1 = new Restaurant("La Ola", 20.5);
        Restaurant r2 = Restaurant.builder().averagePrice(35.0).name("Fuertecabra").active(false).build();

        List<Restaurant> restaurantes = List.of(r1, r2);
        restaurantRepository.saveAll(restaurantes);

        List<Restaurant> restaurantesFromDB = restaurantRepository.findAll();

        for(Restaurant restaurante : restaurantesFromDB){
            System.out.println(restaurante);
        }

        //Comprobamos que SI hay restaurantes
        assertNotNull(restaurantesFromDB);
        assertFalse(restaurantesFromDB.isEmpty());
        assertEquals(2, restaurantesFromDB.size());

        //Array[0]
        restaurantesFromDB.get(0);
        restaurantesFromDB.getFirst();

        assertEquals("La Ola", restaurantesFromDB.get(0).getName());
        assertEquals("La Ola", restaurantesFromDB.getFirst().getName());
        assertEquals("Fuertecabra", restaurantesFromDB.get(1).getName());
    }

    @Test
    void borrarRestaurantePorId(){
        Restaurant restaurante = new Restaurant("Fuertecabra", 65.0);
        restaurante = restaurantRepository.save(restaurante);

        Long id = restaurante.getId();

        restaurantRepository.delete(restaurante);
        assertEquals(0, restaurantRepository.count());
    }

    @Test
    @DisplayName("Borrar todos")
    void borrarRestaurantes(){
        Restaurant r1 = new Restaurant("La Ola", 55.6);
        Restaurant r2 = new Restaurant("Fuertecabra", 45.8);
        restaurantRepository.saveAll(List.of(r1, r2));
        assertEquals(2, restaurantRepository.count());

        restaurantRepository.deleteAll();

        assertEquals(0, restaurantRepository.count());
    }

    @Test
    @DisplayName("Buscar restaurante por id que NO EXISTE")
    void buscarRestaurantePorId_Vacio(){
        Long id = 1L;
        Optional<Restaurant> restaurante = restaurantRepository.findById(id);

        boolean existeRestaurante = restaurante.isPresent();
        assertFalse(existeRestaurante);
    }

    @Test
    @DisplayName("Buscar restaurante por id que SI EXISTE")
    void buscarRestaurantePorId_Existente(){
        //Crear y guardar UN restaurante en BD:
        var restaurante = new Restaurant("La Ola", 25.5);
        restaurante = restaurantRepository.save(restaurante);
        Long id = restaurante.getId();

        //Buscar el restaurante en BD por id --> clave primaria
        Optional<Restaurant> optional =  restaurantRepository.findById(id);

        //Comprobar que existe el restaurante buscando
        boolean existe = optional.isPresent();
        assertTrue(existe);

        //Obtener el restaurante del Optional y comprobar su nombre precio...
        Restaurant restaurantFromDB = optional.get();
        assertEquals("La Ola", restaurantFromDB.getName());
        assertEquals(25.5, restaurantFromDB.getAveragePrice());
    }

//El test sirve para comprobar que ese métdo responde bien en los dos casos: cuando no existe y cuando sí existe.
    @Test
    void existByIdTest(){
        //Buscar un restaurante que NO EXISTE,  La BD está vacía todavía. Preguntás "¿existe el id 1?" → devuelve false. Como esperabas false, assertFalse pasa. Estás probando el caso negativo.
        //Opcion 1: Con assert directamente
        assertFalse(restaurantRepository.existsById(1L));

        //Opcion 2: Lo guardo en una variable primero, por si lo quiero usar en más lugares de mi programa.
        //guardando el resultado en una variable primero. Tu profe lo puso como "opción 2" solo para mostrar el estilo: a veces te
        //conviene guardar el boolean en una variable por si lo querés reutilizar.
        boolean restauranteExiste = restaurantRepository.existsById(1L);
        assertFalse(restauranteExiste);

        //Buscar un restaurante que SI EXISTE, Creás un restaurante y lo guardás. A partir de acá sí existe algo en la BD. Ojo: save() devuelve el objeto ya con su id autogenerado
        //(por eso reasignás restaurante = ...).
        Restaurant restaurante = new Restaurant("Fuertecabra", 35.4);
        restaurante = restaurantRepository.save(restaurante);

        //Comprobamos que SI EXISTE el restaurante que acabamos de crear usando exists,  Acá preguntás por el id real del restaurante que acabás de guardar
        assertTrue(restaurantRepository.existsById(restaurante.getId()));

        //NO ES UN TEST,  Es solo un ejemplo para mostrar cómo usarías existsById dentro de la lógica de un programa real (decidir algo según
        //  si existe o no)
        if(restaurantRepository.existsById(restaurante.getId())){
            System.out.println("Buenas, ¿Que quieres comer hoy?");
        }
        else{
            System.out.println("Lo sentimos, está cerrado");
        }
    }
}