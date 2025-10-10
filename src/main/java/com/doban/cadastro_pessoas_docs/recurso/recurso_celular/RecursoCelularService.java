package com.doban.cadastro_pessoas_docs.recurso.recurso_celular;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class RecursoCelularService {
    
    private final RecursoCelularRepository recursoCelularRepository;

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

    public void deletar(Long id) {
        recursoCelularRepository.deleteById(id);
    }
}
