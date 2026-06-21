package com.caballeriza.backend.config;

import com.caballeriza.backend.Security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/auth/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        // SOLO ADMINISTRADOR
                        .requestMatchers("/api/empleados/**")
                        .hasRole("ADMINISTRADOR")

                        // ADMINISTRADOR Y VETERINARIO
                        .requestMatchers("/api/historial-medico/**")
                        .hasAnyRole(
                                "ADMINISTRADOR",
                                "VETERINARIO"
                        )

                        // ADMINISTRADOR, VETERINARIO Y CUIDADOR
                        .requestMatchers(
                                "/api/caballos/**",
                                "/api/tareas/**",
                                "/api/turnos/**"
                        )
                        .hasAnyRole(
                                "ADMINISTRADOR",
                                "VETERINARIO",
                                "CUIDADOR"
                        )

                        // ALIMENTACION E INVENTARIO
                        .requestMatchers(
                                "/api/planes-alimentacion/**",
                                "/api/suministros/**",
                                "/api/inventario/**"
                        )
                        .hasAnyRole(
                                "ADMINISTRADOR",
                                "CUIDADOR"
                        )

                        // RESERVAS
                        .requestMatchers("/api/reservas/**")
                        .hasAnyRole(
                                "ADMINISTRADOR",
                                "CLIENTE"
                        )

                        // ALERTAS
                        .requestMatchers("/api/alertas/**")
                        .hasAnyRole(
                                "ADMINISTRADOR",
                                "VETERINARIO",
                                "CUIDADOR"
                        )

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form.disable());

        http.addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
        );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
