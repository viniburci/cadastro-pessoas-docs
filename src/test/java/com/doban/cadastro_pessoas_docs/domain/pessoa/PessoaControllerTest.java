package com.doban.cadastro_pessoas_docs.domain.pessoa;

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

@WebMvcTest(PessoaController.class)
@WithMockUser
class PessoaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PessoaService pessoaService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Test
    void buscarTodasPessoas_comResultados_retornaOk() throws Exception {
        when(pessoaService.buscarTodasPessoas()).thenReturn(List.of(criarDTO(1L, "Joao")));

        mockMvc.perform(get("/api/v1/pessoa"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Joao"));
    }

    @Test
    void buscarTodasPessoas_semResultados_retorna204() throws Exception {
        when(pessoaService.buscarTodasPessoas()).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/pessoa"))
                .andExpect(status().isNoContent());
    }

    @Test
    void buscarPessoasAtivas_comResultados_retornaOk() throws Exception {
        when(pessoaService.buscarPessoasAtivas()).thenReturn(List.of(criarDTO(1L, "Joao")));

        mockMvc.perform(get("/api/v1/pessoa/ativas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPessoa_encontrada_retornaOk() throws Exception {
        when(pessoaService.buscarPessoaPorId(1L)).thenReturn(criarDTO(1L, "Joao"));

        mockMvc.perform(get("/api/v1/pessoa/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Joao"));
    }

    @Test
    void buscarPessoa_naoEncontrada_retorna404() throws Exception {
        when(pessoaService.buscarPessoaPorId(99L)).thenThrow(new EntityNotFoundException("Pessoa 99 nao encontrada"));

        mockMvc.perform(get("/api/v1/pessoa/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void criarPessoa_payloadValido_retornaOk() throws Exception {
        PessoaDTO dto = criarDTO(null, "Maria");
        PessoaDTO salva = criarDTO(1L, "Maria");
        when(pessoaService.salvarPessoa(any(PessoaDTO.class))).thenReturn(salva);

        mockMvc.perform(post("/api/v1/pessoa").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void atualizarPessoa_payloadValido_retornaOk() throws Exception {
        PessoaDTO dto = criarDTO(null, "Maria Atualizada");
        PessoaDTO atualizada = criarDTO(1L, "Maria Atualizada");
        when(pessoaService.atualizarPessoa(eq(1L), any(PessoaDTO.class))).thenReturn(atualizada);

        mockMvc.perform(put("/api/v1/pessoa/atualizar/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Maria Atualizada"));
    }

    @Test
    void deletarPessoa_existente_retorna204() throws Exception {
        doNothing().when(pessoaService).deletarPessoa(1L);

        mockMvc.perform(delete("/api/v1/pessoa/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(pessoaService).deletarPessoa(1L);
    }

    private PessoaDTO criarDTO(Long id, String nome) {
        return PessoaDTO.builder().id(id).nome(nome).build();
    }
}
