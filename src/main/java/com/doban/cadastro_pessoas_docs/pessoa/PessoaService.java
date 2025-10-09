package com.doban.cadastro_pessoas_docs.pessoa;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    public List<PessoaDTO> buscarTodasPessoas() {
        return pessoaRepository.findAll().stream().map(PessoaDTO::new).toList();
    }

    public Pessoa buscarEntidadePessoaPorId(Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));
    }

    public PessoaDTO buscarPessoaPorId(Long id) {
        Pessoa pessoa = buscarEntidadePessoaPorId(id);
        return new PessoaDTO(pessoa);
    }

    public PessoaDTO salvarPessoa(PessoaDTO pessoaDTO) {
        Pessoa pessoa = pessoaDTO.toEntity();
        Pessoa pessoaSalva = pessoaRepository.save(pessoa);
        return new PessoaDTO(pessoaSalva);
    }

    public PessoaDTO atualizarPessoa(Long id, PessoaDTO dto) {
        if (dto.getId() != null && !dto.getId().equals(id)) {
            throw new IllegalArgumentException("ID do DTO não corresponde ao ID do parâmetro");
        }

        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com id: " + id));

        pessoa.updateFromDTO(dto);
        Pessoa atualizada = pessoaRepository.save(pessoa);
        return new PessoaDTO(atualizada);
    }

    public void deletarPessoa(Long id) {
        pessoaRepository.deleteById(id);
    }
}
