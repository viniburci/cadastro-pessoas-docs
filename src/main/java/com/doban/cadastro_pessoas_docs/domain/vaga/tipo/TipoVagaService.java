package com.doban.cadastro_pessoas_docs.domain.vaga.tipo;

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
public class TipoVagaService {

    private final TipoVagaRepository tipoVagaRepository;

    @Transactional(readOnly = true)
    public List<TipoVagaDTO> listarTodos() {
        return tipoVagaRepository.findAll(Sort.by("id").ascending()).stream()
                .map(TipoVagaDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TipoVagaDTO> listarAtivos() {
        return tipoVagaRepository.findByAtivoTrueOrderByIdAsc().stream()
                .map(TipoVagaDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public TipoVagaDTO buscarPorId(Long id) {
        TipoVaga tipoVaga = tipoVagaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vaga não encontrado com id: " + id));
        return new TipoVagaDTO(tipoVaga);
    }

    @Transactional(readOnly = true)
    public TipoVagaDTO buscarPorCodigo(String codigo) {
        TipoVaga tipoVaga = tipoVagaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vaga não encontrado com código: " + codigo));
        return new TipoVagaDTO(tipoVaga);
    }

    @Transactional(readOnly = true)
    public TipoVaga buscarEntidadePorId(Long id) {
        return tipoVagaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vaga não encontrado com id: " + id));
    }

    @Transactional(readOnly = true)
    public TipoVaga buscarEntidadePorCodigo(String codigo) {
        return tipoVagaRepository.findByCodigo(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vaga não encontrado com código: " + codigo));
    }

    @Transactional
    public TipoVagaDTO criar(TipoVagaCreateDTO dto) {
        log.info("Criando tipo de vaga: {} ({})", dto.getNome(), dto.getCodigo());
        if (tipoVagaRepository.existsByCodigo(dto.getCodigo())) {
            throw new DataIntegrityViolationException("Já existe um tipo de vaga com o código: " + dto.getCodigo());
        }

        TipoVaga entity = dto.toEntity();
        entity = tipoVagaRepository.save(entity);
        return new TipoVagaDTO(entity);
    }

    @Transactional
    public TipoVagaDTO atualizar(Long id, TipoVagaUpdateDTO dto) {
        log.info("Atualizando tipo de vaga com id: {}", id);
        TipoVaga entity = tipoVagaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vaga não encontrado com id: " + id));

        dto.atualizarEntidade(entity);
        entity = tipoVagaRepository.save(entity);
        return new TipoVagaDTO(entity);
    }

    @Transactional
    public void desativar(Long id) {
        log.info("Desativando tipo de vaga com id: {}", id);
        TipoVaga entity = tipoVagaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vaga não encontrado com id: " + id));

        entity.setAtivo(false);
        tipoVagaRepository.save(entity);
    }

    @Transactional
    public void deletar(Long id) {
        log.info("Deletando tipo de vaga com id: {}", id);
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
                    log.info("Criando novo tipo de vaga: {}", nome);
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
