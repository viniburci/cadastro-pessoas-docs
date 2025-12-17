package com.doban.cadastro_pessoas_docs.domain.pessoa;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
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
        try {
            Pessoa pessoa = pessoaDTO.toEntity();
            Pessoa pessoaSalva = pessoaRepository.save(pessoa);
            return new PessoaDTO(pessoaSalva);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Violação de integridade: dados inválidos ou duplicados.");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar pessoa: " + e.getMessage(), e);
        }
    }

    public PessoaDTO atualizarPessoa(Long id, PessoaDTO dto) {
        if (dto.getId() != null && !dto.getId().equals(id)) {
            throw new IllegalArgumentException("ID do DTO não corresponde ao ID do parâmetro");
        }

        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com id: " + id));

        dto.atualizarEntidade(pessoa);
        
        pessoa = pessoaRepository.save(pessoa);
        return new PessoaDTO(pessoa);
    }

    public void deletarPessoa(Long id) {
        pessoaRepository.deleteById(id);
    }
}
