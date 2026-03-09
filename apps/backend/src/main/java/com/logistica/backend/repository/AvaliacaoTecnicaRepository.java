package com.logistica.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logistica.backend.domain.avaliacao.AvaliacaoTecnica;
import com.logistica.backend.domain.equipamento.Equipamento;

public interface AvaliacaoTecnicaRepository extends JpaRepository<AvaliacaoTecnica, UUID> {

    List<AvaliacaoTecnica> findByEquipamentoOrderByDataAvaliacaoDesc(Equipamento equipamento);

    Optional<AvaliacaoTecnica> findFirstByEquipamentoIdOrderByDataAvaliacaoDesc(UUID equipamentoId);
}