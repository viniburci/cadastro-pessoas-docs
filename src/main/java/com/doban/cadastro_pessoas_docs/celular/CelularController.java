package com.doban.cadastro_pessoas_docs.celular;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/celular")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CelularController {
    private final CelularService celularService;

    @GetMapping
    public List<Celular> listar() {
        return celularService.listarTodos();
    }

    @GetMapping("/{id}")
    public Celular buscar(@PathVariable Long id) {
        return celularService.buscarPorId(id);
    }

    @PostMapping
    public Celular criar(@RequestBody Celular celular) {
        return celularService.salvar(celular);
    }

    @PutMapping("/{id}")
    public Celular atualizar(@PathVariable Long id, @RequestBody Celular celular) {
        return celularService.atualizar(id, celular);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        celularService.deletar(id);
    }
}
