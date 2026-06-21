package com.caballeriza.backend.controller;

import com.caballeriza.backend.dto.AuthResponse;
import com.caballeriza.backend.dto.LoginRequest;
import com.caballeriza.backend.dto.RegisterRequest;
import com.caballeriza.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public AuthResponse register(@RequestBody RegisterRequest request) {
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}