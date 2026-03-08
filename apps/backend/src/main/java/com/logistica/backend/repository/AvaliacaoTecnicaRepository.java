package com.logistica.backend.repository;

import com.logistica.backend.domain.avaliacao.AvaliacaoTecnica;
import com.logistica.backend.domain.equipamento.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AvaliacaoTecnicaRepository extends JpaRepository<AvaliacaoTecnica, UUID> {

    List<AvaliacaoTecnica> findByEquipamentoOrderByDataAvaliacaoDesc(Equipamento equipamento);
}