package com.doban.cadastro_pessoas_docs.domain.cliente;

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

@WebMvcTest(ClienteController.class)
@WithMockUser
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ClienteService clienteService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Test
    void listarTodos_retornaOk() throws Exception {
        when(clienteService.listarTodos()).thenReturn(List.of(criarDTO(1L, "Empresa A")));

        mockMvc.perform(get("/api/v1/clientes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("Empresa A"));
    }

    @Test
    void listarAtivos_retornaOk() throws Exception {
        when(clienteService.listarAtivos()).thenReturn(List.of(criarDTO(1L, "Empresa A")));

        mockMvc.perform(get("/api/v1/clientes/ativos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPorId_encontrado_retornaOk() throws Exception {
        when(clienteService.buscarPorId(1L)).thenReturn(criarDTO(1L, "Empresa A"));

        mockMvc.perform(get("/api/v1/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.nome").value("Empresa A"));
    }

    @Test
    void buscarPorId_naoEncontrado_retorna404() throws Exception {
        when(clienteService.buscarPorId(99L)).thenThrow(new EntityNotFoundException("Cliente 99 nao encontrado"));

        mockMvc.perform(get("/api/v1/clientes/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void criar_payloadValido_retorna201() throws Exception {
        ClienteDTO dto = criarDTO(null, "Novo Cliente");
        ClienteDTO criado = criarDTO(1L, "Novo Cliente");
        when(clienteService.criar(any(ClienteDTO.class))).thenReturn(criado);

        mockMvc.perform(post("/api/v1/clientes").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void atualizar_payloadValido_retornaOk() throws Exception {
        ClienteDTO dto = criarDTO(null, "Atualizado");
        ClienteDTO atualizado = criarDTO(1L, "Atualizado");
        when(clienteService.atualizar(eq(1L), any(ClienteDTO.class))).thenReturn(atualizado);

        mockMvc.perform(put("/api/v1/clientes/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Atualizado"));
    }

    @Test
    void deletar_existente_retorna204() throws Exception {
        doNothing().when(clienteService).deletar(1L);

        mockMvc.perform(delete("/api/v1/clientes/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(clienteService).deletar(1L);
    }

    @Test
    void alternarAtivo_retornaOk() throws Exception {
        ClienteDTO dto = criarDTO(1L, "Empresa A");
        dto.setAtivo(false);
        when(clienteService.alternarAtivo(1L)).thenReturn(dto);

        mockMvc.perform(patch("/api/v1/clientes/1/toggle-ativo").with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ativo").value(false));
    }

    private ClienteDTO criarDTO(Long id, String nome) {
        return ClienteDTO.builder().id(id).nome(nome).ativo(true).build();
    }
}
