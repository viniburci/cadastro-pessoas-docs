package com.doban.cadastro_pessoas_docs.domain.vaga;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.doban.cadastro_pessoas_docs.security.JwtService;
import com.doban.cadastro_pessoas_docs.security.OAuth2AuthenticationSuccessHandler;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VagaController.class)
@WithMockUser
class VagaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private VagaService vagaService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Test
    void obterVagasPorPessoa_retornaOk() throws Exception {
        when(vagaService.obterVagasPorPessoa(1L)).thenReturn(List.of(criarDTO(1L, 1L)));

        mockMvc.perform(get("/api/v1/vaga/pessoa/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void obterVagaMaisRecentePorPessoa_encontrada_retornaOk() throws Exception {
        when(vagaService.obterVagaMaisRecentePorPessoa(1L)).thenReturn(criarDTO(1L, 1L));

        mockMvc.perform(get("/api/v1/vaga/mais-recente/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pessoaId").value(1));
    }

    @Test
    void obterVagaMaisRecentePorPessoa_naoEncontrada_retorna404() throws Exception {
        when(vagaService.obterVagaMaisRecentePorPessoa(99L))
                .thenThrow(new EntityNotFoundException("Nenhuma vaga encontrada para pessoa 99"));

        mockMvc.perform(get("/api/v1/vaga/mais-recente/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void criarVagaParaPessoa_payloadValido_retornaOk() throws Exception {
        VagaDTO dto = criarDTO(null, 1L);
        VagaDTO criada = criarDTO(1L, 1L);
        when(vagaService.criarVaga(eq(1L), any(VagaDTO.class))).thenReturn(criada);

        mockMvc.perform(post("/api/v1/vaga/criar/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void atualizarVaga_payloadValido_retornaOk() throws Exception {
        VagaDTO dto = criarDTO(null, 1L);
        VagaDTO atualizada = criarDTO(1L, 1L);
        when(vagaService.atualizarVaga(eq(1L), any(VagaDTO.class))).thenReturn(atualizada);

        mockMvc.perform(put("/api/v1/vaga/atualizar/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deletarVaga_existente_retorna204() throws Exception {
        doNothing().when(vagaService).deletarVaga(1L);

        mockMvc.perform(delete("/api/v1/vaga/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(vagaService).deletarVaga(1L);
    }

    private VagaDTO criarDTO(Long id, Long pessoaId) {
        return VagaDTO.builder().id(id).pessoaId(pessoaId).build();
    }
}
