package com.caballeriza.backend.controller;

import com.caballeriza.backend.Security.Role;
import com.caballeriza.backend.dto.UserDTO;
import com.caballeriza.backend.model.User;
import com.caballeriza.backend.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public List<UserDTO> listar(Authentication authentication) {
        validarAdministrador(authentication);

        return userRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .sorted(
                        Comparator.comparing(
                                UserDTO::getNombre,
                                Comparator.nullsLast(
                                        String.CASE_INSENSITIVE_ORDER
                                )
                        )
                )
                .toList();
    }

    @PatchMapping("/{id}/rol")
    public UserDTO cambiarRol(
            @PathVariable Long id,
            @Valid @RequestBody CambiarRolRequest request,
            Authentication authentication
    ) {
        validarAdministrador(authentication);

        User user = obtenerPorId(id);
        user.setRole(request.role());

        return convertirADTO(userRepository.save(user));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(
            @PathVariable Long id,
            Authentication authentication
    ) {
        validarAdministrador(authentication);

        User user = obtenerPorId(id);

        String correoAutenticado = authentication.getName();

        if (
                correoAutenticado != null &&
                        correoAutenticado.equalsIgnoreCase(user.getEmail())
        ) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "No puede eliminar su propia cuenta"
            );
        }

        userRepository.delete(user);
    }

    private User obtenerPorId(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Usuario no encontrado"
                        )
                );
    }

    private void validarAdministrador(
            Authentication authentication
    ) {
        if (
                authentication == null ||
                        !authentication.isAuthenticated()
        ) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Debe iniciar sesión"
            );
        }

        boolean esAdministrador =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(authority ->
                                authority.getAuthority()
                                        .equals("ADMINISTRADOR") ||
                                        authority.getAuthority()
                                                .equals("ROLE_ADMINISTRADOR")
                        );

        if (!esAdministrador) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Solo un administrador puede gestionar usuarios"
            );
        }
    }

    private UserDTO convertirADTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .nombre(user.getNombre())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    public record CambiarRolRequest(
            @NotNull(message = "El rol es obligatorio")
            Role role
    ) {
    }
}

