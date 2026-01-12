package com.doban.cadastro_pessoas_docs.recurso.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/itens")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class ItemDinamicoController {

    private final ItemDinamicoService itemDinamicoService;

    @GetMapping
    public ResponseEntity<List<ItemDinamicoDTO>> listarTodos() {
        return ResponseEntity.ok(itemDinamicoService.listarTodos());
    }

    @GetMapping("/tipo/{tipoRecursoCodigo}")
    public ResponseEntity<List<ItemDinamicoDTO>> listarPorTipo(@PathVariable String tipoRecursoCodigo) {
        return ResponseEntity.ok(itemDinamicoService.listarPorTipo(tipoRecursoCodigo));
    }

    @GetMapping("/disponiveis/{tipoRecursoCodigo}")
    public ResponseEntity<List<ItemDinamicoDTO>> listarDisponiveis(@PathVariable String tipoRecursoCodigo) {
        return ResponseEntity.ok(itemDinamicoService.listarDisponiveis(tipoRecursoCodigo));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemDinamicoDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(itemDinamicoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<ItemDinamicoDTO> criar(@Valid @RequestBody ItemDinamicoCreateDTO dto) {
        ItemDinamicoDTO criado = itemDinamicoService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(criado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemDinamicoDTO> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody ItemDinamicoUpdateDTO dto) {
        return ResponseEntity.ok(itemDinamicoService.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        itemDinamicoService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/permanente")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        itemDinamicoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
