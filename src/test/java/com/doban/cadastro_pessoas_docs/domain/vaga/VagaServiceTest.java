package com.doban.cadastro_pessoas_docs.domain.vaga;

import com.doban.cadastro_pessoas_docs.domain.cliente.Cliente;
import com.doban.cadastro_pessoas_docs.domain.cliente.ClienteRepository;
import com.doban.cadastro_pessoas_docs.domain.pessoa.Pessoa;
import com.doban.cadastro_pessoas_docs.domain.pessoa.PessoaService;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VagaServiceTest {

    @Mock
    private VagaRepository vagaRepository;

    @Mock
    private PessoaService pessoaService;

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private VagaService vagaService;

    @Test
    void obterVagasPorPessoa_comVagas_retornaLista() {
        Pessoa pessoa = criarPessoa(1L);
        Vaga vaga = criarVaga(1L, pessoa);
        when(vagaRepository.findByPessoaId(1L)).thenReturn(Optional.of(List.of(vaga)));

        List<VagaDTO> resultado = vagaService.obterVagasPorPessoa(1L);

        assertThat(resultado).hasSize(1);
    }

    @Test
    void obterVagasPorPessoa_semVagas_retornaListaVazia() {
        when(vagaRepository.findByPessoaId(1L)).thenReturn(Optional.empty());

        List<VagaDTO> resultado = vagaService.obterVagasPorPessoa(1L);

        assertThat(resultado).isEmpty();
    }

    @Test
    void obterVagaPorId_encontrada_retornaDTO() {
        Pessoa pessoa = criarPessoa(1L);
        Vaga vaga = criarVaga(10L, pessoa);
        when(vagaRepository.findById(10L)).thenReturn(Optional.of(vaga));

        VagaDTO resultado = vagaService.obterVagaPorId(10L);

        assertThat(resultado.getId()).isEqualTo(10L);
    }

    @Test
    void obterVagaPorId_naoEncontrada_lancaEntityNotFoundException() {
        when(vagaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vagaService.obterVagaPorId(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void criarVaga_dadosValidos_salvaeRetornaDTO() {
        Pessoa pessoa = criarPessoa(1L);
        VagaDTO dto = VagaDTO.builder()
                .salario(BigDecimal.valueOf(3000))
                .cidade("Sao Paulo")
                .build();
        Vaga vagaSalva = criarVaga(1L, pessoa);
        when(pessoaService.buscarEntidadePessoaPorId(1L)).thenReturn(pessoa);
        when(vagaRepository.save(any(Vaga.class))).thenReturn(vagaSalva);

        VagaDTO resultado = vagaService.criarVaga(1L, dto);

        assertThat(resultado).isNotNull();
        verify(vagaRepository).save(any(Vaga.class));
    }

    @Test
    void criarVaga_dtoNulo_lancaIllegalArgumentException() {
        assertThatThrownBy(() -> vagaService.criarVaga(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nulos");
    }

    @Test
    void criarVaga_pessoaIdNulo_lancaIllegalArgumentException() {
        assertThatThrownBy(() -> vagaService.criarVaga(null, new VagaDTO()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nulo");
    }

    @Test
    void criarVaga_comCliente_associaClienteAVaga() {
        Pessoa pessoa = criarPessoa(1L);
        Cliente cliente = Cliente.builder().id(5L).nome("Empresa X").build();
        VagaDTO dto = VagaDTO.builder().clienteId(5L).build();
        Vaga vagaSalva = criarVaga(1L, pessoa);
        vagaSalva.setClienteEntity(cliente);
        when(pessoaService.buscarEntidadePessoaPorId(1L)).thenReturn(pessoa);
        when(clienteRepository.findById(5L)).thenReturn(Optional.of(cliente));
        when(vagaRepository.save(any(Vaga.class))).thenReturn(vagaSalva);

        VagaDTO resultado = vagaService.criarVaga(1L, dto);

        assertThat(resultado.getClienteNome()).isEqualTo("Empresa X");
    }

    @Test
    void criarVaga_clienteNaoEncontrado_lancaEntityNotFoundException() {
        Pessoa pessoa = criarPessoa(1L);
        VagaDTO dto = VagaDTO.builder().clienteId(99L).build();
        when(pessoaService.buscarEntidadePessoaPorId(1L)).thenReturn(pessoa);
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> vagaService.criarVaga(1L, dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void atualizarVaga_encontrada_atualizaERetorna() {
        Pessoa pessoa = criarPessoa(1L);
        Vaga vagaExistente = criarVaga(1L, pessoa);
        VagaDTO dto = VagaDTO.builder().cidade("Campinas").build();
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vagaExistente));
        when(vagaRepository.save(any(Vaga.class))).thenAnswer(inv -> inv.getArgument(0));

        VagaDTO resultado = vagaService.atualizarVaga(1L, dto);

        assertThat(resultado.getCidade()).isEqualTo("Campinas");
    }

    @Test
    void deletarVaga_existente_deletaSemErros() {
        when(vagaRepository.existsById(1L)).thenReturn(true);

        vagaService.deletarVaga(1L);

        verify(vagaRepository).deleteById(1L);
    }

    @Test
    void deletarVaga_naoExistente_lancaEntityNotFoundException() {
        when(vagaRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> vagaService.deletarVaga(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    private Pessoa criarPessoa(Long id) {
        Pessoa pessoa = new Pessoa();
        pessoa.setId(id);
        pessoa.setNome("Pessoa " + id);
        return pessoa;
    }

    private Vaga criarVaga(Long id, Pessoa pessoa) {
        Vaga vaga = new Vaga();
        vaga.setId(id);
        vaga.setPessoa(pessoa);
        return vaga;
    }
}
