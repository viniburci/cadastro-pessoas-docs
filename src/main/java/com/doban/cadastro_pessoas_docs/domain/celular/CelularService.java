package com.doban.cadastro_pessoas_docs.domain.celular;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CelularService {
    private final CelularRepository celularRepository;

    public CelularService(CelularRepository celularRepository) {
        this.celularRepository = celularRepository;
    }

    public List<Celular> listarTodos() {
        return celularRepository.findAll();
    }

    public Celular buscarPorId(Long id) {
        return celularRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Celular não encontrado com id: " + id));
    }

    public Celular salvar(Celular celular) {
        return celularRepository.save(celular);
    }

    public Celular atualizar(Long id, Celular celularAtualizado) {
        Celular celular = buscarPorId(id);
        celular.setMarca(celularAtualizado.getMarca());
        celular.setModelo(celularAtualizado.getModelo());
        celular.setChip(celularAtualizado.getChip());
        celular.setImei(celularAtualizado.getImei());
        return celularRepository.save(celular);
    }

    public void deletar(Long id) {
        celularRepository.deleteById(id);
    }
}
