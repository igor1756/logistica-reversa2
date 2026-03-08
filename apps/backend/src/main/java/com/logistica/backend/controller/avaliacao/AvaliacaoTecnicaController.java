package com.logistica.backend.controller.avaliacao;

import com.logistica.backend.controller.avaliacao.dto.AvaliacaoTecnicaRequest;
import com.logistica.backend.controller.avaliacao.dto.AvaliacaoTecnicaResponse;
import com.logistica.backend.security.AuthenticatedUser;
import com.logistica.backend.service.avaliacao.AvaliacaoTecnicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/avaliacoes")
@RequiredArgsConstructor
public class AvaliacaoTecnicaController {

    private final AvaliacaoTecnicaService avaliacaoTecnicaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AvaliacaoTecnicaResponse criar(
            @RequestBody AvaliacaoTecnicaRequest request,
            @AuthenticationPrincipal AuthenticatedUser usuarioAutenticado) {

        // log
        System.out.println(">>> Entrou no controller de avaliação");
        System.out.println(">>> Usuário autenticado: " +
                (usuarioAutenticado != null ? usuarioAutenticado.getMatricula() : "null"));
        System.out.println(">>> EquipamentoId recebido: " + request.getEquipamentoId());
        // fim do log
        
        return avaliacaoTecnicaService.criar(request, usuarioAutenticado.getMatricula());
    }

    @GetMapping("/equipamento/{equipamentoId}")
    public List<AvaliacaoTecnicaResponse> listarPorEquipamento(
            @PathVariable UUID equipamentoId) {
        return avaliacaoTecnicaService.listarPorEquipamento(equipamentoId);
    }
}