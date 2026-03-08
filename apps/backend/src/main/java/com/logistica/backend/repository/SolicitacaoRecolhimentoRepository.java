package com.logistica.backend.repository;

import com.logistica.backend.domain.recolhimento.SolicitacaoRecolhimento;
import com.logistica.backend.domain.recolhimento.StatusSolicitacaoRecolhimento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SolicitacaoRecolhimentoRepository extends JpaRepository<SolicitacaoRecolhimento, UUID> {

    boolean existsByEquipamentoIdAndStatus(UUID equipamentoId, StatusSolicitacaoRecolhimento status);

    List<SolicitacaoRecolhimento> findAllByOrderByDataSolicitacaoDesc();
}