package com.doban.cadastro_pessoas_docs.domain.vaga.tipo;

import com.doban.cadastro_pessoas_docs.recurso.tipo.TipoRecurso;
import com.doban.cadastro_pessoas_docs.recurso.tipo.TipoRecursoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoVagaService {

    private final TipoVagaRepository tipoVagaRepository;
    private final TipoRecursoService tipoRecursoService;

    @Transactional(readOnly = true)
    public List<TipoVagaDTO> listarTodos() {
        return tipoVagaRepository.findAll().stream()
                .map(TipoVagaDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TipoVagaDTO> listarAtivos() {
        return tipoVagaRepository.findByAtivoTrue().stream()
                .map(TipoVagaDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public TipoVagaDTO buscarPorId(Long id) {
        TipoVaga tipoVaga = tipoVagaRepository.findByIdWithRecursos(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vaga não encontrado com id: " + id));
        return new TipoVagaDTO(tipoVaga);
    }

    @Transactional(readOnly = true)
    public TipoVagaDTO buscarPorCodigo(String codigo) {
        TipoVaga tipoVaga = tipoVagaRepository.findByCodigoWithRecursos(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vaga não encontrado com código: " + codigo));
        return new TipoVagaDTO(tipoVaga);
    }

    @Transactional(readOnly = true)
    public TipoVaga buscarEntidadePorId(Long id) {
        return tipoVagaRepository.findByIdWithRecursos(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vaga não encontrado com id: " + id));
    }

    @Transactional(readOnly = true)
    public TipoVaga buscarEntidadePorCodigo(String codigo) {
        return tipoVagaRepository.findByCodigoWithRecursos(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vaga não encontrado com código: " + codigo));
    }

    @Transactional
    public TipoVagaDTO criar(TipoVagaCreateDTO dto) {
        if (tipoVagaRepository.existsByCodigo(dto.getCodigo())) {
            throw new DataIntegrityViolationException("Já existe um tipo de vaga com o código: " + dto.getCodigo());
        }

        TipoVaga entity = dto.toEntity();

        // Adicionar recursos permitidos se especificados
        if (dto.getRecursosPermitidosIds() != null) {
            for (Long tipoRecursoId : dto.getRecursosPermitidosIds()) {
                TipoRecurso tipoRecurso = tipoRecursoService.buscarEntidadePorId(tipoRecursoId);
                entity.adicionarRecursoPermitido(tipoRecurso);
            }
        }

        entity = tipoVagaRepository.save(entity);
        return new TipoVagaDTO(entity);
    }

    @Transactional
    public TipoVagaDTO atualizar(Long id, TipoVagaUpdateDTO dto) {
        TipoVaga entity = tipoVagaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vaga não encontrado com id: " + id));

        dto.atualizarEntidade(entity);
        entity = tipoVagaRepository.save(entity);
        return new TipoVagaDTO(entity);
    }

    @Transactional
    public TipoVagaDTO adicionarRecursoPermitido(Long tipoVagaId, Long tipoRecursoId) {
        TipoVaga tipoVaga = tipoVagaRepository.findByIdWithRecursos(tipoVagaId)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vaga não encontrado com id: " + tipoVagaId));

        TipoRecurso tipoRecurso = tipoRecursoService.buscarEntidadePorId(tipoRecursoId);

        if (tipoVaga.permiteRecurso(tipoRecurso)) {
            throw new DataIntegrityViolationException(
                    "Tipo de recurso " + tipoRecurso.getNome() + " já está associado a este tipo de vaga");
        }

        tipoVaga.adicionarRecursoPermitido(tipoRecurso);
        tipoVaga = tipoVagaRepository.save(tipoVaga);
        return new TipoVagaDTO(tipoVaga);
    }

    @Transactional
    public TipoVagaDTO removerRecursoPermitido(Long tipoVagaId, Long tipoRecursoId) {
        TipoVaga tipoVaga = tipoVagaRepository.findByIdWithRecursos(tipoVagaId)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vaga não encontrado com id: " + tipoVagaId));

        TipoRecurso tipoRecurso = tipoRecursoService.buscarEntidadePorId(tipoRecursoId);

        if (!tipoVaga.permiteRecurso(tipoRecurso)) {
            throw new IllegalArgumentException(
                    "Tipo de recurso " + tipoRecurso.getNome() + " não está associado a este tipo de vaga");
        }

        tipoVaga.removerRecursoPermitido(tipoRecurso);
        tipoVaga = tipoVagaRepository.save(tipoVaga);
        return new TipoVagaDTO(tipoVaga);
    }

    @Transactional
    public void desativar(Long id) {
        TipoVaga entity = tipoVagaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vaga não encontrado com id: " + id));

        entity.setAtivo(false);
        tipoVagaRepository.save(entity);
    }

    @Transactional
    public void deletar(Long id) {
        if (!tipoVagaRepository.existsById(id)) {
            throw new EntityNotFoundException("Tipo de vaga não encontrado com id: " + id);
        }
        tipoVagaRepository.deleteById(id);
    }

    /**
     * Busca ou cria um tipo de vaga pelo nome.
     * O código é gerado automaticamente em maiúsculo com underscores.
     * Útil para migração de dados existentes.
     */
    @Transactional
    public TipoVaga buscarOuCriarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return null;
        }

        return tipoVagaRepository.findByNome(nome)
                .orElseGet(() -> {
                    // Gera código automaticamente: nome em maiúsculo com underscores
                    String codigo = nome.trim()
                            .toUpperCase()
                            .replaceAll("\\s+", "_")
                            .replaceAll("[^A-Z0-9_]", "");

                    TipoVaga novoTipo = TipoVaga.builder()
                            .codigo(codigo)
                            .nome(nome.trim())
                            .ativo(true)
                            .build();

                    return tipoVagaRepository.save(novoTipo);
                });
    }
}
