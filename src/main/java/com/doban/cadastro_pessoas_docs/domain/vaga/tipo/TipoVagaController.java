package com.doban.cadastro_pessoas_docs.domain.vaga.tipo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-vaga")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TipoVagaController {

    private final TipoVagaService tipoVagaService;

    @GetMapping
    public ResponseEntity<List<TipoVagaDTO>> listarTodos() {
        return ResponseEntity.ok(tipoVagaService.listarTodos());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<TipoVagaDTO>> listarAtivos() {
        return ResponseEntity.ok(tipoVagaService.listarAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoVagaDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tipoVagaService.buscarPorId(id));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<TipoVagaDTO> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(tipoVagaService.buscarPorCodigo(codigo));
    }

    @PostMapping
    public ResponseEntity<TipoVagaDTO> criar(@Valid @RequestBody TipoVagaCreateDTO dto) {
        TipoVagaDTO criado = tipoVagaService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoVagaDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TipoVagaUpdateDTO dto) {
        return ResponseEntity.ok(tipoVagaService.atualizar(id, dto));
    }

    @PostMapping("/{tipoVagaId}/recursos-permitidos/{tipoRecursoId}")
    public ResponseEntity<TipoVagaDTO> adicionarRecursoPermitido(
            @PathVariable Long tipoVagaId,
            @PathVariable Long tipoRecursoId) {
        return ResponseEntity.ok(tipoVagaService.adicionarRecursoPermitido(tipoVagaId, tipoRecursoId));
    }

    @DeleteMapping("/{tipoVagaId}/recursos-permitidos/{tipoRecursoId}")
    public ResponseEntity<TipoVagaDTO> removerRecursoPermitido(
            @PathVariable Long tipoVagaId,
            @PathVariable Long tipoRecursoId) {
        return ResponseEntity.ok(tipoVagaService.removerRecursoPermitido(tipoVagaId, tipoRecursoId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        tipoVagaService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tipoVagaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
