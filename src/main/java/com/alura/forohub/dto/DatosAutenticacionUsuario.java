package com.alura.forohub.dto;

import jakarta.validation.constraints.NotBlank;

public record DatosAutenticacionUsuario(
        @NotBlank String login,
        @NotBlank String password
) {
    public record DatosJWTToken(String token) {}
}
