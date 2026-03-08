package com.logistica.backend.controller.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String matricula,
        @NotBlank String senha) {
}
