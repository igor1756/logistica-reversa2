package com.logistica.backend.service.historico;

import com.logistica.backend.domain.equipamento.Equipamento;
import com.logistica.backend.domain.equipamento.StatusEquipamento;
import com.logistica.backend.domain.historico.HistoricoStatus;
import com.logistica.backend.domain.usuario.Usuario;
import com.logistica.backend.repository.HistoricoStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HistoricoStatusService {

    private final HistoricoStatusRepository historicoStatusRepository;

    public HistoricoStatus registrarMudancaStatus(
            Equipamento equipamento,
            StatusEquipamento statusDe,
            StatusEquipamento statusPara,
            Usuario usuario,
            String descricao
    ) {
        HistoricoStatus historico = new HistoricoStatus();
        historico.setEquipamento(equipamento);
        historico.setStatusDe(statusDe);
        historico.setStatusPara(statusPara);
        historico.setDataMudanca(LocalDateTime.now());
        historico.setUsuario(usuario);
        historico.setDescricao(descricao);

        return historicoStatusRepository.save(historico);
    }

    public List<HistoricoStatus> listarPorEquipamento(UUID equipamentoId) {
        return historicoStatusRepository.findByEquipamentoIdOrderByDataMudancaDesc(equipamentoId);
    }
}