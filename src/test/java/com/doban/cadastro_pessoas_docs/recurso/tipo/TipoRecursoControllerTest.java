package com.doban.cadastro_pessoas_docs.recurso.tipo;

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

@WebMvcTest(TipoRecursoController.class)
@WithMockUser
class TipoRecursoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TipoRecursoService tipoRecursoService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Test
    void listarTodos_retornaOk() throws Exception {
        when(tipoRecursoService.listarTodos()).thenReturn(List.of(criarDTO(1L, "CARRO")));

        mockMvc.perform(get("/api/v1/tipos-recurso"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("CARRO"));
    }

    @Test
    void listarAtivos_retornaOk() throws Exception {
        when(tipoRecursoService.listarAtivos()).thenReturn(List.of(criarDTO(1L, "CARRO")));

        mockMvc.perform(get("/api/v1/tipos-recurso/ativos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPorId_encontrado_retornaOk() throws Exception {
        when(tipoRecursoService.buscarPorId(1L)).thenReturn(criarDTO(1L, "CARRO"));

        mockMvc.perform(get("/api/v1/tipos-recurso/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codigo").value("CARRO"));
    }

    @Test
    void buscarPorId_naoEncontrado_retorna404() throws Exception {
        when(tipoRecursoService.buscarPorId(99L)).thenThrow(new EntityNotFoundException("TipoRecurso 99 nao encontrado"));

        mockMvc.perform(get("/api/v1/tipos-recurso/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorCodigo_encontrado_retornaOk() throws Exception {
        when(tipoRecursoService.buscarPorCodigo("CARRO")).thenReturn(criarDTO(1L, "CARRO"));

        mockMvc.perform(get("/api/v1/tipos-recurso/codigo/CARRO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("CARRO"));
    }

    @Test
    void criar_payloadValido_retorna201() throws Exception {
        TipoRecursoCreateDTO createDTO = TipoRecursoCreateDTO.builder()
                .codigo("CARRO")
                .nome("Carro")
                .build();
        when(tipoRecursoService.criar(any(TipoRecursoCreateDTO.class))).thenReturn(criarDTO(1L, "CARRO"));

        mockMvc.perform(post("/api/v1/tipos-recurso").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void atualizar_payloadValido_retornaOk() throws Exception {
        TipoRecursoUpdateDTO updateDTO = TipoRecursoUpdateDTO.builder().nome("Carro Atualizado").build();
        TipoRecursoDTO atualizado = criarDTO(1L, "CARRO");
        atualizado.setNome("Carro Atualizado");
        when(tipoRecursoService.atualizar(eq(1L), any(TipoRecursoUpdateDTO.class))).thenReturn(atualizado);

        mockMvc.perform(put("/api/v1/tipos-recurso/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Carro Atualizado"));
    }

    @Test
    void desativar_existente_retorna204() throws Exception {
        doNothing().when(tipoRecursoService).desativar(1L);

        mockMvc.perform(delete("/api/v1/tipos-recurso/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(tipoRecursoService).desativar(1L);
    }

    @Test
    void deletar_existente_retorna204() throws Exception {
        doNothing().when(tipoRecursoService).deletar(1L);

        mockMvc.perform(delete("/api/v1/tipos-recurso/1/permanente").with(csrf()))
                .andExpect(status().isNoContent());

        verify(tipoRecursoService).deletar(1L);
    }

    private TipoRecursoDTO criarDTO(Long id, String codigo) {
        return TipoRecursoDTO.builder().id(id).codigo(codigo).nome(codigo).ativo(true).build();
    }
}
