package com.logistica.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/teste-auth")
    public String testeAuth() {
        return "Rota protegida acessada com sucesso";
    }
}
