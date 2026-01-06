package com.doban.cadastro_pessoas_docs.domain.pessoa;

import java.time.LocalDate;
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

    public List<PessoaDTO> buscarPessoasAtivas() {
        LocalDate hoje = LocalDate.now();
        return pessoaRepository.findAll().stream()
                .filter(pessoa -> pessoa.getVagas().stream()
                        .anyMatch(vaga -> vaga.getDataDemissao() == null || vaga.getDataDemissao().isAfter(hoje)))
                .map(PessoaDTO::new)
                .toList();
    }

    public List<PessoaDTO> buscarPessoasInativas() {
        LocalDate hoje = LocalDate.now();
        return pessoaRepository.findAll().stream()
                .filter(pessoa -> pessoa.getVagas().isEmpty() ||
                        pessoa.getVagas().stream()
                                .allMatch(vaga -> vaga.getDataDemissao() != null && !vaga.getDataDemissao().isAfter(hoje)))
                .map(PessoaDTO::new)
                .toList();
    }

    public void salvarFoto(Long pessoaId, byte[] foto) {
        if (foto == null || foto.length == 0) {
            throw new IllegalArgumentException("Foto não pode ser vazia");
        }

        // Limite de 5MB para fotos
        if (foto.length > 5_000_000) {
            throw new IllegalArgumentException("Foto muito grande. Tamanho máximo: 5MB");
        }

        Pessoa pessoa = buscarEntidadePessoaPorId(pessoaId);
        pessoa.setFoto(foto);
        pessoaRepository.save(pessoa);
    }

    public byte[] buscarFoto(Long pessoaId) {
        Pessoa pessoa = buscarEntidadePessoaPorId(pessoaId);
        byte[] foto = pessoa.getFoto();

        if (foto == null || foto.length == 0) {
            throw new EntityNotFoundException("Foto não encontrada para a pessoa com id: " + pessoaId);
        }

        return foto;
    }
}
