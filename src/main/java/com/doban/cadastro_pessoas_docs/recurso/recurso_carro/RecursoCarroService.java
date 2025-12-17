package com.doban.cadastro_pessoas_docs.recurso.recurso_carro;

import java.util.List;

import org.springframework.stereotype.Service;

import com.doban.cadastro_pessoas_docs.domain.carro.Carro;
import com.doban.cadastro_pessoas_docs.domain.carro.CarroRepository;
import com.doban.cadastro_pessoas_docs.domain.pessoa.Pessoa;
import com.doban.cadastro_pessoas_docs.domain.pessoa.PessoaRepository;
import com.doban.cadastro_pessoas_docs.recurso.DevolucaoDTO;

@Service
public class RecursoCarroService {

    private final RecursoCarroRepository recursoCarroRepository;
    private final CarroRepository carroRepository;
    private final PessoaRepository pessoaRepository;

    public RecursoCarroService(RecursoCarroRepository recursoCarroRepository,
                               CarroRepository carroRepository,
                               PessoaRepository pessoaRepository) {
        this.recursoCarroRepository = recursoCarroRepository;
        this.carroRepository = carroRepository;
        this.pessoaRepository = pessoaRepository;
    }

    public List<RecursoCarro> listarTodos() {
        return recursoCarroRepository.findAll();
    }

    public RecursoCarro buscarPorId(Long id) {
        return recursoCarroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso não encontrado com id: " + id));
    }

    public List<RecursoCarro> buscarPorPessoaId(Long pessoaId) {
        return recursoCarroRepository.findByPessoaId(pessoaId);
    }

    public RecursoCarroResponseDTO criar(RecursoCarroRequestDTO dto) {
        Pessoa pessoa = pessoaRepository.findById(dto.getPessoaId())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com id: " + dto.getPessoaId()));

        Carro carro = carroRepository.findById(dto.getCarroId())
                .orElseThrow(() -> new RuntimeException("Carro não encontrado com id: " + dto.getCarroId()));

        RecursoCarro recursoCarro = new RecursoCarro();
        recursoCarro.setPessoa(pessoa);
        recursoCarro.setCarro(carro);
        recursoCarro.setDataEntrega(dto.getDataEntrega());
        recursoCarro.setDataDevolucao(dto.getDataDevolucao());

        recursoCarro = recursoCarroRepository.save(recursoCarro);

        return new RecursoCarroResponseDTO(recursoCarro);
    }

    public RecursoCarroResponseDTO registrarDevolucao(Long id, DevolucaoDTO dto) {
        RecursoCarro recurso = buscarPorId(id);
        recurso.setDataDevolucao(dto.getDataDevolucao());
        recurso = recursoCarroRepository.save(recurso);
        return new RecursoCarroResponseDTO(recurso);
    }

    public void deletar(Long id) {
        recursoCarroRepository.deleteById(id);
    }
}
