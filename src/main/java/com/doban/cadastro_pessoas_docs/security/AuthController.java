package com.doban.cadastro_pessoas_docs.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(Map.of(
                "id", usuario.getId(),
                "email", usuario.getEmail(),
                "nome", usuario.getNome(),
                "pictureUrl", usuario.getPictureUrl() != null ? usuario.getPictureUrl() : "",
                "role", usuario.getRole().name(),
                "ativo", usuario.getAtivo()
        ));
    }

    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@AuthenticationPrincipal Usuario usuario) {
        return ResponseEntity.ok(Map.of("valid", true, "email", usuario.getEmail()));
    }
}
