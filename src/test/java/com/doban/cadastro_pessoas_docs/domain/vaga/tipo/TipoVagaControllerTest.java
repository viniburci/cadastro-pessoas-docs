package com.doban.cadastro_pessoas_docs.domain.vaga.tipo;

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
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TipoVagaController.class)
@WithMockUser
class TipoVagaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TipoVagaService tipoVagaService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Test
    void listarTodos_retornaOk() throws Exception {
        when(tipoVagaService.listarTodos()).thenReturn(List.of(criarDTO(1L, "CLT")));

        mockMvc.perform(get("/api/v1/tipos-vaga"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("CLT"));
    }

    @Test
    void listarAtivos_retornaOk() throws Exception {
        when(tipoVagaService.listarAtivos()).thenReturn(List.of(criarDTO(1L, "CLT")));

        mockMvc.perform(get("/api/v1/tipos-vaga/ativos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPorId_encontrado_retornaOk() throws Exception {
        when(tipoVagaService.buscarPorId(1L)).thenReturn(criarDTO(1L, "CLT"));

        mockMvc.perform(get("/api/v1/tipos-vaga/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.codigo").value("CLT"));
    }

    @Test
    void buscarPorId_naoEncontrado_retorna404() throws Exception {
        when(tipoVagaService.buscarPorId(99L)).thenThrow(new EntityNotFoundException("TipoVaga 99 nao encontrado"));

        mockMvc.perform(get("/api/v1/tipos-vaga/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void buscarPorCodigo_encontrado_retornaOk() throws Exception {
        when(tipoVagaService.buscarPorCodigo("CLT")).thenReturn(criarDTO(1L, "CLT"));

        mockMvc.perform(get("/api/v1/tipos-vaga/codigo/CLT"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value("CLT"));
    }

    @Test
    void criar_payloadValido_retorna201() throws Exception {
        TipoVagaCreateDTO createDTO = TipoVagaCreateDTO.builder()
                .codigo("CLT")
                .nome("Contrato CLT")
                .build();
        when(tipoVagaService.criar(any(TipoVagaCreateDTO.class))).thenReturn(criarDTO(1L, "CLT"));

        mockMvc.perform(post("/api/v1/tipos-vaga").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void desativar_existente_retorna204() throws Exception {
        doNothing().when(tipoVagaService).desativar(1L);

        mockMvc.perform(delete("/api/v1/tipos-vaga/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(tipoVagaService).desativar(1L);
    }

    @Test
    void deletar_existente_retorna204() throws Exception {
        doNothing().when(tipoVagaService).deletar(1L);

        mockMvc.perform(delete("/api/v1/tipos-vaga/1/permanente").with(csrf()))
                .andExpect(status().isNoContent());

        verify(tipoVagaService).deletar(1L);
    }

    private TipoVagaDTO criarDTO(Long id, String codigo) {
        return TipoVagaDTO.builder().id(id).codigo(codigo).nome(codigo).ativo(true).build();
    }
}
