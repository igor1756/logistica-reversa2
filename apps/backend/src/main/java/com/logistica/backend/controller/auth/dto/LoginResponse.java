package com.logistica.backend.controller.auth.dto;

public record LoginResponse(
        String token,
        String nome,
        String tipo,
        String matricula
) {
    
}
