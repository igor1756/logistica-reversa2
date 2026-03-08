package com.logistica.backend.config;

import com.logistica.backend.domain.usuario.UserRole;
import com.logistica.backend.domain.usuario.Usuario;
import com.logistica.backend.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        String matriculaAdmin = "0000001";

        if (usuarioRepository.existsByMatricula(matriculaAdmin)) {
            return;
        }

        Usuario admin = new Usuario();
        admin.setMatricula(matriculaAdmin);
        admin.setNome("Administrador");
        admin.setTipo(UserRole.ADMIN);
        admin.setSetor("TI");
        admin.setSenhaHash(passwordEncoder.encode("admin123"));

        usuarioRepository.save(admin);

        System.out.println("Usuário admin criado com sucesso. Matrícula: 0000001 | Senha: admin123");
    }
}