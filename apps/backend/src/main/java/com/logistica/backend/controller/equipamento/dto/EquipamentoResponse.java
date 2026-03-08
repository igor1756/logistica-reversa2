package com.logistica.backend.controller.equipamento.dto;

import com.logistica.backend.domain.equipamento.StatusEquipamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record EquipamentoResponse(
        UUID id,
        String numUniversal,
        String modelo,
        String descricao,
        LocalDate dataAquisicao,
        BigDecimal valorAquisicao,
        UUID usuarioId,
        String nomeUsuario,
        StatusEquipamento statusAtual) {
}