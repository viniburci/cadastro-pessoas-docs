package com.doban.cadastro_pessoas_docs.domain.vaga.tipo;

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
class TipoVagaServiceTest {

    @Mock
    private TipoVagaRepository tipoVagaRepository;

    @InjectMocks
    private TipoVagaService tipoVagaService;

    @Test
    void listarTodos_retornaLista() {
        when(tipoVagaRepository.findAll(any(Sort.class)))
                .thenReturn(List.of(criarTipoVaga(1L, "OPERADOR", "Operador")));

        List<TipoVagaDTO> resultado = tipoVagaService.listarTodos();

        assertThat(resultado).hasSize(1);
    }

    @Test
    void listarAtivos_retornaApenasAtivos() {
        when(tipoVagaRepository.findByAtivoTrueOrderByIdAsc())
                .thenReturn(List.of(criarTipoVaga(1L, "OPERADOR", "Operador")));

        List<TipoVagaDTO> resultado = tipoVagaService.listarAtivos();

        assertThat(resultado).hasSize(1);
    }

    @Test
    void buscarPorId_encontrado_retornaDTO() {
        when(tipoVagaRepository.findById(1L))
                .thenReturn(Optional.of(criarTipoVaga(1L, "OPERADOR", "Operador")));

        TipoVagaDTO resultado = tipoVagaService.buscarPorId(1L);

        assertThat(resultado.getCodigo()).isEqualTo("OPERADOR");
    }

    @Test
    void buscarPorId_naoEncontrado_lancaEntityNotFoundException() {
        when(tipoVagaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tipoVagaService.buscarPorId(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void buscarPorCodigo_encontrado_retornaDTO() {
        when(tipoVagaRepository.findByCodigo("OPERADOR"))
                .thenReturn(Optional.of(criarTipoVaga(1L, "OPERADOR", "Operador")));

        TipoVagaDTO resultado = tipoVagaService.buscarPorCodigo("OPERADOR");

        assertThat(resultado.getCodigo()).isEqualTo("OPERADOR");
    }

    @Test
    void buscarPorCodigo_naoEncontrado_lancaEntityNotFoundException() {
        when(tipoVagaRepository.findByCodigo("INEXISTENTE")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> tipoVagaService.buscarPorCodigo("INEXISTENTE"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void criar_codigoNovo_salvaERetornaDTO() {
        TipoVagaCreateDTO dto = TipoVagaCreateDTO.builder()
                .codigo("MOTORISTA")
                .nome("Motorista")
                .build();
        TipoVaga salvo = criarTipoVaga(1L, "MOTORISTA", "Motorista");
        when(tipoVagaRepository.existsByCodigo("MOTORISTA")).thenReturn(false);
        when(tipoVagaRepository.save(any(TipoVaga.class))).thenReturn(salvo);

        TipoVagaDTO resultado = tipoVagaService.criar(dto);

        assertThat(resultado.getCodigo()).isEqualTo("MOTORISTA");
        verify(tipoVagaRepository).save(any(TipoVaga.class));
    }

    @Test
    void criar_codigoDuplicado_lancaDataIntegrityViolationException() {
        TipoVagaCreateDTO dto = TipoVagaCreateDTO.builder()
                .codigo("MOTORISTA")
                .nome("Motorista")
                .build();
        when(tipoVagaRepository.existsByCodigo("MOTORISTA")).thenReturn(true);

        assertThatThrownBy(() -> tipoVagaService.criar(dto))
                .isInstanceOf(DataIntegrityViolationException.class)
                .hasMessageContaining("MOTORISTA");
    }

    @Test
    void desativar_encontrado_setaAtivoFalso() {
        TipoVaga tipoVaga = criarTipoVaga(1L, "OPERADOR", "Operador");
        tipoVaga.setAtivo(true);
        when(tipoVagaRepository.findById(1L)).thenReturn(Optional.of(tipoVaga));
        when(tipoVagaRepository.save(any(TipoVaga.class))).thenAnswer(inv -> inv.getArgument(0));

        tipoVagaService.desativar(1L);

        assertThat(tipoVaga.getAtivo()).isFalse();
        verify(tipoVagaRepository).save(tipoVaga);
    }

    @Test
    void deletar_existente_deletaSemErros() {
        when(tipoVagaRepository.existsById(1L)).thenReturn(true);

        tipoVagaService.deletar(1L);

        verify(tipoVagaRepository).deleteById(1L);
    }

    @Test
    void deletar_naoExistente_lancaEntityNotFoundException() {
        when(tipoVagaRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> tipoVagaService.deletar(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void buscarOuCriarPorNome_existente_retornaExistente() {
        TipoVaga existente = criarTipoVaga(1L, "OPERADOR", "Operador");
        when(tipoVagaRepository.findByNome("Operador")).thenReturn(Optional.of(existente));

        TipoVaga resultado = tipoVagaService.buscarOuCriarPorNome("Operador");

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(tipoVagaRepository, never()).save(any());
    }

    @Test
    void buscarOuCriarPorNome_nomeNulo_retornaNulo() {
        TipoVaga resultado = tipoVagaService.buscarOuCriarPorNome(null);
        assertThat(resultado).isNull();
    }

    @Test
    void buscarOuCriarPorNome_nomeNovo_criaNovo() {
        TipoVaga novo = criarTipoVaga(2L, "SOLDADOR", "Soldador");
        when(tipoVagaRepository.findByNome("Soldador")).thenReturn(Optional.empty());
        when(tipoVagaRepository.save(any(TipoVaga.class))).thenReturn(novo);

        TipoVaga resultado = tipoVagaService.buscarOuCriarPorNome("Soldador");

        assertThat(resultado.getCodigo()).isEqualTo("SOLDADOR");
        verify(tipoVagaRepository).save(any(TipoVaga.class));
    }

    private TipoVaga criarTipoVaga(Long id, String codigo, String nome) {
        return TipoVaga.builder().id(id).codigo(codigo).nome(nome).ativo(true).build();
    }
}
