package com.logistica.backend.domain.equipamento;

import com.logistica.backend.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "equipamentos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 100)
    private String numUniversal;

    @Column(nullable = false, length = 150)
    private String modelo;

    @Column(length = 500)
    private String descricao;

    @Column(nullable = false)
    private LocalDate dataAquisicao;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorAquisicao;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private StatusEquipamento statusAtual;
}