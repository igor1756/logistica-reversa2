package com.logistica.backend.controller.recolhimento;

import com.logistica.backend.controller.recolhimento.dto.SolicitacaoRecolhimentoRequest;
import com.logistica.backend.controller.recolhimento.dto.SolicitacaoRecolhimentoResponse;
import com.logistica.backend.service.recolhimento.SolicitacaoRecolhimentoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recolhimentos")
@RequiredArgsConstructor
public class SolicitacaoRecolhimentoController {

    private final SolicitacaoRecolhimentoService solicitacaoRecolhimentoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitacaoRecolhimentoResponse criar(
            @RequestBody SolicitacaoRecolhimentoRequest request,
            Authentication authentication
    ) {
        String matriculaUsuarioLogado = authentication.getName();
        return solicitacaoRecolhimentoService.criar(request, matriculaUsuarioLogado);
    }

    @GetMapping
    public List<SolicitacaoRecolhimentoResponse> listar() {
        return solicitacaoRecolhimentoService.listar();
    }

    @PatchMapping("/{id}/cancelar")
    public SolicitacaoRecolhimentoResponse cancelar(@PathVariable UUID id) {
        return solicitacaoRecolhimentoService.cancelar(id);
    }
}