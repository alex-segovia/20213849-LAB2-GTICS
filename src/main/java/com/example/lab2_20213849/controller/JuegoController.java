package com.example.lab2_20213849.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JuegoController {
    @GetMapping(value = "/buscaminas")
    public String buscaminas(){
        return "buscaminas";
    }

    @PostMapping(value = "/jugar")
    public String juego(){
        return "juego";
    }

    @PostMapping(value = "/minar")
    public String minar(){
        return "forward:/jugar";
    }
}
