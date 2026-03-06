package com.doban.cadastro_pessoas_docs.domain.cliente;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public List<ClienteDTO> listarTodos() {
        return clienteRepository.findAll(Sort.by("id").ascending()).stream()
                .map(ClienteDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ClienteDTO> listarAtivos() {
        return clienteRepository.findByAtivoTrueOrderByIdAsc().stream()
                .map(ClienteDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClienteDTO buscarPorId(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id: " + id));
        return new ClienteDTO(cliente);
    }

    @Transactional(readOnly = true)
    public ClienteDTO buscarPorNome(String nome) {
        Cliente cliente = clienteRepository.findByNome(nome)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com nome: " + nome));
        return new ClienteDTO(cliente);
    }

    @Transactional
    public ClienteDTO criar(ClienteDTO dto) {
        log.info("Criando cliente: {}", dto.getNome());
        if (clienteRepository.existsByNome(dto.getNome())) {
            throw new IllegalArgumentException("Já existe um cliente com o nome: " + dto.getNome());
        }

        Cliente cliente = dto.toEntity();
        cliente.setId(null);
        cliente = clienteRepository.save(cliente);
        return new ClienteDTO(cliente);
    }

    @Transactional
    public ClienteDTO atualizar(Long id, ClienteDTO dto) {
        log.info("Atualizando cliente com id: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id: " + id));

        // Verifica se o novo nome já existe em outro cliente
        if (!cliente.getNome().equals(dto.getNome()) && clienteRepository.existsByNome(dto.getNome())) {
            throw new IllegalArgumentException("Já existe um cliente com o nome: " + dto.getNome());
        }

        cliente.setNome(dto.getNome());
        cliente.setDescricao(dto.getDescricao());
        if (dto.getAtivo() != null) {
            cliente.setAtivo(dto.getAtivo());
        }

        cliente = clienteRepository.save(cliente);
        return new ClienteDTO(cliente);
    }

    @Transactional
    public void deletar(Long id) {
        log.info("Deletando cliente com id: {}", id);
        if (!clienteRepository.existsById(id)) {
            throw new EntityNotFoundException("Cliente não encontrado com id: " + id);
        }
        clienteRepository.deleteById(id);
    }

    @Transactional
    public ClienteDTO alternarAtivo(Long id) {
        log.info("Alternando status ativo do cliente com id: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado com id: " + id));

        cliente.setAtivo(!cliente.getAtivo());
        cliente = clienteRepository.save(cliente);
        return new ClienteDTO(cliente);
    }

    /**
     * Busca ou cria um cliente pelo nome.
     * Útil para migração de dados existentes.
     */
    @Transactional
    public Cliente buscarOuCriarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return null;
        }

        return clienteRepository.findByNome(nome)
                .orElseGet(() -> {
                    Cliente novoCliente = Cliente.builder()
                            .nome(nome)
                            .ativo(true)
                            .build();
                    return clienteRepository.save(novoCliente);
                });
    }
}
