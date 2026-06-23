package com.caballeriza.backend.dto;

import com.caballeriza.backend.Security.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;

    private String nombre;

    private String email;

    private Role role;
}

