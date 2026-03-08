package com.logistica.backend.security;

import com.logistica.backend.domain.usuario.Usuario;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class AuthenticatedUser implements UserDetails {

    private final Usuario usuario;

    public AuthenticatedUser(Usuario usuario) {
        this.usuario = usuario;
    }

    public UUID getId() {
        return usuario.getId();
    }

    public String getNome() {
        return usuario.getNome();
    }

    public String getMatricula() {
        return usuario.getMatricula();
    }

    public String getTipo() {
        return usuario.getTipo().name();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getTipo().name()));
    }

    @Override
    public String getPassword() {
        return usuario.getSenhaHash();
    }

    @Override
    public String getUsername() {
        return usuario.getMatricula();
    }
}