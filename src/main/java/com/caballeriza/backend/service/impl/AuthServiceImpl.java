package com.caballeriza.backend.service.impl;

import com.caballeriza.backend.dto.AuthResponse;
import com.caballeriza.backend.dto.LoginRequest;
import com.caballeriza.backend.dto.RegisterRequest;
import com.caballeriza.backend.model.User;
import com.caballeriza.backend.repository.UserRepository;
import com.caballeriza.backend.Security.JwtService;
import com.caballeriza.backend.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public AuthResponse register(RegisterRequest request) {

        User user = User.builder()
                .nombre(request.getNombre())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {
            throw new RuntimeException("Credenciales incorrectas");
        }

        String token = jwtService.generateToken(user.getEmail());

        return new AuthResponse(token);
    }
}