package com.doban.cadastro_pessoas_docs.recurso.recurso_celular;

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
@RequestMapping("/recursos/celulares")
@CrossOrigin(origins = "http://localhost:4200")
public class RecursoCelularController {

    private final RecursoCelularService recursoCelularService;

    public RecursoCelularController(RecursoCelularService recursoCelularService) {
        this.recursoCelularService = recursoCelularService;
    }

    @GetMapping
    public ResponseEntity<List<RecursoCelularResponseDTO>> listar() {
        List<RecursoCelular> recursos = recursoCelularService.listarTodos();
        List<RecursoCelularResponseDTO> response = recursos.stream()
                .map(RecursoCelularResponseDTO::new)
                .toList();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecursoCelularResponseDTO> buscar(@PathVariable Long id) {
        RecursoCelular recurso = recursoCelularService.buscarPorId(id);
        return ResponseEntity.ok(new RecursoCelularResponseDTO(recurso));
    }

    @PostMapping
    public ResponseEntity<RecursoCelularResponseDTO> criar(@RequestBody RecursoCelularRequestDTO dto) {
        RecursoCelularResponseDTO response = recursoCelularService.criar(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/devolucao")
    public ResponseEntity<RecursoCelularResponseDTO> registrarDevolucao(
            @PathVariable Long id,
            @RequestBody DevolucaoDTO dto) {
        RecursoCelularResponseDTO response = recursoCelularService.registrarDevolucao(id, dto);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        recursoCelularService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
