package com.logistica.backend.controller.auth;

import com.logistica.backend.controller.auth.dto.LoginRequest;
import com.logistica.backend.controller.auth.dto.LoginResponse;
import com.logistica.backend.service.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        return authService.login(request);
    }
}