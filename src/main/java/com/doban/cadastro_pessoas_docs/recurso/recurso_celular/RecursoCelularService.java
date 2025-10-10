package com.doban.cadastro_pessoas_docs.recurso.recurso_celular;

import java.util.List;

import org.springframework.stereotype.Service;

import com.doban.cadastro_pessoas_docs.celular.Celular;
import com.doban.cadastro_pessoas_docs.celular.CelularRepository;
import com.doban.cadastro_pessoas_docs.pessoa.Pessoa;
import com.doban.cadastro_pessoas_docs.pessoa.PessoaRepository;
import com.doban.cadastro_pessoas_docs.recurso.DevolucaoDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RecursoCelularService {

    private final RecursoCelularRepository recursoCelularRepository;
    private final PessoaRepository pessoaRepository;
    private final CelularRepository celularRepository;

    public List<RecursoCelular> listarTodos() {
        return recursoCelularRepository.findAll();
    }

    public RecursoCelular buscarPorId(Long id) {
        return recursoCelularRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso não encontrado com id: " + id));
    }

    public RecursoCelular salvar(RecursoCelular recursoCelular) {
        return recursoCelularRepository.save(recursoCelular);
    }

    public RecursoCelularResponseDTO criar(RecursoCelularRequestDTO dto) {
        Pessoa pessoa = pessoaRepository.findById(dto.getPessoaId())
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com id: " + dto.getPessoaId()));

        Celular celular = celularRepository.findById(dto.getCelularId())
                .orElseThrow(() -> new RuntimeException("Celular não encontrado com id: " + dto.getCelularId()));

        RecursoCelular recursoCelular = new RecursoCelular();
        recursoCelular.setPessoa(pessoa);
        recursoCelular.setCelular(celular);
        recursoCelular.setDataEntrega(dto.getDataEntrega());
        recursoCelular.setDataDevolucao(dto.getDataDevolucao());

        recursoCelular = recursoCelularRepository.save(recursoCelular);

        return new RecursoCelularResponseDTO(recursoCelular);
    }

    public RecursoCelularResponseDTO registrarDevolucao(Long id, DevolucaoDTO dto) {
        RecursoCelular recurso = recursoCelularRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Recurso não encontrado com id: " + id));

        recurso.setDataDevolucao(dto.getDataDevolucao());

        recurso = recursoCelularRepository.save(recurso);

        return new RecursoCelularResponseDTO(recurso);
    }

    public void deletar(Long id) {
        recursoCelularRepository.deleteById(id);
    }
}
