package com.logistica.backend.service.recolhimento;

import com.logistica.backend.controller.recolhimento.dto.SolicitacaoRecolhimentoRequest;
import com.logistica.backend.controller.recolhimento.dto.SolicitacaoRecolhimentoResponse;
import com.logistica.backend.domain.equipamento.Equipamento;
import com.logistica.backend.domain.equipamento.StatusEquipamento;
import com.logistica.backend.domain.recolhimento.SolicitacaoRecolhimento;
import com.logistica.backend.domain.recolhimento.StatusSolicitacaoRecolhimento;
import com.logistica.backend.domain.usuario.Usuario;
import com.logistica.backend.repository.EquipamentoRepository;
import com.logistica.backend.repository.SolicitacaoRecolhimentoRepository;
import com.logistica.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SolicitacaoRecolhimentoService {

    private final SolicitacaoRecolhimentoRepository solicitacaoRecolhimentoRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final UsuarioRepository usuarioRepository;

    public SolicitacaoRecolhimentoResponse criar(SolicitacaoRecolhimentoRequest request, String matriculaUsuarioLogado) {
        Equipamento equipamento = equipamentoRepository.findById(request.equipamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado."));

        Usuario usuarioSolicitante = usuarioRepository.findByMatricula(matriculaUsuarioLogado)
                .orElseThrow(() -> new EntityNotFoundException("Usuário autenticado não encontrado."));

        boolean existePendente = solicitacaoRecolhimentoRepository
                .existsByEquipamentoIdAndStatus(
                        equipamento.getId(),
                        StatusSolicitacaoRecolhimento.PENDENTE
                );

        if (existePendente) {
            throw new IllegalStateException("Já existe uma solicitação pendente para este equipamento.");
        }

        SolicitacaoRecolhimento solicitacao = SolicitacaoRecolhimento.builder()
                .equipamento(equipamento)
                .usuarioSolicitante(usuarioSolicitante)
                .status(StatusSolicitacaoRecolhimento.PENDENTE)
                .build();

        SolicitacaoRecolhimento solicitacaoSalva = solicitacaoRecolhimentoRepository.save(solicitacao);

        equipamento.setStatusAtual(StatusEquipamento.RECOLHIMENTO_SOLICITADO);
        equipamentoRepository.save(equipamento);

        return toResponse(solicitacaoSalva);
    }

    public List<SolicitacaoRecolhimentoResponse> listar() {
        return solicitacaoRecolhimentoRepository.findAllByOrderByDataSolicitacaoDesc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public SolicitacaoRecolhimentoResponse cancelar(UUID id) {
        SolicitacaoRecolhimento solicitacao = solicitacaoRecolhimentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Solicitação não encontrada."));

        if (solicitacao.getStatus() != StatusSolicitacaoRecolhimento.PENDENTE) {
            throw new IllegalStateException("Somente solicitações pendentes podem ser canceladas.");
        }

        solicitacao.setStatus(StatusSolicitacaoRecolhimento.CANCELADA);
        SolicitacaoRecolhimento solicitacaoAtualizada = solicitacaoRecolhimentoRepository.save(solicitacao);

        Equipamento equipamento = solicitacao.getEquipamento();
        equipamento.setStatusAtual(StatusEquipamento.EM_USO);
        equipamentoRepository.save(equipamento);

        return toResponse(solicitacaoAtualizada);
    }

    private SolicitacaoRecolhimentoResponse toResponse(SolicitacaoRecolhimento solicitacao) {
        Equipamento equipamento = solicitacao.getEquipamento();
        Usuario usuarioSolicitante = solicitacao.getUsuarioSolicitante();

        return new SolicitacaoRecolhimentoResponse(
                solicitacao.getId(),
                equipamento.getId(),
                montarNomeEquipamento(equipamento),
                usuarioSolicitante.getId(),
                usuarioSolicitante.getNome(),
                solicitacao.getDataSolicitacao(),
                solicitacao.getStatus()
        );
    }

    private String montarNomeEquipamento(Equipamento equipamento) {
        if (equipamento.getModelo() != null && !equipamento.getModelo().isBlank()) {
            return equipamento.getModelo();
        }

        if (equipamento.getDescricao() != null && !equipamento.getDescricao().isBlank()) {
            return equipamento.getDescricao();
        }

        if (equipamento.getNumUniversal() != null && !equipamento.getNumUniversal().isBlank()) {
            return equipamento.getNumUniversal();
        }

        return "Equipamento " + equipamento.getId();
    }
}