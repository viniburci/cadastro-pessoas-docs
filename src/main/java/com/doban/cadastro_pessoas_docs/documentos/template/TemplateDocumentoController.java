package com.doban.cadastro_pessoas_docs.documentos.template;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/templates-documento")
public class TemplateDocumentoController {

    private final TemplateDocumentoService templateDocumentoService;

    public TemplateDocumentoController(TemplateDocumentoService templateDocumentoService) {
        this.templateDocumentoService = templateDocumentoService;
    }

    @GetMapping
    public ResponseEntity<List<TemplateDocumentoDTO>> listarTodos() {
        return ResponseEntity.ok(templateDocumentoService.listarTodos());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<TemplateDocumentoDTO>> listarAtivos() {
        return ResponseEntity.ok(templateDocumentoService.listarAtivos());
    }

    @GetMapping("/tipo-vaga/{tipoVagaId}")
    public ResponseEntity<List<TemplateDocumentoDTO>> listarPorTipoVaga(@PathVariable Long tipoVagaId) {
        return ResponseEntity.ok(templateDocumentoService.listarPorTipoVaga(tipoVagaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateDocumentoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(templateDocumentoService.buscarPorId(id));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<TemplateDocumentoDTO> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(templateDocumentoService.buscarPorCodigo(codigo));
    }

    @PostMapping
    public ResponseEntity<TemplateDocumentoDTO> criar(@Valid @RequestBody TemplateDocumentoCreateDTO dto) {
        TemplateDocumentoDTO created = templateDocumentoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TemplateDocumentoDTO> atualizar(@PathVariable Long id,
                                                          @Valid @RequestBody TemplateDocumentoUpdateDTO dto) {
        return ResponseEntity.ok(templateDocumentoService.atualizar(id, dto));
    }

    @PostMapping("/{templateId}/tipos-vaga/{tipoVagaId}")
    public ResponseEntity<TemplateDocumentoDTO> adicionarTipoVagaPermitido(@PathVariable Long templateId,
                                                                           @PathVariable Long tipoVagaId) {
        return ResponseEntity.ok(templateDocumentoService.adicionarTipoVagaPermitido(templateId, tipoVagaId));
    }

    @DeleteMapping("/{templateId}/tipos-vaga/{tipoVagaId}")
    public ResponseEntity<TemplateDocumentoDTO> removerTipoVagaPermitido(@PathVariable Long templateId,
                                                                         @PathVariable Long tipoVagaId) {
        return ResponseEntity.ok(templateDocumentoService.removerTipoVagaPermitido(templateId, tipoVagaId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        templateDocumentoService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        templateDocumentoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
