package com.doban.cadastro_pessoas_docs.recurso.tipo;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TipoRecursoService {

    private final TipoRecursoRepository tipoRecursoRepository;

    @Transactional(readOnly = true)
    public List<TipoRecursoDTO> listarTodos() {
        return tipoRecursoRepository.findAll(Sort.by("id").ascending()).stream()
                .map(TipoRecursoDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TipoRecursoDTO> listarAtivos() {
        return tipoRecursoRepository.findByAtivoTrueOrderByIdAsc().stream()
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
        log.info("Criando tipo de recurso: {} ({})", dto.getNome(), dto.getCodigo());
        if (tipoRecursoRepository.existsByCodigo(dto.getCodigo())) {
            throw new DataIntegrityViolationException("Já existe um tipo de recurso com o código: " + dto.getCodigo());
        }

        TipoRecurso entity = dto.toEntity();
        entity = tipoRecursoRepository.save(entity);
        return new TipoRecursoDTO(entity);
    }

    @Transactional
    public TipoRecursoDTO atualizar(Long id, TipoRecursoUpdateDTO dto) {
        log.info("Atualizando tipo de recurso com id: {}", id);
        TipoRecurso entity = tipoRecursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de recurso não encontrado com id: " + id));

        dto.atualizarEntidade(entity);
        entity = tipoRecursoRepository.save(entity);
        return new TipoRecursoDTO(entity);
    }

    @Transactional
    public void desativar(Long id) {
        log.info("Desativando tipo de recurso com id: {}", id);
        TipoRecurso entity = tipoRecursoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de recurso não encontrado com id: " + id));

        entity.setAtivo(false);
        tipoRecursoRepository.save(entity);
    }

    @Transactional
    public void deletar(Long id) {
        log.info("Deletando tipo de recurso com id: {}", id);
        if (!tipoRecursoRepository.existsById(id)) {
            throw new EntityNotFoundException("Tipo de recurso não encontrado com id: " + id);
        }
        tipoRecursoRepository.deleteById(id);
    }
}
