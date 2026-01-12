package com.doban.cadastro_pessoas_docs.recurso.tipo;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoRecursoService {

    private final TipoRecursoRepository tipoRecursoRepository;

    @Transactional(readOnly = true)
    public List<TipoRecursoDTO> listarTodos() {
        return tipoRecursoRepository.findAll().stream()
                .map(TipoRecursoDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TipoRecursoDTO> listarAtivos() {
        return tipoRecursoRepository.findByAtivoTrue().stream()
                .map(TipoRecursoDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public TipoRecursoDTO buscarPorId(Long id) {
        TipoRecurso tipoRecurso = tipoRecursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de recurso não encontrado com id: " + id));
        return new TipoRecursoDTO(tipoRecurso);
    }

    @Transactional(readOnly = true)
    public TipoRecursoDTO buscarPorCodigo(String codigo) {
        TipoRecurso tipoRecurso = tipoRecursoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de recurso não encontrado com código: " + codigo));
        return new TipoRecursoDTO(tipoRecurso);
    }

    @Transactional(readOnly = true)
    public TipoRecurso buscarEntidadePorId(Long id) {
        return tipoRecursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de recurso não encontrado com id: " + id));
    }

    @Transactional(readOnly = true)
    public TipoRecurso buscarEntidadePorCodigo(String codigo) {
        return tipoRecursoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de recurso não encontrado com código: " + codigo));
    }

    @Transactional
    public TipoRecursoDTO criar(TipoRecursoCreateDTO dto) {
        if (tipoRecursoRepository.existsByCodigo(dto.getCodigo())) {
            throw new DataIntegrityViolationException("Já existe um tipo de recurso com o código: " + dto.getCodigo());
        }

        TipoRecurso entity = dto.toEntity();
        entity = tipoRecursoRepository.save(entity);
        return new TipoRecursoDTO(entity);
    }

    @Transactional
    public TipoRecursoDTO atualizar(Long id, TipoRecursoUpdateDTO dto) {
        TipoRecurso entity = tipoRecursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de recurso não encontrado com id: " + id));

        dto.atualizarEntidade(entity);
        entity = tipoRecursoRepository.save(entity);
        return new TipoRecursoDTO(entity);
    }

    @Transactional
    public void desativar(Long id) {
        TipoRecurso entity = tipoRecursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de recurso não encontrado com id: " + id));

        entity.setAtivo(false);
        tipoRecursoRepository.save(entity);
    }

    @Transactional
    public void deletar(Long id) {
        if (!tipoRecursoRepository.existsById(id)) {
            throw new EntityNotFoundException("Tipo de recurso não encontrado com id: " + id);
        }
        tipoRecursoRepository.deleteById(id);
    }
}
