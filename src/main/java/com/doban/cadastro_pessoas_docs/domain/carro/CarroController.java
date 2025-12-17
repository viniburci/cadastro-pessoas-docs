package com.doban.cadastro_pessoas_docs.domain.carro;

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

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("api/v1/carros")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CarroController {

    private final CarroService carroService;

    @GetMapping
    public ResponseEntity<List<Carro>> listar() {
        return ResponseEntity.ok(carroService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carro> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(carroService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Carro> criar(@RequestBody Carro carro) {
        Carro novo = carroService.salvar(carro);
        return ResponseEntity.status(HttpStatus.CREATED).body(novo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Carro> atualizar(@PathVariable Long id, @RequestBody Carro carro) {
        return ResponseEntity.ok(carroService.atualizar(id, carro));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        carroService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}

