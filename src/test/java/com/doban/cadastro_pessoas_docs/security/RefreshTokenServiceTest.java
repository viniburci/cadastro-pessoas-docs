package com.doban.cadastro_pessoas_docs.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenService refreshTokenService;

    @Test
    void createRefreshToken_removeTokenAnteriorESalvaNovoToken() {
        ReflectionTestUtils.setField(refreshTokenService, "refreshExpiration", 604_800_000L);
        Usuario usuario = criarUsuario(1L, "user@email.com");
        RefreshToken tokenSalvo = new RefreshToken();
        tokenSalvo.setToken("uuid-gerado");
        tokenSalvo.setUsuario(usuario);
        tokenSalvo.setExpiryDate(Instant.now().plusMillis(604_800_000L));
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenReturn(tokenSalvo);

        RefreshToken result = refreshTokenService.createRefreshToken(usuario);

        verify(refreshTokenRepository).deleteByUsuario(usuario);
        verify(refreshTokenRepository).save(any(RefreshToken.class));
        assertThat(result).isNotNull();
        assertThat(result.getUsuario()).isEqualTo(usuario);
    }

    @Test
    void verifyExpiration_tokenValido_retornaToken() {
        RefreshToken token = new RefreshToken();
        token.setExpiryDate(Instant.now().plusSeconds(3600));

        RefreshToken result = refreshTokenService.verifyExpiration(token);

        assertThat(result).isEqualTo(token);
        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    void verifyExpiration_tokenExpirado_deletaELancaExcecao() {
        RefreshToken token = new RefreshToken();
        token.setExpiryDate(Instant.now().minusSeconds(3600));

        assertThatThrownBy(() -> refreshTokenService.verifyExpiration(token))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("expirado");

        verify(refreshTokenRepository).delete(token);
    }

    @Test
    void deleteByUsuario_chamaRepositoryComUsuarioCorreto() {
        Usuario usuario = criarUsuario(1L, "user@email.com");

        refreshTokenService.deleteByUsuario(usuario);

        verify(refreshTokenRepository).deleteByUsuario(usuario);
    }

    private Usuario criarUsuario(Long id, String email) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setEmail(email);
        usuario.setRole(Role.USER);
        usuario.setAtivo(true);
        return usuario;
    }
}
