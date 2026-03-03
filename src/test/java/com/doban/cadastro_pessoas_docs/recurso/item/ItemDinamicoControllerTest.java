package com.doban.cadastro_pessoas_docs.recurso.item;

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
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemDinamicoController.class)
@WithMockUser
class ItemDinamicoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ItemDinamicoService itemDinamicoService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @MockitoBean
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Test
    void listarTodos_retornaOk() throws Exception {
        when(itemDinamicoService.listarTodos()).thenReturn(List.of(criarDTO(1L, "CARRO-001")));

        mockMvc.perform(get("/api/v1/itens"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].identificador").value("CARRO-001"));
    }

    @Test
    void listarPorTipo_retornaOk() throws Exception {
        when(itemDinamicoService.listarPorTipo("CARRO")).thenReturn(List.of(criarDTO(1L, "CARRO-001")));

        mockMvc.perform(get("/api/v1/itens/tipo/CARRO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void listarDisponiveis_retornaOk() throws Exception {
        when(itemDinamicoService.listarDisponiveis("CARRO")).thenReturn(List.of(criarDTO(1L, "CARRO-001")));

        mockMvc.perform(get("/api/v1/itens/disponiveis/CARRO"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void buscarPorId_encontrado_retornaOk() throws Exception {
        when(itemDinamicoService.buscarPorId(1L)).thenReturn(criarDTO(1L, "CARRO-001"));

        mockMvc.perform(get("/api/v1/itens/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.identificador").value("CARRO-001"));
    }

    @Test
    void buscarPorId_naoEncontrado_retorna404() throws Exception {
        when(itemDinamicoService.buscarPorId(99L)).thenThrow(new EntityNotFoundException("Item 99 nao encontrado"));

        mockMvc.perform(get("/api/v1/itens/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void criar_payloadValido_retorna201() throws Exception {
        ItemDinamicoCreateDTO createDTO = ItemDinamicoCreateDTO.builder()
                .tipoRecursoCodigo("CARRO")
                .identificador("CARRO-001")
                .atributos(Map.of("placa", "ABC1234"))
                .build();
        when(itemDinamicoService.criar(any(ItemDinamicoCreateDTO.class))).thenReturn(criarDTO(1L, "CARRO-001"));

        mockMvc.perform(post("/api/v1/itens").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void atualizar_payloadValido_retornaOk() throws Exception {
        ItemDinamicoUpdateDTO updateDTO = ItemDinamicoUpdateDTO.builder()
                .identificador("CARRO-001-ATUALIZADO")
                .build();
        ItemDinamicoDTO atualizado = criarDTO(1L, "CARRO-001-ATUALIZADO");
        when(itemDinamicoService.atualizar(eq(1L), any(ItemDinamicoUpdateDTO.class))).thenReturn(atualizado);

        mockMvc.perform(put("/api/v1/itens/1").with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identificador").value("CARRO-001-ATUALIZADO"));
    }

    @Test
    void desativar_existente_retorna204() throws Exception {
        doNothing().when(itemDinamicoService).desativar(1L);

        mockMvc.perform(delete("/api/v1/itens/1").with(csrf()))
                .andExpect(status().isNoContent());

        verify(itemDinamicoService).desativar(1L);
    }

    @Test
    void deletar_existente_retorna204() throws Exception {
        doNothing().when(itemDinamicoService).deletar(1L);

        mockMvc.perform(delete("/api/v1/itens/1/permanente").with(csrf()))
                .andExpect(status().isNoContent());

        verify(itemDinamicoService).deletar(1L);
    }

    private ItemDinamicoDTO criarDTO(Long id, String identificador) {
        return ItemDinamicoDTO.builder()
                .id(id)
                .tipoRecursoId(1L)
                .tipoRecursoCodigo("CARRO")
                .identificador(identificador)
                .ativo(true)
                .build();
    }
}
