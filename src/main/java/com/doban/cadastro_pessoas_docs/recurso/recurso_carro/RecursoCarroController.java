package com.doban.cadastro_pessoas_docs.recurso.recurso_carro;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.doban.cadastro_pessoas_docs.recurso.DevolucaoDTO;


@RestController
@RequestMapping("/recursos/carros")
@CrossOrigin(origins = "http://localhost:4200")
public class RecursoCarroController {

    private final RecursoCarroService recursoCarroService;

    public RecursoCarroController(RecursoCarroService recursoCarroService) {
        this.recursoCarroService = recursoCarroService;
    }

    @GetMapping
    public ResponseEntity<List<RecursoCarroResponseDTO>> listar() {
        List<RecursoCarroResponseDTO> response = recursoCarroService.listarTodos()
                .stream()
                .map(RecursoCarroResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoCarroResponseDTO> buscar(@PathVariable Long id) {
        RecursoCarro recurso = recursoCarroService.buscarPorId(id);
        return ResponseEntity.ok(new RecursoCarroResponseDTO(recurso));
    }

    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<RecursoCarroResponseDTO>> buscarPorPessoaId(@PathVariable Long pessoaId) {
        List<RecursoCarro> recursos = recursoCarroService.buscarPorPessoaId(pessoaId);
        List<RecursoCarroResponseDTO> response = recursos.stream()
                .map(RecursoCarroResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<RecursoCarroResponseDTO> criar(@RequestBody RecursoCarroRequestDTO dto) {
        RecursoCarroResponseDTO response = recursoCarroService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/devolucao")
    public ResponseEntity<RecursoCarroResponseDTO> registrarDevolucao(
            @PathVariable Long id,
            @RequestBody DevolucaoDTO dto) {
        RecursoCarroResponseDTO response = recursoCarroService.registrarDevolucao(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        recursoCarroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
