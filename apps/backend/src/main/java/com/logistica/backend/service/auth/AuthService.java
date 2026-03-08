package com.logistica.backend.service.auth;

import com.logistica.backend.controller.auth.dto.LoginRequest;
import com.logistica.backend.controller.auth.dto.LoginResponse;
import com.logistica.backend.security.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public LoginResponse login(LoginRequest request) {
        var authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.matricula(), request.senha())
        );

        var user = (AuthenticatedUser) authentication.getPrincipal();
        var token = jwtService.generateToken(user);

        return new LoginResponse(
                token,
                user.getTipo(),
                user.getNome(),
                user.getMatricula()
        );
    }
}