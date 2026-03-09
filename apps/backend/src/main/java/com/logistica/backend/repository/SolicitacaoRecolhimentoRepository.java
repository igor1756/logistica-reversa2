package com.logistica.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logistica.backend.domain.equipamento.Equipamento;
import com.logistica.backend.domain.recolhimento.SolicitacaoRecolhimento;
import com.logistica.backend.domain.recolhimento.StatusSolicitacaoRecolhimento;

public interface SolicitacaoRecolhimentoRepository extends JpaRepository<SolicitacaoRecolhimento, UUID> {

    boolean existsByEquipamentoIdAndStatus(UUID equipamentoId, StatusSolicitacaoRecolhimento status);

    List<SolicitacaoRecolhimento> findAllByOrderByDataSolicitacaoDesc();

    Optional<SolicitacaoRecolhimento> findFirstByEquipamentoAndStatusOrderByDataSolicitacaoDesc(
            Equipamento equipamento,
            StatusSolicitacaoRecolhimento status);

    Optional<SolicitacaoRecolhimento> findByEquipamentoAndStatus(
            Equipamento equipamento,
            StatusSolicitacaoRecolhimento status);
}