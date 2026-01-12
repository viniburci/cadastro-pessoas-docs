package com.doban.cadastro_pessoas_docs.recurso.tipo;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/tipos-recurso")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class TipoRecursoController {

    private final TipoRecursoService tipoRecursoService;

    @GetMapping
    public ResponseEntity<List<TipoRecursoDTO>> listarTodos() {
        return ResponseEntity.ok(tipoRecursoService.listarTodos());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<TipoRecursoDTO>> listarAtivos() {
        return ResponseEntity.ok(tipoRecursoService.listarAtivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoRecursoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(tipoRecursoService.buscarPorId(id));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<TipoRecursoDTO> buscarPorCodigo(@PathVariable String codigo) {
        return ResponseEntity.ok(tipoRecursoService.buscarPorCodigo(codigo));
    }

    @PostMapping
    public ResponseEntity<TipoRecursoDTO> criar(@Valid @RequestBody TipoRecursoCreateDTO dto) {
        TipoRecursoDTO criado = tipoRecursoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoRecursoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody TipoRecursoUpdateDTO dto) {
        return ResponseEntity.ok(tipoRecursoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        tipoRecursoService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        tipoRecursoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
