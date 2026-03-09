package com.logistica.backend.service.avaliacao;

import com.logistica.backend.controller.avaliacao.dto.AvaliacaoTecnicaRequest;
import com.logistica.backend.controller.avaliacao.dto.AvaliacaoTecnicaResponse;
import com.logistica.backend.domain.avaliacao.AvaliacaoTecnica;
import com.logistica.backend.domain.equipamento.Equipamento;
import com.logistica.backend.domain.equipamento.StatusEquipamento;
import com.logistica.backend.domain.recolhimento.StatusSolicitacaoRecolhimento;
import com.logistica.backend.domain.usuario.Usuario;
import com.logistica.backend.repository.AvaliacaoTecnicaRepository;
import com.logistica.backend.repository.EquipamentoRepository;
import com.logistica.backend.repository.SolicitacaoRecolhimentoRepository;
import com.logistica.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.logistica.backend.service.destinacao.DestinacaoService;

@Service
@RequiredArgsConstructor
public class AvaliacaoTecnicaService {

    private final AvaliacaoTecnicaRepository avaliacaoTecnicaRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DestinacaoService destinacaoService;
    private final SolicitacaoRecolhimentoRepository solicitacaoRecolhimentoRepository;

    @Transactional
    public AvaliacaoTecnicaResponse criar(AvaliacaoTecnicaRequest request, String matriculaUsuarioAutenticado) {
        Equipamento equipamento = equipamentoRepository.findById(request.getEquipamentoId())
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado."));

        Usuario tecnico = usuarioRepository.findByMatricula(matriculaUsuarioAutenticado)
                .orElseThrow(() -> new EntityNotFoundException("Usuário autenticado não encontrado."));

        validarStatusParaAvaliacao(equipamento);
        validarRequest(request);

        AvaliacaoTecnica avaliacao = AvaliacaoTecnica.builder()
                .equipamento(equipamento)
                .tecnico(tecnico)
                .dataAvaliacao(LocalDateTime.now())
                .estaFuncionando(request.getEstaFuncionando())
                .ehRecuperavel(request.getEhRecuperavel())
                .descricao(request.getDescricao())
                .build();

        AvaliacaoTecnica salva = avaliacaoTecnicaRepository.save(avaliacao);

        concluirSolicitacaoPendenteSeExistir(equipamento);
        equipamento.setStatusAtual(StatusEquipamento.AVALIADO);
        Equipamento equipamentoAtualizado = equipamentoRepository.save(equipamento);

        destinacaoService.aplicarDestinacao(equipamentoAtualizado, tecnico);

        return toResponse(salva);
    }

    @Transactional(readOnly = true)
    public List<AvaliacaoTecnicaResponse> listarPorEquipamento(UUID equipamentoId) {
        Equipamento equipamento = equipamentoRepository.findById(equipamentoId)
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado."));

        return avaliacaoTecnicaRepository.findByEquipamentoOrderByDataAvaliacaoDesc(equipamento)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private void validarStatusParaAvaliacao(Equipamento equipamento) {
        StatusEquipamento status = equipamento.getStatusAtual();

        boolean statusValido = status == StatusEquipamento.RECOLHIMENTO_SOLICITADO
                || status == StatusEquipamento.AVALIADO;

        if (!statusValido) {
            throw new IllegalStateException(
                    "O equipamento não está em status compatível para avaliação técnica.");
        }
    }

    private void validarRequest(AvaliacaoTecnicaRequest request) {
        if (request.getEquipamentoId() == null) {
            throw new IllegalArgumentException("equipamentoId é obrigatório.");
        }

        if (request.getEstaFuncionando() == null) {
            throw new IllegalArgumentException("estaFuncionando é obrigatório.");
        }

        if (request.getEhRecuperavel() == null) {
            throw new IllegalArgumentException("ehRecuperavel é obrigatório.");
        }
    }

    private AvaliacaoTecnicaResponse toResponse(AvaliacaoTecnica avaliacao) {
        Equipamento equipamento = avaliacao.getEquipamento();
        Usuario tecnico = avaliacao.getTecnico();

        return AvaliacaoTecnicaResponse.builder()
                .id(avaliacao.getId())
                .equipamentoId(equipamento.getId())
                .numeroPatrimonio(equipamento.getNumUniversal())
                .modelo(equipamento.getModelo())
                .descricaoEquipamento(equipamento.getDescricao())
                .tecnicoId(tecnico.getId())
                .nomeTecnico(tecnico.getNome())
                .dataAvaliacao(avaliacao.getDataAvaliacao())
                .estaFuncionando(avaliacao.getEstaFuncionando())
                .ehRecuperavel(avaliacao.getEhRecuperavel())
                .descricao(avaliacao.getDescricao())
                .build();
    }

    private void concluirSolicitacaoPendenteSeExistir(Equipamento equipamento) {
        solicitacaoRecolhimentoRepository
                .findByEquipamentoAndStatus(equipamento, StatusSolicitacaoRecolhimento.PENDENTE)
                .ifPresent(solicitacao -> {
                    solicitacao.setStatus(StatusSolicitacaoRecolhimento.CONCLUIDA);
                    solicitacaoRecolhimentoRepository.save(solicitacao);
                });
    }
}