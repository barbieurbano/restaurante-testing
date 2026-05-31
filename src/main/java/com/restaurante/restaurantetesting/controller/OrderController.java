package com.restaurante.restaurantetesting.controller;

import com.restaurante.restaurantetesting.model.Dish;
import com.restaurante.restaurantetesting.model.Order;
import com.restaurante.restaurantetesting.model.OrderLine;
import com.restaurante.restaurantetesting.model.Restaurant;
import com.restaurante.restaurantetesting.model.User;
import com.restaurante.restaurantetesting.model.enums.OrderStatus;
import com.restaurante.restaurantetesting.model.enums.Role;
import com.restaurante.restaurantetesting.repository.DishRepository;
import com.restaurante.restaurantetesting.repository.OrderLineRepository;
import com.restaurante.restaurantetesting.repository.OrderRepository;
import com.restaurante.restaurantetesting.repository.RestaurantRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

//Order de prioridad: TRACE > DEBUG > INFO > WARN > ERROR (estos 2 ultimos te dicen en produccion si algo va bien o mal)
@Slf4j//Proporciona un objeto log en esta clase, para dar inf extra, en un try catch... y demas
@Controller
@AllArgsConstructor
public class OrderController {

    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final RestaurantRepository restaurantRepository;
    private final DishRepository dishRepository;

    //decide qué pedidos mostrar según el rol. Si es admin, ve todos (findAll()). Si es un cliente normal, ve solo los suyos (findByUser_Id)
    @GetMapping("orders") // CONTROLADOR
    public String orders(Model model, @AuthenticationPrincipal User user) {
        log.info("user: {} request to order-list", user.getId());
        if (user.getRole().equals(Role.ROLE_ADMIN))
            model.addAttribute("orders", orderRepository.findAll());
        else
            model.addAttribute("orders", orderRepository.findByUser_Id(user.getId()));
        return "orders/order-list";
    }


    // aqui trae los platos
    // carga el pedido, sus líneas, y además la carta del restaurante (dishes) ordenada por precio, para que en el detalle puedas elegir platos y
    //agregarlos. Lo que te faltaba era cargar dishes.
    @GetMapping("orders/{id}")
    public String order(Model model, @PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow();
        model.addAttribute("order", order);
        // asociaciones:
        model.addAttribute("orderLines", orderLineRepository.findByOrder_Id(id));

        // cargar platos del restaurante para que el usuario pueda elegirlos
        List<Dish> dishes = dishRepository.findByRestaurantIdOrderByPrice(order.getRestaurant().getId());
        model.addAttribute("dishes", dishes);
        return "orders/order-detail";
    }

    /*
    Recibir el pedido y guardarlo en BD, se genera clave primaria porque estas guardando por primera vez el pedido
    Este te lleva al detalle del pedido, recibe un modelAtribute y recibes un objeto de algo en este caso un pedido
    Se crea un pedido que esta pendiente de ser atentido epro ya se ha creado

     Busca el restaurante por el restaurantId que llega en la URL (orders/new?restaurantId=5), crea un Order vacío, le asocia ese restaurante y lo
  manda al formulario. Tu versión devolvía la vista sin crear ni pasar el order al modelo (el formulario lo necesita).

    * */

    // Es para navegar al formulario, para enviar el formulario es el post
    @GetMapping("orders/new")
    public String newOrder(Model model, @RequestParam() Long restaurantId) {
        Restaurant restaurant = restaurantRepository.findById(restaurantId).orElseThrow();
        Order order = new Order();
        order.setRestaurant(restaurant);
        model.addAttribute("order", order);
        return "orders/order-form";
    }

    /*
     //Agregar un plato al pedido, seria otro postmapping que en vez de crear un pedido crea una linea de pedido
    //Damos por hecho que ya se creo el pedido porque no puedes agregar lineas a un pedido que no existe
    //Lines permite relacionar un plato con un pedido???

    ATENCION: recibe el pedido del formulario (@ModelAttribute), le pone estado PENDING, la fecha de ahora, total en 0 (todavía no tiene platos) y lo asocia
    al usuario logueado. Lo guarda y redirige a su detalle.
    */

    @PostMapping("orders")
    public String createOrder(@ModelAttribute Order order, @AuthenticationPrincipal User user){
        order.setStatus(OrderStatus.PENDING);
        order.setDate(LocalDateTime.now());
        order.setTotalPrice(0d);
        order.setUser(user);
        orderRepository.save(order);
        return "redirect:/orders/" + order.getId();
    }

    /*
        Agregar un plato al pedido — POST, el más importante

        Qué hace, paso a paso:
          1. Busca el pedido y el plato.
          2. findByOrder_IdAndDish_Id → mira si ese plato ya está en el pedido.
            - Si ya existe esa línea → le suma 1 a la cantidad (no duplica el plato).
            - Si no existe → crea una línea nueva con cantidad 1.
          3. Si el pedido estaba en PENDING, lo pasa a IN_PROGRESS (porque ya tiene al menos un plato).
          4. Recalcula el total con calculateTotalPrice y guarda.
    */

    @PostMapping("orders/{id}/lines")
    public String createLine(@PathVariable Long id,
                             @RequestParam Long dishId) {
        Order order = orderRepository.findById(id).orElseThrow();
        Dish dish = dishRepository.findById(dishId).orElseThrow();

        Optional<OrderLine> lineOptional = orderLineRepository.findByOrder_IdAndDish_Id(id, dishId);

        OrderLine orderLine;
        if (lineOptional.isPresent()) {
            orderLine = lineOptional.get();
            orderLine.setQuantity(orderLine.getQuantity() + 1);
        } else {
            orderLine = new OrderLine();
            orderLine.setDish(dish);
            orderLine.setOrder(order);
            orderLine.setQuantity(1);
        }
        orderLineRepository.save(orderLine);

        if (order.getStatus() == OrderStatus.PENDING)
            order.setStatus(OrderStatus.IN_PROGRESS);

        Double totalPrice = orderLineRepository.calculateTotalPrice(order.getId());
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        return "redirect:/orders/" + order.getId();
    }
    /*
         QUITA UN PLATO DEL PEDIDP, Borra la línea por su lineId y recalcula el total del pedido (porque sacar un plato cambia el precio).
    */

    //Una vez agregas un plato a un pedido puedes querer quitarlo al PLATO DEL PERDIDO
    @GetMapping("orders/{orderId}/lines/{lineId}/delete")
    public String deleteLine(@PathVariable Long orderId,
                             @PathVariable Long lineId){
        orderLineRepository.deleteById(lineId);
        Order order = orderRepository.findById(orderId).orElseThrow();
        order.setTotalPrice(orderLineRepository.calculateTotalPrice(order.getId()));
        orderRepository.save(order);
        return "redirect:/orders/" + orderId;
    }

    /*
         cambia la cantidad de (un plato), una línea (solo si quantity >= 1, para no dejar cantidades 0 o negativas) y recalcula el total.
    */

    //Si quieres cambiarle la cantidad necesitamos uun postmapping pasandole un parametro quantity
    //Para poder actualizar la linea de pedido, la cantidad de 1 plato
    @PostMapping("orders/{orderId}/lines/{lineId}")
    public String updateLine(@PathVariable Long orderId,
                             @PathVariable Long lineId,
                             @RequestParam Integer quantity){
        if (quantity >= 1) {
            OrderLine orderLine = orderLineRepository.findById(lineId).orElseThrow();
            orderLine.setQuantity(quantity);
            orderLineRepository.save(orderLine);

            Order order = orderRepository.findById(orderId).orElseThrow();
            order.setTotalPrice(orderLineRepository.calculateTotalPrice(order.getId()));
            orderRepository.save(order);
        }
        return "redirect:/orders/" + orderId;
    }

    /*
         marca el pedido como FINISHED, recalcula el total y guarda la propina (tip) si vino. Ojo con @RequestParam(required = false): el profe lo pone
        opcional, así no explota si finalizás sin propina
    */

    //Para finalizar un pedido, no te deja modificar nada del pedido puede ser con un GET o POST
    //Cambia el estatus a finish
    @GetMapping("orders/{id}/finish")
    public String finishOrder(@PathVariable Long id,
                              @RequestParam(required = false) Double tip){
        Order order = orderRepository.findById(id).orElseThrow();
        order.setStatus(OrderStatus.FINISHED);
        order.setTotalPrice(orderLineRepository.calculateTotalPrice(order.getId()));
        if (tip != null && tip > 0) {
            order.setTip(tip);
        } else {
            order.setTip(0d);
        }
        orderRepository.save(order);
        return "redirect:/orders/" + id;
    }




}
