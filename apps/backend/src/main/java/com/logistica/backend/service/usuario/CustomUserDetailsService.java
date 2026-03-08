package com.logistica.backend.service.usuario;

import com.logistica.backend.repository.UsuarioRepository;
import com.logistica.backend.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String matricula) throws UsernameNotFoundException {
        return usuarioRepository.findByMatricula(matricula)
                .map(AuthenticatedUser::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado para a matrícula: " + matricula));
    }
}