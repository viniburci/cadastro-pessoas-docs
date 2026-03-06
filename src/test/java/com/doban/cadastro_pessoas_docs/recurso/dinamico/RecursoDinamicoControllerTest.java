package com.doban.cadastro_pessoas_docs.recurso.dinamico;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.doban.cadastro_pessoas_docs.recurso.item.ItemDinamicoDTO;
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

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecursoDinamicoController.class)
@WithMockUser
class RecursoDinamicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RecursoDinamicoService recursoDinamicoService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Test
    void listarTodos_retornaOk() throws Exception {
        when(recursoDinamicoService.listarTodos()).thenReturn(List.of(criarDTO(1L)));

        mockMvc.perform(get("/api/v1/recursos-dinamicos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void listarPorPessoa_retornaOk() throws Exception {
        when(recursoDinamicoService.listarPorPessoa(1L)).thenReturn(List.of(criarDTO(1L), criarDTO(2L)));

        mockMvc.perform(get("/api/v1/recursos-dinamicos/pessoa/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    void buscarPorId_encontrado_retornaOk() throws Exception {
        when(recursoDinamicoService.buscarPorId(1L)).thenReturn(criarDTO(1L));

        mockMvc.perform(get("/api/v1/recursos-dinamicos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void buscarPorId_naoEncontrado_retorna404() throws Exception {
        when(recursoDinamicoService.buscarPorId(99L))
                .thenThrow(new EntityNotFoundException("Recurso 99 nao encontrado"));

        mockMvc.perform(get("/api/v1/recursos-dinamicos/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void emprestar_payloadValido_retorna201() throws Exception {
        RecursoDinamicoCreateDTO createDTO = RecursoDinamicoCreateDTO.builder()
                .pessoaId(1L)
                .itemId(10L)
                .dataEntrega(LocalDate.now())
                .build();
        when(recursoDinamicoService.emprestar(any(RecursoDinamicoCreateDTO.class))).thenReturn(criarDTO(1L));

        mockMvc.perform(post("/api/v1/recursos-dinamicos").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void registrarDevolucao_dataValida_retornaOk() throws Exception {
        DevolucaoDTO dto = new DevolucaoDTO(LocalDate.now());
        RecursoDinamicoDTO atualizado = criarDTO(1L);
        atualizado.setDataDevolucao(LocalDate.now());
        when(recursoDinamicoService.registrarDevolucao(eq(1L), any(DevolucaoDTO.class))).thenReturn(atualizado);

        mockMvc.perform(put("/api/v1/recursos-dinamicos/1/devolucao").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataDevolucao").isNotEmpty());
    }

    @Test
    void deletar_existente_retorna204() throws Exception {
        doNothing().when(recursoDinamicoService).deletar(1L);

        mockMvc.perform(delete("/api/v1/recursos-dinamicos/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(recursoDinamicoService).deletar(1L);
    }

    private RecursoDinamicoDTO criarDTO(Long id) {
        ItemDinamicoDTO item = ItemDinamicoDTO.builder()
                .id(10L)
                .tipoRecursoId(1L)
                .tipoRecursoCodigo("CARRO")
                .identificador("CARRO-001")
                .ativo(true)
                .build();
        return RecursoDinamicoDTO.builder()
                .id(id)
                .pessoaId(1L)
                .pessoaNome("Pessoa " + id)
                .item(item)
                .dataEntrega(LocalDate.now())
                .build();
    }
}
