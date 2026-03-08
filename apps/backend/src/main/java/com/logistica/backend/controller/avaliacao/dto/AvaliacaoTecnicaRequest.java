package com.logistica.backend.controller.avaliacao.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AvaliacaoTecnicaRequest {

    private UUID equipamentoId;
    private Boolean estaFuncionando;
    private Boolean ehRecuperavel;
    private String descricao;
}