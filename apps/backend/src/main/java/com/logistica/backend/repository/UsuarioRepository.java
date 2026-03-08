package com.logistica.backend.repository;

import com.logistica.backend.domain.usuario.Usuario;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByMatricula(String matricula);

    boolean existsByMatricula(String matricula);
}