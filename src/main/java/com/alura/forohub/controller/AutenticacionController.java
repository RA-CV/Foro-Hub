package com.alura.forohub.controller;

import com.alura.forohub.dto.DatosAutenticacionUsuario;
import com.alura.forohub.dto.DatosAutenticacionUsuario.DatosJWTToken;
import com.alura.forohub.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AutenticacionController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<DatosJWTToken> login(@RequestBody @Valid DatosAutenticacionUsuario datos) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(datos.login(), datos.password())
        );
        String token = tokenService.generarToken(datos.login());
        return ResponseEntity.ok(new DatosJWTToken(token));
    }
}
