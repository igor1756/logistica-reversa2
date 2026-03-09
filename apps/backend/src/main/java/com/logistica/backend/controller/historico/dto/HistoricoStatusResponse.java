package com.logistica.backend.controller.historico.dto;

import com.logistica.backend.domain.equipamento.StatusEquipamento;
import com.logistica.backend.domain.historico.HistoricoStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class HistoricoStatusResponse {

    private UUID id;
    private UUID equipamentoId;
    private StatusEquipamento statusDe;
    private StatusEquipamento statusPara;
    private LocalDateTime dataMudanca;
    private UUID usuarioId;
    private String nomeUsuario;
    private String descricao;

    public static HistoricoStatusResponse fromEntity(HistoricoStatus historico) {
        return new HistoricoStatusResponse(
                historico.getId(),
                historico.getEquipamento().getId(),
                historico.getStatusDe(),
                historico.getStatusPara(),
                historico.getDataMudanca(),
                historico.getUsuario() != null ? historico.getUsuario().getId() : null,
                historico.getUsuario() != null ? historico.getUsuario().getNome() : null,
                historico.getDescricao());
    }
}