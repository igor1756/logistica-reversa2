package com.logistica.backend.controller.avaliacao.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class AvaliacaoTecnicaResponse {

    private UUID id;

    private UUID equipamentoId;
    private String numeroPatrimonio;
    private String modelo;
    private String descricaoEquipamento;

    private UUID tecnicoId;
    private String nomeTecnico;

    private LocalDateTime dataAvaliacao;

    private Boolean estaFuncionando;
    private Boolean ehRecuperavel;
    private String descricao;
}