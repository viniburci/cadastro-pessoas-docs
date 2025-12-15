package com.doban.cadastro_pessoas_docs.recurso.recurso_rocadeira;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recursos/rocadeiras")
@CrossOrigin(origins = "http://localhost:4200")
public class RecursoRocadeiraController {

    @Autowired
    private RecursoRocadeiraService service;

    @GetMapping
    public ResponseEntity<List<RecursoRocadeiraResponseDTO>> listar() {
        return ResponseEntity.ok().body(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoRocadeiraResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok().body(service.buscarPorId(id));
    }

    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<RecursoRocadeiraResponseDTO>> buscarPorPessoaId(@PathVariable Long pessoaId) {
        return ResponseEntity.ok().body(service.buscarPorPessoaId(pessoaId));
    }

    @PostMapping
    public ResponseEntity<RecursoRocadeiraResponseDTO> criar(RecursoRocadeiraRequestDTO dto) {
        return ResponseEntity.ok().body(service.criar(dto));
    }

    @PostMapping(path = "/{id}/devolucao")
    public ResponseEntity<RecursoRocadeiraResponseDTO> registrarDevolucao(@PathVariable Long id, RecursoRocadeiraRequestDTO dto) {
        return ResponseEntity.ok().body(service.registrarDevolucao(id, dto));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
