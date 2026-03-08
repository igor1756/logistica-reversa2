package com.logistica.backend.domain.avaliacao;

import com.logistica.backend.domain.equipamento.Equipamento;
import com.logistica.backend.domain.usuario.Usuario;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "avaliacoes_tecnicas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AvaliacaoTecnica {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "equipamento_id", nullable = false)
    private Equipamento equipamento;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tecnico_id", nullable = false)
    private Usuario tecnico;

    @Column(name = "data_avaliacao", nullable = false)
    private LocalDateTime dataAvaliacao;

    @Column(name = "esta_funcionando", nullable = false)
    private Boolean estaFuncionando;

    @Column(name = "eh_recuperavel", nullable = false)
    private Boolean ehRecuperavel;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;
}