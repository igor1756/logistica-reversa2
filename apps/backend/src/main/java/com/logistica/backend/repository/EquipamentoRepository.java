package com.logistica.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logistica.backend.domain.equipamento.Equipamento;

public interface EquipamentoRepository extends JpaRepository<Equipamento, UUID> {

    boolean existsByNumUniversal(String numUniversal);

    Optional<Equipamento> findByNumUniversal(String numUniversal);

    List<Equipamento> findAllByOrderByNumUniversalAsc();
}