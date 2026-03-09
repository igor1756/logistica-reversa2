package com.logistica.backend.controller.historico;

import com.logistica.backend.controller.historico.dto.HistoricoStatusResponse;
import com.logistica.backend.service.historico.HistoricoStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/historicos")
@RequiredArgsConstructor
public class HistoricoStatusController {

    private final HistoricoStatusService historicoStatusService;

    @GetMapping("/equipamento/{equipamentoId}")
    public List<HistoricoStatusResponse> listarPorEquipamento(@PathVariable UUID equipamentoId) {
        return historicoStatusService.listarPorEquipamento(equipamentoId)
                .stream()
                .map(HistoricoStatusResponse::fromEntity)
                .toList();
    }
}