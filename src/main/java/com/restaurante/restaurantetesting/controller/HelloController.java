package com.restaurante.restaurantetesting.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HelloController {
    @GetMapping("hola")
    public String hola(){
        return "hola";
    }

    @GetMapping("adios")
    public String adios(Model model){
        model.addAttribute("mensaje", "Adios terricolas");
        return "adios";
    }
}
