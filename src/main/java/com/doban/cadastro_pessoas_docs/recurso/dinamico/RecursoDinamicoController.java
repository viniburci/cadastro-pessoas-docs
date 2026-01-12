package com.doban.cadastro_pessoas_docs.recurso.dinamico;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recursos-dinamicos")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class RecursoDinamicoController {

    private final RecursoDinamicoService recursoDinamicoService;

    @GetMapping
    public ResponseEntity<List<RecursoDinamicoDTO>> listarTodos() {
        return ResponseEntity.ok(recursoDinamicoService.listarTodos());
    }

    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<RecursoDinamicoDTO>> listarPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(recursoDinamicoService.listarPorPessoa(pessoaId));
    }

    @GetMapping("/pessoa/{pessoaId}/ativos")
    public ResponseEntity<List<RecursoDinamicoDTO>> listarAtivosParaPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(recursoDinamicoService.listarAtivosParaPessoa(pessoaId));
    }

    @GetMapping("/tipo/{tipoRecursoCodigo}")
    public ResponseEntity<List<RecursoDinamicoDTO>> listarPorTipoRecurso(@PathVariable String tipoRecursoCodigo) {
        return ResponseEntity.ok(recursoDinamicoService.listarPorTipoRecurso(tipoRecursoCodigo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoDinamicoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(recursoDinamicoService.buscarPorId(id));
    }

    @GetMapping("/item/{itemId}/ativo")
    public ResponseEntity<RecursoDinamicoDTO> buscarAtivoParaItem(@PathVariable Long itemId) {
        return ResponseEntity.ok(recursoDinamicoService.buscarAtivoParaItem(itemId));
    }

    @PostMapping
    public ResponseEntity<RecursoDinamicoDTO> emprestar(@Valid @RequestBody RecursoDinamicoCreateDTO dto) {
        RecursoDinamicoDTO criado = recursoDinamicoService.emprestar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}/devolucao")
    public ResponseEntity<RecursoDinamicoDTO> registrarDevolucao(
            @PathVariable Long id,
            @Valid @RequestBody DevolucaoDTO dto) {
        return ResponseEntity.ok(recursoDinamicoService.registrarDevolucao(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        recursoDinamicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
