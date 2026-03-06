package com.doban.cadastro_pessoas_docs.recurso.tipo;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoRecursoServiceTest {

    @Mock
    private TipoRecursoRepository tipoRecursoRepository;

    @InjectMocks
    private TipoRecursoService tipoRecursoService;

    @Test
    void listarTodos_retornaLista() {
        when(tipoRecursoRepository.findAll(any(Sort.class)))
                .thenReturn(List.of(criarTipoRecurso(1L, "CARRO", "Carro")));

        List<TipoRecursoDTO> resultado = tipoRecursoService.listarTodos();

        assertThat(resultado).hasSize(1);
    }

    @Test
    void listarAtivos_retornaApenasAtivos() {
        when(tipoRecursoRepository.findByAtivoTrueOrderByIdAsc())
                .thenReturn(List.of(criarTipoRecurso(1L, "CELULAR", "Celular")));

        List<TipoRecursoDTO> resultado = tipoRecursoService.listarAtivos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getCodigo()).isEqualTo("CELULAR");
    }

    @Test
    void buscarPorId_encontrado_retornaDTO() {
        when(tipoRecursoRepository.findById(1L))
                .thenReturn(Optional.of(criarTipoRecurso(1L, "CARRO", "Carro")));

        TipoRecursoDTO resultado = tipoRecursoService.buscarPorId(1L);

        assertThat(resultado.getCodigo()).isEqualTo("CARRO");
    }

    @Test
    void buscarPorId_naoEncontrado_lancaEntityNotFoundException() {
        when(tipoRecursoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tipoRecursoService.buscarPorId(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void buscarPorCodigo_encontrado_retornaDTO() {
        when(tipoRecursoRepository.findByCodigo("CARRO"))
                .thenReturn(Optional.of(criarTipoRecurso(1L, "CARRO", "Carro")));

        TipoRecursoDTO resultado = tipoRecursoService.buscarPorCodigo("CARRO");

        assertThat(resultado.getCodigo()).isEqualTo("CARRO");
    }

    @Test
    void buscarPorCodigo_naoEncontrado_lancaEntityNotFoundException() {
        when(tipoRecursoRepository.findByCodigo("INEXISTENTE")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tipoRecursoService.buscarPorCodigo("INEXISTENTE"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void criar_codigoNovo_salvaERetornaDTO() {
        TipoRecursoCreateDTO dto = TipoRecursoCreateDTO.builder()
                .codigo("ROCADEIRA")
                .nome("Rocadeira")
                .build();
        TipoRecurso salvo = criarTipoRecurso(1L, "ROCADEIRA", "Rocadeira");
        when(tipoRecursoRepository.existsByCodigo("ROCADEIRA")).thenReturn(false);
        when(tipoRecursoRepository.save(any(TipoRecurso.class))).thenReturn(salvo);

        TipoRecursoDTO resultado = tipoRecursoService.criar(dto);

        assertThat(resultado.getCodigo()).isEqualTo("ROCADEIRA");
        verify(tipoRecursoRepository).save(any(TipoRecurso.class));
    }

    @Test
    void criar_codigoDuplicado_lancaDataIntegrityViolationException() {
        TipoRecursoCreateDTO dto = TipoRecursoCreateDTO.builder()
                .codigo("CARRO")
                .nome("Carro")
                .build();
        when(tipoRecursoRepository.existsByCodigo("CARRO")).thenReturn(true);

        assertThatThrownBy(() -> tipoRecursoService.criar(dto))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("CARRO");
    }

    @Test
    void desativar_encontrado_setaAtivoFalso() {
        TipoRecurso tipoRecurso = criarTipoRecurso(1L, "CARRO", "Carro");
        tipoRecurso.setAtivo(true);
        when(tipoRecursoRepository.findById(1L)).thenReturn(Optional.of(tipoRecurso));
        when(tipoRecursoRepository.save(any(TipoRecurso.class))).thenAnswer(inv -> inv.getArgument(0));

        tipoRecursoService.desativar(1L);

        assertThat(tipoRecurso.getAtivo()).isFalse();
        verify(tipoRecursoRepository).save(tipoRecurso);
    }

    @Test
    void deletar_existente_deletaSemErros() {
        when(tipoRecursoRepository.existsById(1L)).thenReturn(true);

        tipoRecursoService.deletar(1L);

        verify(tipoRecursoRepository).deleteById(1L);
    }

    @Test
    void deletar_naoExistente_lancaEntityNotFoundException() {
        when(tipoRecursoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> tipoRecursoService.deletar(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private TipoRecurso criarTipoRecurso(Long id, String codigo, String nome) {
        return TipoRecurso.builder().id(id).codigo(codigo).nome(nome).ativo(true).build();
    }
}
