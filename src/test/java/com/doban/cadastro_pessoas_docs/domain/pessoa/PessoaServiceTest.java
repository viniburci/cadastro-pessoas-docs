package com.doban.cadastro_pessoas_docs.domain.pessoa;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PessoaServiceTest {

    @Mock
    private PessoaRepository pessoaRepository;

    @Mock
    private DadosBancariosService dadosBancariosService;

    @InjectMocks
    private PessoaService pessoaService;

    @Test
    void buscarTodasPessoas_retornaLista() {
        when(pessoaRepository.findAll(any(Sort.class)))
                .thenReturn(List.of(criarPessoa(1L, "Joao"), criarPessoa(2L, "Maria")));

        List<PessoaDTO> resultado = pessoaService.buscarTodasPessoas();

        assertThat(resultado).hasSize(2);
    }

    @Test
    void buscarPessoaPorId_encontrada_retornaDTO() {
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(criarPessoa(1L, "Joao")));

        PessoaDTO resultado = pessoaService.buscarPessoaPorId(1L);

        assertThat(resultado.getNome()).isEqualTo("Joao");
        assertThat(resultado.getId()).isEqualTo(1L);
    }

    @Test
    void buscarPessoaPorId_naoEncontrada_lancaEntityNotFoundException() {
        when(pessoaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> pessoaService.buscarPessoaPorId(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Pessoa não encontrada");
    }

    @Test
    void buscarEntidadePessoaPorId_encontrada_retornaEntidade() {
        Pessoa pessoa = criarPessoa(1L, "Joao");
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));

        Pessoa resultado = pessoaService.buscarEntidadePessoaPorId(1L);

        assertThat(resultado).isEqualTo(pessoa);
    }

    @Test
    void salvarPessoa_semDadosBancarios_salvaSomenteAPessoa() {
        PessoaDTO dto = PessoaDTO.builder().nome("Joao").cpf("12345678901").build();
        Pessoa pessoaSalva = criarPessoa(1L, "Joao");
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaSalva);

        PessoaDTO resultado = pessoaService.salvarPessoa(dto);

        assertThat(resultado.getNome()).isEqualTo("Joao");
        verify(dadosBancariosService, never()).salvar(any(), any());
    }

    @Test
    void atualizarPessoa_encontrada_atualizaERetorna() {
        Pessoa pessoaExistente = criarPessoa(1L, "Nome Antigo");
        PessoaDTO dto = PessoaDTO.builder().nome("Nome Novo").cpf("12345678901").build();
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoaExistente));
        when(pessoaRepository.save(any(Pessoa.class))).thenAnswer(inv -> inv.getArgument(0));

        PessoaDTO resultado = pessoaService.atualizarPessoa(1L, dto);

        assertThat(resultado.getNome()).isEqualTo("Nome Novo");
    }

    @Test
    void atualizarPessoa_idDTODivergenteDoParametro_lancaIllegalArgumentException() {
        PessoaDTO dto = PessoaDTO.builder().id(99L).nome("Nome").build();

        assertThatThrownBy(() -> pessoaService.atualizarPessoa(1L, dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ID");
    }

    @Test
    void deletarPessoa_chamaDeleteById() {
        pessoaService.deletarPessoa(1L);

        verify(pessoaRepository).deleteById(1L);
    }

    @Test
    void salvarFoto_fotoVazia_lancaIllegalArgumentException() {
        assertThatThrownBy(() -> pessoaService.salvarFoto(1L, new byte[0]))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("vazia");
    }

    @Test
    void salvarFoto_fotoMuitoGrande_lancaIllegalArgumentException() {
        byte[] fotoGrande = new byte[5_000_001];

        assertThatThrownBy(() -> pessoaService.salvarFoto(1L, fotoGrande))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("5MB");
    }

    @Test
    void buscarFoto_pessoaSemFoto_lancaEntityNotFoundException() {
        Pessoa pessoa = criarPessoa(1L, "Joao");
        pessoa.setFoto(null);
        when(pessoaRepository.findById(1L)).thenReturn(Optional.of(pessoa));

        assertThatThrownBy(() -> pessoaService.buscarFoto(1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Foto não encontrada");
    }

    private Pessoa criarPessoa(Long id, String nome) {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(id);
        pessoa.setNome(nome);
        return pessoa;
    }
}
