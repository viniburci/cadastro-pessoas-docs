package com.doban.cadastro_pessoas_docs.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

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

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String requestRefreshToken = request.get("refreshToken");

        if (requestRefreshToken == null || requestRefreshToken.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Refresh token é obrigatório"));
        }

        return refreshTokenRepository.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(refreshToken -> {
                    Usuario usuario = refreshToken.getUsuario();

                    // Gera novo access token
                    String accessToken = jwtService.generateToken(usuario);

                    // Rotaciona o refresh token
                    RefreshToken newRefreshToken = refreshTokenService.createRefreshToken(usuario);

                    return ResponseEntity.ok(Map.of(
                            "accessToken", accessToken,
                            "refreshToken", newRefreshToken.getToken()
                    ));
                })
                .orElseGet(() -> ResponseEntity.status(403)
                        .body(Map.of("error", "Refresh token inválido")));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@AuthenticationPrincipal Usuario usuario) {
        refreshTokenService.deleteByUsuario(usuario);
        return ResponseEntity.ok(Map.of("message", "Logout realizado com sucesso"));
    }
}
