package com.caballeriza.backend.dto;

import com.caballeriza.backend.Security.Role;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {

    private String nombre;
    private String email;
    private String password;
    private Role role;

}