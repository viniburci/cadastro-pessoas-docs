package com.doban.cadastro_pessoas_docs.recurso.dinamico;

import com.doban.cadastro_pessoas_docs.domain.pessoa.Pessoa;
import com.doban.cadastro_pessoas_docs.domain.pessoa.PessoaRepository;
import com.doban.cadastro_pessoas_docs.recurso.item.ItemDinamico;
import com.doban.cadastro_pessoas_docs.recurso.item.ItemDinamicoService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecursoDinamicoServiceTest {

    @Mock
    private RecursoDinamicoRepository recursoDinamicoRepository;

    @Mock
    private ItemDinamicoService itemDinamicoService;

    @Mock
    private PessoaRepository pessoaRepository;

    @InjectMocks
    private RecursoDinamicoService recursoDinamicoService;

    @Test
    void listarTodos_retornaLista() {
        when(recursoDinamicoRepository.findAll(any(org.springframework.data.domain.Sort.class)))
                .thenReturn(List.of(criarRecurso(1L)));

        List<RecursoDinamicoDTO> resultado = recursoDinamicoService.listarTodos();

        assertThat(resultado).hasSize(1);
    }

    @Test
    void listarPorPessoa_retornaRecursosDaPessoa() {
        when(recursoDinamicoRepository.findByPessoaIdOrderByIdAsc(1L))
                .thenReturn(List.of(criarRecurso(1L), criarRecurso(2L)));

        List<RecursoDinamicoDTO> resultado = recursoDinamicoService.listarPorPessoa(1L);

        assertThat(resultado).hasSize(2);
    }

    @Test
    void buscarPorId_encontrado_retornaDTO() {
        when(recursoDinamicoRepository.findById(1L)).thenReturn(Optional.of(criarRecurso(1L)));

        RecursoDinamicoDTO resultado = recursoDinamicoService.buscarPorId(1L);

        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    void buscarPorId_naoEncontrado_lancaEntityNotFoundException() {
        when(recursoDinamicoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recursoDinamicoService.buscarPorId(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void emprestar_itemDisponivel_salvaERetornaDTO() {
        Pessoa pessoa = criarPessoa(1L);
        ItemDinamico item = criarItem(10L);
        RecursoDinamicoCreateDTO dto = RecursoDinamicoCreateDTO.builder()
                .pessoaId(1L)
                .itemId(10L)
                .dataEntrega(LocalDate.now())
                .build();
        RecursoDinamico recursoSalvo = criarRecurso(1L);

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(itemDinamicoService.buscarEntidadePorId(10L)).thenReturn(item);
        when(recursoDinamicoRepository.existsByItemIdAndDataDevolucaoIsNull(10L)).thenReturn(false);
        when(recursoDinamicoRepository.save(any(RecursoDinamico.class))).thenReturn(recursoSalvo);

        RecursoDinamicoDTO resultado = recursoDinamicoService.emprestar(dto);

        assertThat(resultado).isNotNull();
        verify(recursoDinamicoRepository).save(any(RecursoDinamico.class));
    }

    @Test
    void emprestar_itemJaEmprestado_lancaIllegalArgumentException() {
        Pessoa pessoa = criarPessoa(1L);
        ItemDinamico item = criarItem(10L);
        RecursoDinamicoCreateDTO dto = RecursoDinamicoCreateDTO.builder()
                .pessoaId(1L)
                .itemId(10L)
                .dataEntrega(LocalDate.now())
                .build();

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(itemDinamicoService.buscarEntidadePorId(10L)).thenReturn(item);
        when(recursoDinamicoRepository.existsByItemIdAndDataDevolucaoIsNull(10L)).thenReturn(true);

        assertThatThrownBy(() -> recursoDinamicoService.emprestar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("já está emprestado");
    }

    @Test
    void emprestar_itemInativo_lancaIllegalArgumentException() {
        Pessoa pessoa = criarPessoa(1L);
        ItemDinamico item = criarItem(10L);
        item.setAtivo(false);
        RecursoDinamicoCreateDTO dto = RecursoDinamicoCreateDTO.builder()
                .pessoaId(1L)
                .itemId(10L)
                .dataEntrega(LocalDate.now())
                .build();

        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));
        when(itemDinamicoService.buscarEntidadePorId(10L)).thenReturn(item);
        when(recursoDinamicoRepository.existsByItemIdAndDataDevolucaoIsNull(10L)).thenReturn(false);

        assertThatThrownBy(() -> recursoDinamicoService.emprestar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("inativo");
    }

    @Test
    void registrarDevolucao_dataValida_atualizaERetorna() {
        LocalDate dataEntrega = LocalDate.now().minusDays(5);
        LocalDate dataDevolucao = LocalDate.now();
        RecursoDinamico recurso = criarRecurso(1L);
        recurso.setDataEntrega(dataEntrega);
        DevolucaoDTO dto = new DevolucaoDTO(dataDevolucao);

        when(recursoDinamicoRepository.findById(1L)).thenReturn(Optional.of(recurso));
        when(recursoDinamicoRepository.save(any(RecursoDinamico.class))).thenAnswer(inv -> inv.getArgument(0));

        RecursoDinamicoDTO resultado = recursoDinamicoService.registrarDevolucao(1L, dto);

        assertThat(resultado.getDataDevolucao()).isEqualTo(dataDevolucao);
    }

    @Test
    void registrarDevolucao_dataAnteriorAEntrega_lancaIllegalArgumentException() {
        LocalDate dataEntrega = LocalDate.now();
        LocalDate dataDevolucao = dataEntrega.minusDays(1);
        RecursoDinamico recurso = criarRecurso(1L);
        recurso.setDataEntrega(dataEntrega);
        DevolucaoDTO dto = new DevolucaoDTO(dataDevolucao);

        when(recursoDinamicoRepository.findById(1L)).thenReturn(Optional.of(recurso));

        assertThatThrownBy(() -> recursoDinamicoService.registrarDevolucao(1L, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("anterior");
    }

    @Test
    void deletar_existente_deletaSemErros() {
        when(recursoDinamicoRepository.existsById(1L)).thenReturn(true);

        recursoDinamicoService.deletar(1L);

        verify(recursoDinamicoRepository).deleteById(1L);
    }

    @Test
    void deletar_naoExistente_lancaEntityNotFoundException() {
        when(recursoDinamicoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> recursoDinamicoService.deletar(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private Pessoa criarPessoa(Long id) {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(id);
        pessoa.setNome("Pessoa " + id);
        return pessoa;
    }

    private ItemDinamico criarItem(Long id) {
        com.doban.cadastro_pessoas_docs.recurso.tipo.TipoRecurso tipo =
                com.doban.cadastro_pessoas_docs.recurso.tipo.TipoRecurso.builder()
                        .id(1L).codigo("CARRO").nome("Carro").ativo(true).build();
        return ItemDinamico.builder().id(id).identificador("ITEM-" + id).ativo(true).tipoRecurso(tipo).build();
    }

    private RecursoDinamico criarRecurso(Long id) {
        RecursoDinamico recurso = new RecursoDinamico();
        recurso.setId(id);
        recurso.setPessoa(criarPessoa(id));
        recurso.setItem(criarItem(id));
        return recurso;
    }
}
