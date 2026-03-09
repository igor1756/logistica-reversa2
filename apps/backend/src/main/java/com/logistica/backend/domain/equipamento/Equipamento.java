package com.logistica.backend.domain.equipamento;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
// import com.logistica.backend.domain.historico.HistoricoStatus;
// import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.logistica.backend.domain.historico.HistoricoStatus;
import com.logistica.backend.domain.usuario.Usuario;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @OneToMany(mappedBy = "equipamento", cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    private List<HistoricoStatus> historicoStatus = new ArrayList<>();
}