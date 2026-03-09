package com.logistica.backend.domain.historico;

import com.logistica.backend.domain.equipamento.Equipamento;
import com.logistica.backend.domain.equipamento.StatusEquipamento;
import com.logistica.backend.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "historico_status")
@Getter
@Setter
@NoArgsConstructor
public class HistoricoStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_de", nullable = false, length = 50)
    private StatusEquipamento statusDe;

    @Enumerated(EnumType.STRING)
    @Column(name = "status_para", nullable = false, length = 50)
    private StatusEquipamento statusPara;

    @Column(name = "data_mudanca", nullable = false)
    private LocalDateTime dataMudanca;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "usuario_id", nullable = true)
    private Usuario usuario;

    @Column(name = "descricao", nullable = false, columnDefinition = "TEXT")
    private String descricao;
}