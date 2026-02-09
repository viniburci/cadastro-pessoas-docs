package com.doban.cadastro_pessoas_docs.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final EmailPermitidoRepository emailPermitidoRepository;
    private final RefreshTokenService refreshTokenService;

    @Value("${app.oauth2.redirect-uri:http://localhost:4200/auth/callback}")
    private String redirectUri;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = oAuth2User.getAttribute("email");
        String nome = oAuth2User.getAttribute("name");
        String pictureUrl = oAuth2User.getAttribute("picture");

        log.info("Tentativa de login OAuth2 para email: {}", email);

        // Verifica se o email está na lista de permitidos
        EmailPermitido emailPermitido = emailPermitidoRepository.findByEmail(email)
                .orElse(null);

        if (emailPermitido == null) {
            log.warn("Email não autorizado tentou fazer login: {}", email);
            String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                    .queryParam("error", "unauthorized")
                    .queryParam("message", "Email não autorizado. Entre em contato com o administrador.")
                    .build()
                    .toUriString();
            getRedirectStrategy().sendRedirect(request, response, targetUrl);
            return;
        }

        // Busca ou cria o usuário
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseGet(() -> {
                    Usuario novoUsuario = new Usuario();
                    novoUsuario.setEmail(email);
                    novoUsuario.setNome(nome);
                    novoUsuario.setPictureUrl(pictureUrl);
                    novoUsuario.setRole(emailPermitido.getRole());
                    novoUsuario.setAtivo(true);
                    log.info("Criando novo usuário: {}", email);
                    return usuarioRepository.save(novoUsuario);
                });

        // Atualiza last login e informações do usuário
        usuario.setLastLogin(LocalDateTime.now());
        usuario.setNome(nome);
        usuario.setPictureUrl(pictureUrl);
        usuarioRepository.save(usuario);

        // Gera o token JWT e o refresh token
        String token = jwtService.generateToken(usuario);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(usuario);

        // Redireciona para o frontend com os tokens
        String targetUrl = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", token)
                .queryParam("refreshToken", refreshToken.getToken())
                .build()
                .toUriString();

        log.info("Login bem-sucedido para: {}", email);
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }
}
