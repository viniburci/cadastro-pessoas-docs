package com.doban.cadastro_pessoas_docs.recurso.recurso_rocadeira;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.doban.cadastro_pessoas_docs.pessoa.Pessoa;
import com.doban.cadastro_pessoas_docs.pessoa.PessoaRepository;
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

    public RecursoRocadeira buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso não encontrado com id: " + id));
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
    
    public void deletarPorId(Long id) {
        repository.deleteById(id);
    }
}
