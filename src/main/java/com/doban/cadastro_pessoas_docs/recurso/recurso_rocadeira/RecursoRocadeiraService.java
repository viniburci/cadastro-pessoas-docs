package com.doban.cadastro_pessoas_docs.recurso.recurso_rocadeira;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doban.cadastro_pessoas_docs.pessoa.Pessoa;
import com.doban.cadastro_pessoas_docs.pessoa.PessoaRepository;
import com.doban.cadastro_pessoas_docs.recurso.DevolucaoDTO;
import com.doban.cadastro_pessoas_docs.rocadeira.Rocadeira;
import com.doban.cadastro_pessoas_docs.rocadeira.RocadeiraRepository;

@Service
public class RecursoRocadeiraService {
    
    @Autowired
    private RecursoRocadeiraRepository repository;

    @Autowired
    private RocadeiraRepository rocadeiraRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    public RecursoRocadeiraResponseDTO buscarPorId(Long id) {
        RecursoRocadeira rocadeira = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso não encontrado com id: " + id));
        return new RecursoRocadeiraResponseDTO(rocadeira);
    }

    public List<RecursoRocadeiraResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(RecursoRocadeiraResponseDTO::new)
                .collect(Collectors.toList());
    }

    public RecursoRocadeiraResponseDTO criar(RecursoRocadeiraRequestDTO requestDTO) {
        Pessoa pessoa = pessoaRepository.findById(requestDTO.getPessoaId())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com id: " + requestDTO.getPessoaId()));

        Rocadeira rocadeira = rocadeiraRepository.findById(requestDTO.getRocadeiraId())
                .orElseThrow(() -> new RuntimeException("Rocadeira não encontrada com id: " + requestDTO.getRocadeiraId()));

        RecursoRocadeira recursoRocadeira = new RecursoRocadeira();
        recursoRocadeira.setPessoa(pessoa);
        recursoRocadeira.setRocadeira(rocadeira);
        recursoRocadeira.setDataEntrega(requestDTO.getDataEntrega());
        recursoRocadeira.setDataDevolucao(requestDTO.getDataDevolucao());

        RecursoRocadeira recursoSalvo = repository.save(recursoRocadeira);
        return new RecursoRocadeiraResponseDTO(recursoSalvo);
    }

    public RecursoRocadeiraResponseDTO atualizar(Long id, RecursoRocadeiraRequestDTO requestDTO) {
        RecursoRocadeira recursoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso não encontrado com id: " + id));

        Pessoa pessoa = pessoaRepository.findById(requestDTO.getPessoaId())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com id: " + requestDTO.getPessoaId()));

        Rocadeira rocadeira = rocadeiraRepository.findById(requestDTO.getRocadeiraId())
                .orElseThrow(() -> new RuntimeException("Rocadeira não encontrada com id: " + requestDTO.getRocadeiraId()));

        recursoExistente.setPessoa(pessoa);
        recursoExistente.setRocadeira(rocadeira);
        recursoExistente.setDataEntrega(requestDTO.getDataEntrega());
        recursoExistente.setDataDevolucao(requestDTO.getDataDevolucao());

        RecursoRocadeira recursoAtualizado = repository.save(recursoExistente);
        return new RecursoRocadeiraResponseDTO(recursoAtualizado);
    }

    public List<RecursoRocadeiraResponseDTO> buscarPorPessoaId(Long pessoaId) {
        return repository.findByPessoaId(pessoaId).stream()
                .map(RecursoRocadeiraResponseDTO::new)
                .collect(Collectors.toList());
    }

    public RecursoRocadeiraResponseDTO registrarDevolucao(Long id, DevolucaoDTO dto) {
        RecursoRocadeira recurso = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso não encontrado com id: " + id));
        recurso.setDataDevolucao(dto.getDataDevolucao());
        RecursoRocadeira recursoAtualizado = repository.save(recurso);
        return new RecursoRocadeiraResponseDTO(recursoAtualizado);
    }
    
    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }
}
