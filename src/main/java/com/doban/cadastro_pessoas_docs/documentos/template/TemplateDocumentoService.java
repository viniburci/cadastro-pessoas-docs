package com.doban.cadastro_pessoas_docs.documentos.template;

import com.doban.cadastro_pessoas_docs.domain.vaga.tipo.TipoVaga;
import com.doban.cadastro_pessoas_docs.domain.vaga.tipo.TipoVagaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TemplateDocumentoService {

    private final TemplateDocumentoRepository templateDocumentoRepository;
    private final TipoVagaRepository tipoVagaRepository;

    public TemplateDocumentoService(TemplateDocumentoRepository templateDocumentoRepository,
                                    TipoVagaRepository tipoVagaRepository) {
        this.templateDocumentoRepository = templateDocumentoRepository;
        this.tipoVagaRepository = tipoVagaRepository;
    }

    @Transactional(readOnly = true)
    public List<TemplateDocumentoDTO> listarTodos() {
        return templateDocumentoRepository.findAll().stream()
                .map(TemplateDocumentoDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TemplateDocumentoDTO> listarAtivos() {
        return templateDocumentoRepository.findByAtivoTrue().stream()
                .map(TemplateDocumentoDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TemplateDocumentoDTO> listarPorTipoVaga(Long tipoVagaId) {
        return templateDocumentoRepository.findAtivosByTipoVaga(tipoVagaId).stream()
                .map(TemplateDocumentoDTO::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TemplateDocumentoDTO buscarPorId(Long id) {
        TemplateDocumento template = templateDocumentoRepository.findByIdWithTiposVaga(id)
                .orElseThrow(() -> new EntityNotFoundException("Template não encontrado com id: " + id));
        return new TemplateDocumentoDTO(template);
    }

    @Transactional(readOnly = true)
    public TemplateDocumentoDTO buscarPorCodigo(String codigo) {
        TemplateDocumento template = templateDocumentoRepository.findByCodigoWithTiposVaga(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Template não encontrado com código: " + codigo));
        return new TemplateDocumentoDTO(template);
    }

    @Transactional(readOnly = true)
    public TemplateDocumento buscarEntidadePorCodigo(String codigo) {
        return templateDocumentoRepository.findByCodigoWithTiposVaga(codigo)
                .orElseThrow(() -> new EntityNotFoundException("Template não encontrado com código: " + codigo));
    }

    @Transactional
    public TemplateDocumentoDTO criar(TemplateDocumentoCreateDTO dto) {
        if (templateDocumentoRepository.existsByCodigo(dto.getCodigo())) {
            throw new IllegalArgumentException("Já existe um template com o código: " + dto.getCodigo());
        }

        TemplateDocumento template = TemplateDocumento.builder()
                .codigo(dto.getCodigo())
                .nome(dto.getNome())
                .descricao(dto.getDescricao())
                .conteudoHtml(dto.getConteudoHtml())
                .ativo(true)
                .build();

        if (dto.getSchemaItens() != null) {
            template.setSchemaItens(dto.getSchemaItens());
        }

        if (dto.getVariaveisDisponiveis() != null) {
            template.setVariaveisDisponiveis(dto.getVariaveisDisponiveis());
        }

        if (dto.getTiposVagaPermitidosIds() != null && !dto.getTiposVagaPermitidosIds().isEmpty()) {
            Set<TipoVaga> tiposVaga = new HashSet<>(tipoVagaRepository.findAllById(dto.getTiposVagaPermitidosIds()));
            template.setTiposVagaPermitidos(tiposVaga);
        }

        TemplateDocumento saved = templateDocumentoRepository.save(template);
        return new TemplateDocumentoDTO(saved);
    }

    @Transactional
    public TemplateDocumentoDTO atualizar(Long id, TemplateDocumentoUpdateDTO dto) {
        TemplateDocumento template = templateDocumentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Template não encontrado com id: " + id));

        if (dto.getNome() != null) {
            template.setNome(dto.getNome());
        }

        if (dto.getDescricao() != null) {
            template.setDescricao(dto.getDescricao());
        }

        if (dto.getConteudoHtml() != null) {
            template.setConteudoHtml(dto.getConteudoHtml());
        }

        if (dto.getSchemaItens() != null) {
            template.setSchemaItens(dto.getSchemaItens());
        }

        if (dto.getVariaveisDisponiveis() != null) {
            template.setVariaveisDisponiveis(dto.getVariaveisDisponiveis());
        }

        if (dto.getAtivo() != null) {
            template.setAtivo(dto.getAtivo());
        }

        TemplateDocumento saved = templateDocumentoRepository.save(template);
        return new TemplateDocumentoDTO(saved);
    }

    @Transactional
    public TemplateDocumentoDTO adicionarTipoVagaPermitido(Long templateId, Long tipoVagaId) {
        TemplateDocumento template = templateDocumentoRepository.findByIdWithTiposVaga(templateId)
                .orElseThrow(() -> new EntityNotFoundException("Template não encontrado com id: " + templateId));

        TipoVaga tipoVaga = tipoVagaRepository.findById(tipoVagaId)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vaga não encontrado com id: " + tipoVagaId));

        template.adicionarTipoVagaPermitido(tipoVaga);
        TemplateDocumento saved = templateDocumentoRepository.save(template);
        return new TemplateDocumentoDTO(saved);
    }

    @Transactional
    public TemplateDocumentoDTO removerTipoVagaPermitido(Long templateId, Long tipoVagaId) {
        TemplateDocumento template = templateDocumentoRepository.findByIdWithTiposVaga(templateId)
                .orElseThrow(() -> new EntityNotFoundException("Template não encontrado com id: " + templateId));

        TipoVaga tipoVaga = tipoVagaRepository.findById(tipoVagaId)
                .orElseThrow(() -> new EntityNotFoundException("Tipo de vaga não encontrado com id: " + tipoVagaId));

        template.removerTipoVagaPermitido(tipoVaga);
        TemplateDocumento saved = templateDocumentoRepository.save(template);
        return new TemplateDocumentoDTO(saved);
    }

    @Transactional
    public void desativar(Long id) {
        TemplateDocumento template = templateDocumentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Template não encontrado com id: " + id));
        template.setAtivo(false);
        templateDocumentoRepository.save(template);
    }

    @Transactional
    public void deletar(Long id) {
        if (!templateDocumentoRepository.existsById(id)) {
            throw new EntityNotFoundException("Template não encontrado com id: " + id);
        }
        templateDocumentoRepository.deleteById(id);
    }
}
