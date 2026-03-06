package com.doban.cadastro_pessoas_docs.domain.cliente;

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
class ClienteServiceTest {

    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    @Test
    void listarTodos_retornaListaDeClientes() {
        when(clienteRepository.findAll(any(Sort.class)))
                .thenReturn(List.of(criarCliente(1L, "Cliente A"), criarCliente(2L, "Cliente B")));

        List<ClienteDTO> resultado = clienteService.listarTodos();

        assertThat(resultado).hasSize(2);
    }

    @Test
    void listarAtivos_retornaApenasAtivos() {
        when(clienteRepository.findByAtivoTrueOrderByIdAsc())
                .thenReturn(List.of(criarCliente(1L, "Cliente A")));

        List<ClienteDTO> resultado = clienteService.listarAtivos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0).getNome()).isEqualTo("Cliente A");
    }

    @Test
    void buscarPorId_encontrado_retornaDTO() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(criarCliente(1L, "Cliente A")));

        ClienteDTO resultado = clienteService.buscarPorId(1L);

        assertThat(resultado.getNome()).isEqualTo("Cliente A");
    }

    @Test
    void buscarPorId_naoEncontrado_lancaEntityNotFoundException() {
        when(clienteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.buscarPorId(99L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void buscarPorNome_encontrado_retornaDTO() {
        when(clienteRepository.findByNome("Cliente A")).thenReturn(Optional.of(criarCliente(1L, "Cliente A")));

        ClienteDTO resultado = clienteService.buscarPorNome("Cliente A");

        assertThat(resultado.getNome()).isEqualTo("Cliente A");
    }

    @Test
    void buscarPorNome_naoEncontrado_lancaEntityNotFoundException() {
        when(clienteRepository.findByNome("Inexistente")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> clienteService.buscarPorNome("Inexistente"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void criar_nomeNovo_salvaERetornaDTO() {
        ClienteDTO dto = ClienteDTO.builder().nome("Novo Cliente").descricao("Desc").ativo(true).build();
        Cliente clienteSalvo = criarCliente(1L, "Novo Cliente");
        when(clienteRepository.existsByNome("Novo Cliente")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteSalvo);

        ClienteDTO resultado = clienteService.criar(dto);

        assertThat(resultado.getNome()).isEqualTo("Novo Cliente");
        verify(clienteRepository).save(any(Cliente.class));
    }

    @Test
    void criar_nomeDuplicado_lancaIllegalArgumentException() {
        ClienteDTO dto = ClienteDTO.builder().nome("Duplicado").build();
        when(clienteRepository.existsByNome("Duplicado")).thenReturn(true);

        assertThatThrownBy(() -> clienteService.criar(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Duplicado");
    }

    @Test
    void atualizar_dadosValidos_retornaClienteAtualizado() {
        Cliente clienteExistente = criarCliente(1L, "Nome Antigo");
        ClienteDTO dto = ClienteDTO.builder().nome("Nome Novo").build();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.existsByNome("Nome Novo")).thenReturn(false);
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        ClienteDTO resultado = clienteService.atualizar(1L, dto);

        assertThat(resultado.getNome()).isEqualTo("Nome Novo");
    }

    @Test
    void atualizar_novoNomeDuplicado_lancaIllegalArgumentException() {
        Cliente clienteExistente = criarCliente(1L, "Nome Antigo");
        ClienteDTO dto = ClienteDTO.builder().nome("Nome Duplicado").build();
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteExistente));
        when(clienteRepository.existsByNome("Nome Duplicado")).thenReturn(true);

        assertThatThrownBy(() -> clienteService.atualizar(1L, dto))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void deletar_existente_deletaSemErros() {
        when(clienteRepository.existsById(1L)).thenReturn(true);

        clienteService.deletar(1L);

        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void deletar_naoExistente_lancaEntityNotFoundException() {
        when(clienteRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> clienteService.deletar(99L))
                .isInstanceOf(EntityNotFoundException.class);
    }

    @Test
    void alternarAtivo_clienteAtivo_desativa() {
        Cliente cliente = criarCliente(1L, "Cliente");
        cliente.setAtivo(true);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        ClienteDTO resultado = clienteService.alternarAtivo(1L);

        assertThat(resultado.getAtivo()).isFalse();
    }

    @Test
    void alternarAtivo_clienteInativo_ativa() {
        Cliente cliente = criarCliente(1L, "Cliente");
        cliente.setAtivo(false);
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteRepository.save(any(Cliente.class))).thenAnswer(inv -> inv.getArgument(0));

        ClienteDTO resultado = clienteService.alternarAtivo(1L);

        assertThat(resultado.getAtivo()).isTrue();
    }

    @Test
    void buscarOuCriarPorNome_nomeExistente_retornaExistente() {
        Cliente existente = criarCliente(1L, "Cliente A");
        when(clienteRepository.findByNome("Cliente A")).thenReturn(Optional.of(existente));

        Cliente resultado = clienteService.buscarOuCriarPorNome("Cliente A");

        assertThat(resultado.getId()).isEqualTo(1L);
        verify(clienteRepository, never()).save(any());
    }

    @Test
    void buscarOuCriarPorNome_nomeNulo_retornaNulo() {
        Cliente resultado = clienteService.buscarOuCriarPorNome(null);
        assertThat(resultado).isNull();
    }

    @Test
    void buscarOuCriarPorNome_nomeNovo_criaNovo() {
        Cliente novo = criarCliente(2L, "Novo");
        when(clienteRepository.findByNome("Novo")).thenReturn(Optional.empty());
        when(clienteRepository.save(any(Cliente.class))).thenReturn(novo);

        Cliente resultado = clienteService.buscarOuCriarPorNome("Novo");

        assertThat(resultado.getNome()).isEqualTo("Novo");
        verify(clienteRepository).save(any(Cliente.class));
    }

    private Cliente criarCliente(Long id, String nome) {
        return Cliente.builder().id(id).nome(nome).ativo(true).build();
    }
}
