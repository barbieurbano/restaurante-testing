package com.restaurante.restaurantetesting.controller;

import com.restaurante.restaurantetesting.model.Dish;
import com.restaurante.restaurantetesting.model.Order;
import com.restaurante.restaurantetesting.model.OrderLine;
import com.restaurante.restaurantetesting.model.Restaurant;
import com.restaurante.restaurantetesting.model.enums.OrderStatus;
import com.restaurante.restaurantetesting.repository.DishRepository;
import com.restaurante.restaurantetesting.repository.OrderLineRepository;
import com.restaurante.restaurantetesting.repository.OrderRepository;
import com.restaurante.restaurantetesting.repository.RestaurantRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import com.restaurante.restaurantetesting.model.User;
import com.restaurante.restaurantetesting.model.enums.Role;
import com.restaurante.restaurantetesting.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class OrderControllerTest {
//Estos son test de integracion porque cargas todas las capas, sprint base de datos, controladores
//cuando tengamos servicios ahi es donde se hacen los test unitarios
    @Autowired MockMvc mockMvc;
    @Autowired RestaurantRepository restaurantRepo;
    @Autowired DishRepository dishRepo;
    @Autowired OrderRepository orderRepo;
    @Autowired OrderLineRepository orderLineRepo;
    @Autowired UserRepository userRepo;
    @Autowired PasswordEncoder passwordEncoder;

    Restaurant restaurant;
    Dish ensalada, lentejas, flan;
    Order order;
    User admin;
    OrderLine lineaEnsalada, lineaLentejas;

    @BeforeEach
    void setUp(){
        admin = userRepo.save(User.builder()
                .email("admin@gmail.com").username("admin")
                .password(passwordEncoder.encode("admin")).role(Role.ROLE_ADMIN).build());

        restaurant = restaurantRepo.save(Restaurant.builder().name("Restaurante 1").build());
        ensalada = dishRepo.save(Dish.builder().name("Ensalada").price(10d).restaurant(restaurant).build());
        lentejas = dishRepo.save(Dish.builder().name("Lentejas").price(15d).restaurant(restaurant).build());
        flan = dishRepo.save(Dish.builder().name("Flan").price(5d).restaurant(restaurant).build());
        order = orderRepo.save(Order.builder().restaurant(restaurant).status(OrderStatus.PENDING).numPeople(2).build());
        lineaEnsalada = orderLineRepo.save(OrderLine.builder().dish(ensalada).order(order).quantity(1).build());
        lineaLentejas = orderLineRepo.save(OrderLine.builder().dish(lentejas).order(order).quantity(1).build());

    }

    //Las operaciones CRUD si son obligatorios los test, list, detail y create
    //porque mockMvc te obligaba que si salta una excepcion te da un mensaje de esa exception
    @Test
    @DisplayName("GET / orders")
    void list() throws Exception {
        mockMvc.perform(get("/orders").with(user(admin))) //lanza una petición HTTP GET simulada a /orders. y luego  simula que la petición la hace admin (autenticado). Acá tiene que ser admin (tu entidad User), no un user("pepe") porque el método orders() usa @AuthenticationPrincipal User y llama user.getRole() / user.getId(). Si pasáramos un usuario genérico de Spring daría error de casteo.
                .andExpect(status().isOk()) //verifica que la respuesta HTTP sea 200 OK.
                .andExpect(view().name("orders/order-list")) //verifica que el controller devuelve esa vista (plantilla).
                .andExpect(model().attributeExists("orders")) //verifica que el controller metió el atributo orders en el modelo.
                .andExpect(model().attribute("orders", hasSize(greaterThanOrEqualTo(1)))); //verifica que esa lista tenga al menos 1 pedido (en setUp creamos uno, asíque como mínimo hay 1)
    }

    @Test
    @DisplayName("GET /orders/{id}")
    void detail() throws Exception{
        mockMvc.perform(get("/orders/" + order.getId()).with(user("pepe").roles("USER"))) // acá sí alcanza un usuario genérico, porque order() no usa @AuthenticationPrincipal; solo necesita estar autenticado para pasar la seguridad.
                .andExpect(status().isOk()) //verifica HTTP 200.
                .andExpect(view().name("orders/order-detail")) //verifica la vista del detalle.
                .andExpect(model().attributeExists("order")) //verifica que cargó el pedido.
                .andExpect(model().attributeExists("orderLines")) //verifica que cargó las líneas.
                .andExpect(model().attributeExists("dishes")) //verifica que cargó la carta de platos (esto es lo que te faltaba en el controller y agregamos).
                .andExpect(model().attribute("orderLines", hasSize(greaterThanOrEqualTo(2)))) //verifica que hay al menos 2 líneas (las que creamos en setUp: ensalada + lentejas)
                .andExpect(model().attribute("dishes", hasSize(greaterThanOrEqualTo(3)))); //verifica que el restaurante tiene al menos 3 platos (ensalada, lentejas,flan).
    }

    //Por que no va with(csrf())  El token CSRF solo se exige en peticiones que cambian estado: POST, PUT, DELETE, PATCH. Spring Security NO lo pide en peticiones de lectura como GET,
    //  HEAD, OPTIONS.
    //Verifica que se ha creado un orderline

    //simula un usuario autenticado para toda la petición
    @WithMockUser(username = "pepe", roles = {"USER"})
    @DisplayName("GET /orders/new?restaurantId={id}")
    @Test
    void newOrder() throws Exception{
        mockMvc.perform(
                        get("/orders/new")
                                .param("restaurantId", restaurant.getId().toString())
                ).andExpect(status().isOk())
                .andExpect(view().name("orders/order-form")) //verifica que devuelve el formulario.
                .andExpect(model().attributeExists("order")) // verifica que metió en el modelo el order vacío (el formulario lo necesita por el th:object="${order}")
                .andExpect(model().attribute("order", hasProperty("restaurant", hasProperty("id", is(restaurant.getId())))));

        //Este ultimo verifica que ese order tiene asociado el restaurante correcto: navega order.restaurant.id y comprueba que es igual al id del restaurante que mandamos. Esto prueba que el controller hizo
        //order.setRestaurant(restaurant).

    }

    //Cuando creas en este necesitas pasarle como param los numero de comensales, sugerencias. viaja en el cuerpo de la peticion
    @WithMockUser(username = "pepe", roles = {"USER"})
    @DisplayName("POST /orders")
    @Test
    void createOrder() throws Exception{
        mockMvc.perform(
                        post("/orders").with(csrf()) //agrega el token CSRF; sin él, Spring Security rechazaría el POST con 403
                                .param("tableNumber", "1") //simula los campos del formulario que viajan en el cuerpo. Spring los mapea al @ModelAttribute Order.
                                .param("numPeople", "2")
                                .param("userSuggestions", "Alergia a todo")
                                .param("restaurant", restaurant.getId().toString())
                ).andExpect(status().is3xxRedirection()) //verifica que la respuesta es una redirección (3xx). El controller devuelve redirect:/orders/{id}.
                .andExpect(redirectedUrlPattern("/orders/*")); //verifica que redirige a una URL del tipo /orders/algo (el * es comodín, porque el id se genera alguardar y no lo sabemos de antemano).

        // Después de la petición vienen las verificaciones en la base de datos (patrón: acción → comprobar el estado después):
        Order creado = orderRepo.findAll().getLast(); // recupera el último pedido guardado (el que acabamos de crear).
        assertEquals(1,  creado.getTableNumber()); //verifica que se guardó la mesa = 1.
        assertEquals(2,  creado.getNumPeople()); //verifica que se guardaron 2 comensales.
        assertEquals("Alergia a todo",  creado.getUserSuggestions()); //verifica que se guardaron las observaciones.
        assertEquals(restaurant.getId(),  creado.getRestaurant().getId()); //verifica que quedó asociado al restaurante correcto.
    }

    @WithMockUser(username = "pepe", roles = {"USER"})
    @Test
    @DisplayName("POST /orders/{orderId}/lines?dishId={id}")
    void createLine()throws Exception{
        // cuenta cuántas líneas hay antes (patrón "contar antes → acción → comprobar después"). En setUp
        //  hay 2 (ensalada + lentejas).
        long countLines = orderLineRepo.count();

        mockMvc.perform(
                        post("/orders/" + order.getId() + "/lines").with(csrf()) // acción: POST para agregar una línea al pedido. Con csrf() porque es un POST
                                .param("dishId", flan.getId().toString()) // manda el plato a agregar (flan) como parámetro dishId (es el @RequestParam del controller).
                ).andExpect(status().is3xxRedirection()) //verifica que responde con redirección.
                .andExpect(redirectedUrlPattern("/orders/*")); //verifica que redirige a /orders/algo

        assertEquals(countLines + 1, orderLineRepo.count()); //verifica que ahora hay una línea más (de 2 a 3). Prueba que la línea del flan se creó.

        Order recargado = orderRepo.findById(order.getId()).orElseThrow(); //preparación: recarga el pedido desde la BD para ver su estado actualizado.
        assertEquals(OrderStatus.IN_PROGRESS, recargado.getStatus()); //verifica que el pedido pasó de PENDING a IN_PROGRESS (porque ya tiene platos).
        assertEquals(30d, recargado.getTotalPrice()); //verifica el total recalculado: ensalada 10 + lentejas 15 + flan 5 = 30.

        OrderLine lineaFlanRecargada = orderLineRepo.findAll().getLast(); //preparación: agarra la última línea guardada (la del flan).
        assertEquals(1, lineaFlanRecargada.getQuantity()); //verifica que la línea nueva tiene cantidad 1.
        assertEquals(flan.getId(), lineaFlanRecargada.getDish().getId()); //verifica que esa línea apunta al flan.
        assertEquals(order.getId(), lineaFlanRecargada.getOrder().getId()); //verifica que esa línea pertenece al pedido correcto.
    }


    @Test
    @DisplayName("GET /orders/{orderId}/lines/{lineId}/delete")
    void deleteLine() throws Exception{

    }

    @Test
    @DisplayName("POST /orders/{orderId}/lines/{lineId}/delete")
    void updateLine(){

    }

    @WithMockUser(username = "pepe", roles = {"USER"})
    @Test
    @DisplayName("GET /orders/{orderId}/finish?tip=0")
    void finishOrder()  throws Exception{
        mockMvc.perform(
                        get("/orders/" + order.getId() + "/finish") //acción: GET para finalizar. Es GET (no POST), así que sin csrf
                                .param("tip", "1.34") //preparación: manda la propina como parámetro.
                ).andExpect(status().is3xxRedirection()) //verifica la redirección.
                .andExpect(redirectedUrl("/orders/" + order.getId())); //verifica la URL exacta de redirección (acá usamos redirectedUrl, no ...Pattern, porque sí sabemos el id: es el del pedido que ya existía)

        Order finalizado = orderRepo.findById(order.getId()).orElseThrow(); //preparación: recarga el pedido.
        assertEquals(OrderStatus.FINISHED, finalizado.getStatus()); //verifica que quedó en FINISHED.
        assertEquals(1.34d, finalizado.getTip()); //verifica que se guardó la propina 1.34.
        assertEquals(25d, finalizado.getTotalPrice()); //verifica el total: ensalada 10 + lentejas 15 = 25 (en este test no agregamos flan; cada test es @Transactional y arranca limpio).
    }
}
