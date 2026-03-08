package com.logistica.backend.repository;

import com.logistica.backend.domain.equipamento.Equipamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EquipamentoRepository extends JpaRepository<Equipamento, UUID> {

    boolean existsByNumUniversal(String numUniversal);

    Optional<Equipamento> findByNumUniversal(String numUniversal);
}