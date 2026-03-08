package com.logistica.backend.controller.equipamento;

import com.logistica.backend.controller.equipamento.dto.EquipamentoRequest;
import com.logistica.backend.controller.equipamento.dto.EquipamentoResponse;
import com.logistica.backend.service.equipamento.EquipamentoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/equipamentos")
@RequiredArgsConstructor
public class EquipamentoController {

    private final EquipamentoService equipamentoService;

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody EquipamentoRequest request) {
        try {
            EquipamentoResponse response = equipamentoService.criar(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<EquipamentoResponse>> listarTodos() {
        return ResponseEntity.ok(equipamentoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscarPorId(@PathVariable UUID id) {
        try {
            EquipamentoResponse response = equipamentoService.buscarPorId(id);
            return ResponseEntity.ok(response);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizar(@PathVariable UUID id, @RequestBody EquipamentoRequest request) {
        try {
            EquipamentoResponse response = equipamentoService.atualizar(id, request);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}