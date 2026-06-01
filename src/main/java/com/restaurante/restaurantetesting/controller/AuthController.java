package com.restaurante.restaurantetesting.controller;

import com.restaurante.restaurantetesting.dto.RegisterForm;
import com.restaurante.restaurantetesting.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import com.restaurante.restaurantetesting.model.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


// controlador para iniciar sesion y/ registrarse crear User en db
@Slf4j
@Controller
@AllArgsConstructor
public class AuthController {

    private final UserService userService;

    // GETMapping register
    // navegar a formulario de registro
    @GetMapping("register")
    public String register(Model model) {
        model.addAttribute("user", new RegisterForm());
        return "auth/register";
    }

    // PostMapping register
    @PostMapping("register")
    public String register(@ModelAttribute RegisterForm form, RedirectAttributes redirectAttributes) {
        try {
            //(capturar el retorno): tu register() del servicio devuelve el User creado (ya con su id generado por la BD).
            // Vos hoy lo ignorás (userService.register(form); a secas). Capturarlo en la variable user te permite usarlo después — en este caso, para loguear su id.
            User user = userService.register(form);  // (2) captura el User
            redirectAttributes.addFlashAttribute("message", "Cuenta creada correctamente, inicia sesión.");
            // escribe un mensaje de nivel INFO en la consola/log. Sirve en producción para dejar
            //  rastro de que se registró un usuario. El {} es un placeholder que se reemplaza por user.getId() (por eso necesitás haberlo capturado en el paso 2).
            log.info("Nuevo usuario registrado con id {}", user.getId());  // (3) log INFO
            return "redirect:/login";
        } catch (Exception e) {
            //si el registro falla, deja en el log el error con la traza de la excepción (e). Útil para diagnosticar problemas.
            log.error("Fallo al registrarse", e);   // (4) log ERROR
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    @GetMapping("login")
    public String login() {
        return "auth/login";
    }

}
