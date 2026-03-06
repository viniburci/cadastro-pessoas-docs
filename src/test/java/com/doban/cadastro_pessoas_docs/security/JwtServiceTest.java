package com.doban.cadastro_pessoas_docs.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    private static final String SECRET = "chave-secreta-de-teste-com-pelo-menos-32-caracteres-para-hmac";
    private static final long EXPIRATION = 1_800_000L;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "secret", SECRET);
        ReflectionTestUtils.setField(jwtService, "expiration", EXPIRATION);
    }

    @Test
    void generateToken_retornaTokenNaoNulo() {
        UserDetails user = criarUser("usuario@email.com");

        String token = jwtService.generateToken(user);

        assertThat(token).isNotNull().isNotBlank();
    }

    @Test
    void extractUsername_retornaUsernameCorreto() {
        UserDetails user = criarUser("usuario@email.com");
        String token = jwtService.generateToken(user);

        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo("usuario@email.com");
    }

    @Test
    void isTokenValid_tokenValidoParaUsuarioCorreto_retornaTrue() {
        UserDetails user = criarUser("usuario@email.com");
        String token = jwtService.generateToken(user);

        boolean valido = jwtService.isTokenValid(token, user);

        assertThat(valido).isTrue();
    }

    @Test
    void isTokenValid_tokenDeOutroUsuario_retornaFalse() {
        UserDetails user1 = criarUser("usuario1@email.com");
        UserDetails user2 = criarUser("usuario2@email.com");
        String token = jwtService.generateToken(user1);

        boolean valido = jwtService.isTokenValid(token, user2);

        assertThat(valido).isFalse();
    }

    @Test
    void isTokenValid_tokenExpirado_lancaExpiredJwtException() {
        ReflectionTestUtils.setField(jwtService, "expiration", -1000L);
        UserDetails user = criarUser("usuario@email.com");
        String token = jwtService.generateToken(user);
        ReflectionTestUtils.setField(jwtService, "expiration", EXPIRATION);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> jwtService.isTokenValid(token, user))
                .isInstanceOf(io.jsonwebtoken.ExpiredJwtException.class);
    }

    @Test
    void generateToken_comExtraClaims_retornaTokenValido() {
        UserDetails user = criarUser("usuario@email.com");
        java.util.Map<String, Object> claims = java.util.Map.of("role", "ADMIN");

        String token = jwtService.generateToken(claims, user);
        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo("usuario@email.com");
    }

    private UserDetails criarUser(String email) {
        return User.builder()
                .username(email)
                .password("")
                .authorities(List.of())
                .build();
    }
}
