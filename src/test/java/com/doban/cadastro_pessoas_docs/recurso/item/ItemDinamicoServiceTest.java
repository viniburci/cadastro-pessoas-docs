package com.doban.cadastro_pessoas_docs.recurso.item;

import com.doban.cadastro_pessoas_docs.recurso.tipo.TipoRecurso;
import com.doban.cadastro_pessoas_docs.recurso.tipo.TipoRecursoService;
import com.doban.cadastro_pessoas_docs.shared.validation.SchemaValidatorService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ItemDinamicoServiceTest {

    @Mock
    private ItemDinamicoRepository itemDinamicoRepository;

    @Mock
    private TipoRecursoService tipoRecursoService;

    @Mock
    private SchemaValidatorService schemaValidatorService;

    @InjectMocks
    private ItemDinamicoService itemDinamicoService;

    @Test
    void listarTodos_retornaApenasAtivos() {
        when(itemDinamicoRepository.findByAtivoTrueOrderByIdAsc())
                .thenReturn(List.of(criarItem(1L, "CARRO-001")));

        List<ItemDinamicoDTO> resultado = itemDinamicoService.listarTodos();

        assertThat(resultado).hasSize(1);
    }

    @Test
    void buscarPorId_encontrado_retornaDTO() {
        when(itemDinamicoRepository.findById(1L))
                .thenReturn(Optional.of(criarItem(1L, "CARRO-001")));

        ItemDinamicoDTO resultado = itemDinamicoService.buscarPorId(1L);

        assertThat(resultado.getIdentificador()).isEqualTo("CARRO-001");
    }

    @Test
    void buscarPorId_naoEncontrado_lancaEntityNotFoundException() {
        when(itemDinamicoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> itemDinamicoService.buscarPorId(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void buscarEntidadePorId_encontrado_retornaEntidade() {
        ItemDinamico item = criarItem(1L, "CARRO-001");
        when(itemDinamicoRepository.findById(1L)).thenReturn(Optional.of(item));

        ItemDinamico resultado = itemDinamicoService.buscarEntidadePorId(1L);

        assertThat(resultado).isEqualTo(item);
    }

    @Test
    void criar_itemNovo_salvaERetornaDTO() {
        TipoRecurso tipoRecurso = criarTipoRecurso(1L, "CARRO");
        ItemDinamicoCreateDTO dto = ItemDinamicoCreateDTO.builder()
                .tipoRecursoCodigo("CARRO")
                .identificador("CARRO-001")
                .atributos(Map.of("placa", "ABC1234"))
                .build();
        ItemDinamico itemSalvo = criarItem(1L, "CARRO-001");
        itemSalvo.setTipoRecurso(tipoRecurso);

        when(tipoRecursoService.buscarEntidadePorCodigo("CARRO")).thenReturn(tipoRecurso);
        when(itemDinamicoRepository.existsByTipoRecursoIdAndIdentificador(1L, "CARRO-001")).thenReturn(false);
        when(itemDinamicoRepository.save(any(ItemDinamico.class))).thenReturn(itemSalvo);

        ItemDinamicoDTO resultado = itemDinamicoService.criar(dto);

        assertThat(resultado.getIdentificador()).isEqualTo("CARRO-001");
        verify(itemDinamicoRepository).save(any(ItemDinamico.class));
    }

    @Test
    void criar_identificadorDuplicado_lancaDataIntegrityViolationException() {
        TipoRecurso tipoRecurso = criarTipoRecurso(1L, "CARRO");
        ItemDinamicoCreateDTO dto = ItemDinamicoCreateDTO.builder()
                .tipoRecursoCodigo("CARRO")
                .identificador("CARRO-001")
                .build();

        when(tipoRecursoService.buscarEntidadePorCodigo("CARRO")).thenReturn(tipoRecurso);
        when(itemDinamicoRepository.existsByTipoRecursoIdAndIdentificador(1L, "CARRO-001")).thenReturn(true);

        assertThatThrownBy(() -> itemDinamicoService.criar(dto))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("CARRO-001");
    }

    @Test
    void desativar_encontrado_setaAtivoFalso() {
        ItemDinamico item = criarItem(1L, "CARRO-001");
        item.setAtivo(true);
        when(itemDinamicoRepository.findById(1L)).thenReturn(Optional.of(item));
        when(itemDinamicoRepository.save(any(ItemDinamico.class))).thenAnswer(inv -> inv.getArgument(0));

        itemDinamicoService.desativar(1L);

        assertThat(item.getAtivo()).isFalse();
        verify(itemDinamicoRepository).save(item);
    }

    @Test
    void deletar_existente_deletaSemErros() {
        when(itemDinamicoRepository.existsById(1L)).thenReturn(true);

        itemDinamicoService.deletar(1L);

        verify(itemDinamicoRepository).deleteById(1L);
    }

    @Test
    void deletar_naoExistente_lancaEntityNotFoundException() {
        when(itemDinamicoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> itemDinamicoService.deletar(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private ItemDinamico criarItem(Long id, String identificador) {
        TipoRecurso tipo = criarTipoRecurso(1L, "CARRO");
        return ItemDinamico.builder().id(id).identificador(identificador).ativo(true).tipoRecurso(tipo).build();
    }

    private TipoRecurso criarTipoRecurso(Long id, String codigo) {
        return TipoRecurso.builder().id(id).codigo(codigo).nome(codigo).ativo(true).build();
    }
}
