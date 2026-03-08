package com.logistica.backend.service.equipamento;

import com.logistica.backend.controller.equipamento.dto.EquipamentoRequest;
import com.logistica.backend.controller.equipamento.dto.EquipamentoResponse;
import com.logistica.backend.domain.equipamento.Equipamento;
import com.logistica.backend.domain.equipamento.StatusEquipamento;
import com.logistica.backend.domain.usuario.Usuario;
import com.logistica.backend.repository.EquipamentoRepository;
import com.logistica.backend.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EquipamentoService {

    private final EquipamentoRepository equipamentoRepository;
    private final UsuarioRepository usuarioRepository;

    public EquipamentoResponse criar(EquipamentoRequest request) {
        if (equipamentoRepository.existsByNumUniversal(request.numUniversal())) {
            throw new IllegalArgumentException("Já existe equipamento com este número universal.");
        }

        Usuario usuario = buscarUsuarioSeInformado(request.usuarioId());

        Equipamento equipamento = Equipamento.builder()
                .numUniversal(request.numUniversal())
                .modelo(request.modelo())
                .descricao(request.descricao())
                .dataAquisicao(request.dataAquisicao())
                .valorAquisicao(request.valorAquisicao())
                .usuario(usuario)
                .statusAtual(StatusEquipamento.EM_USO)
                .build();

        Equipamento salvo = equipamentoRepository.save(equipamento);
        return toResponse(salvo);
    }

    public List<EquipamentoResponse> listarTodos() {
        return equipamentoRepository.findAllByOrderByNumUniversalAsc()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public EquipamentoResponse buscarPorId(UUID id) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado."));

        return toResponse(equipamento);
    }

    public EquipamentoResponse atualizar(UUID id, EquipamentoRequest request) {
        Equipamento equipamento = equipamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipamento não encontrado."));

        if (!equipamento.getNumUniversal().equals(request.numUniversal())
                && equipamentoRepository.existsByNumUniversal(request.numUniversal())) {
            throw new IllegalArgumentException("Já existe equipamento com este número universal.");
        }

        Usuario usuario = buscarUsuarioSeInformado(request.usuarioId());

        equipamento.setNumUniversal(request.numUniversal());
        equipamento.setModelo(request.modelo());
        equipamento.setDescricao(request.descricao());
        equipamento.setDataAquisicao(request.dataAquisicao());
        equipamento.setValorAquisicao(request.valorAquisicao());
        equipamento.setUsuario(usuario);

        Equipamento atualizado = equipamentoRepository.save(equipamento);
        return toResponse(atualizado);
    }

    private Usuario buscarUsuarioSeInformado(UUID usuarioId) {
        if (usuarioId == null) {
            return null;
        }

        return usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new EntityNotFoundException("Usuário responsável não encontrado."));
    }

    private EquipamentoResponse toResponse(Equipamento equipamento) {
        return new EquipamentoResponse(
                equipamento.getId(),
                equipamento.getNumUniversal(),
                equipamento.getModelo(),
                equipamento.getDescricao(),
                equipamento.getDataAquisicao(),
                equipamento.getValorAquisicao(),
                equipamento.getUsuario() != null ? equipamento.getUsuario().getId() : null,
                equipamento.getUsuario() != null ? equipamento.getUsuario().getNome() : null,
                equipamento.getStatusAtual()
        );
    }
}