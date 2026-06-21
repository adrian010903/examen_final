package com.caballeriza.backend.service;

import com.caballeriza.backend.dto.AuthResponse;
import com.caballeriza.backend.dto.LoginRequest;
import com.caballeriza.backend.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

}