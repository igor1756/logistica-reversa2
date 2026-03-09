package com.logistica.backend.service.destinacao;

import com.logistica.backend.domain.avaliacao.AvaliacaoTecnica;
import com.logistica.backend.domain.equipamento.Equipamento;
import com.logistica.backend.domain.equipamento.StatusEquipamento;
import com.logistica.backend.domain.usuario.Usuario;
import com.logistica.backend.repository.AvaliacaoTecnicaRepository;
import com.logistica.backend.repository.EquipamentoRepository;
import com.logistica.backend.service.historico.HistoricoStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DestinacaoService {

    private static final BigDecimal TAXA_DEPRECIACAO_ANUAL = new BigDecimal("0.20");
    private static final int VIDA_UTIL_ANOS = 5;

    private final AvaliacaoTecnicaRepository avaliacaoTecnicaRepository;
    private final EquipamentoRepository equipamentoRepository;
    private final HistoricoStatusService historicoStatusService;

    public int calcularIdadeEmAnos(Equipamento equipamento) {
        LocalDate dataAquisicao = equipamento.getDataAquisicao();

        if (dataAquisicao == null) {
            throw new IllegalArgumentException("Equipamento sem data de aquisição cadastrada.");
        }

        return Period.between(dataAquisicao, LocalDate.now()).getYears();
    }

    public boolean equipamentoEstaValido(Equipamento equipamento) {
        return calcularIdadeEmAnos(equipamento) < VIDA_UTIL_ANOS;
    }

    public BigDecimal calcularValorResidual(Equipamento equipamento) {
        if (equipamento.getValorAquisicao() == null) {
            throw new IllegalArgumentException("Equipamento sem valor de aquisição cadastrado.");
        }

        int idadeEmAnos = calcularIdadeEmAnos(equipamento);

        if (idadeEmAnos >= VIDA_UTIL_ANOS) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        BigDecimal valorAquisicao = equipamento.getValorAquisicao();
        BigDecimal depreciacaoTotal = TAXA_DEPRECIACAO_ANUAL.multiply(BigDecimal.valueOf(idadeEmAnos));
        BigDecimal fatorResidual = BigDecimal.ONE.subtract(depreciacaoTotal);
        BigDecimal valorResidual = valorAquisicao.multiply(fatorResidual);

        if (valorResidual.compareTo(BigDecimal.ZERO) < 0) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        return valorResidual.setScale(2, RoundingMode.HALF_UP);
    }

    public AvaliacaoTecnica obterAvaliacaoMaisRecente(Equipamento equipamento) {
        List<AvaliacaoTecnica> avaliacoes = avaliacaoTecnicaRepository
                .findByEquipamentoOrderByDataAvaliacaoDesc(equipamento);

        if (avaliacoes.isEmpty()) {
            throw new IllegalArgumentException("Nenhuma avaliação técnica encontrada para o equipamento.");
        }

        return avaliacoes.get(0);
    }

    public StatusEquipamento calcularDestinacao(Equipamento equipamento) {
        AvaliacaoTecnica avaliacaoMaisRecente = obterAvaliacaoMaisRecente(equipamento);
        boolean equipamentoValido = equipamentoEstaValido(equipamento);

        if (Boolean.FALSE.equals(avaliacaoMaisRecente.getEhRecuperavel())) {
            return StatusEquipamento.DESTINADO_RECICLAGEM;
        }

        if (Boolean.TRUE.equals(avaliacaoMaisRecente.getEstaFuncionando()) && equipamentoValido) {
            return StatusEquipamento.DESTINADO_VENDA;
        }

        if (Boolean.TRUE.equals(avaliacaoMaisRecente.getEstaFuncionando()) && !equipamentoValido) {
            return StatusEquipamento.DESTINADO_DOACAO;
        }

        if (Boolean.FALSE.equals(avaliacaoMaisRecente.getEstaFuncionando()) && equipamentoValido) {
            return StatusEquipamento.DESTINADO_SUPORTE;
        }

        if (Boolean.FALSE.equals(avaliacaoMaisRecente.getEstaFuncionando()) && !equipamentoValido) {
            return StatusEquipamento.DESTINADO_RECICLAGEM;
        }

        throw new IllegalStateException("Não foi possível determinar a destinação do equipamento.");
    }

    @Transactional
    public Equipamento aplicarDestinacao(Equipamento equipamento, Usuario usuarioResponsavel) {
        StatusEquipamento statusAnterior = equipamento.getStatusAtual();
        StatusEquipamento statusDestino = calcularDestinacao(equipamento);

        if (statusAnterior == statusDestino) {
            return equipamento;
        }

        equipamento.setStatusAtual(statusDestino);
        Equipamento equipamentoAtualizado = equipamentoRepository.save(equipamento);

        historicoStatusService.registrarMudancaStatus(
                equipamentoAtualizado,
                statusAnterior,
                statusDestino,
                usuarioResponsavel,
                montarDescricaoDestinacao(equipamentoAtualizado)
        );

        return equipamentoAtualizado;
    }

    private String montarDescricaoDestinacao(Equipamento equipamento) {
        AvaliacaoTecnica avaliacaoMaisRecente = obterAvaliacaoMaisRecente(equipamento);
        boolean equipamentoValido = equipamentoEstaValido(equipamento);
        BigDecimal valorResidual = calcularValorResidual(equipamento);

        return String.format(
                "Destinação automática definida após avaliação técnica. Funcionando: %s. Recuperável: %s. Válido: %s. Valor residual estimado: %s.",
                avaliacaoMaisRecente.getEstaFuncionando(),
                avaliacaoMaisRecente.getEhRecuperavel(),
                equipamentoValido,
                valorResidual
        );
    }
}