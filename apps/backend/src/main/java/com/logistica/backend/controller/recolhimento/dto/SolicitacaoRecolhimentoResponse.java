package com.logistica.backend.controller.recolhimento.dto;

import com.logistica.backend.domain.recolhimento.StatusSolicitacaoRecolhimento;

import java.time.LocalDateTime;
import java.util.UUID;

public record SolicitacaoRecolhimentoResponse(
        UUID id,
        UUID equipamentoId,
        String nomeEquipamento,
        UUID usuarioSolicitanteId,
        String nomeUsuarioSolicitante,
        LocalDateTime dataSolicitacao,
        StatusSolicitacaoRecolhimento status
) {
}