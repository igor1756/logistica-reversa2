package com.logistica.backend.controller.equipamento.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EquipamentoRequest(
        String numUniversal,
        String modelo,
        String descricao,
        LocalDate dataAquisicao,
        BigDecimal valorAquisicao,
        UUID usuarioId) {
}