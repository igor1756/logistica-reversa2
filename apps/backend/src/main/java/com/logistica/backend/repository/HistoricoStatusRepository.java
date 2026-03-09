package com.logistica.backend.repository;

import com.logistica.backend.domain.historico.HistoricoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface HistoricoStatusRepository extends JpaRepository<HistoricoStatus, UUID> {

    List<HistoricoStatus> findByEquipamentoIdOrderByDataMudancaDesc(UUID equipamentoId);
}