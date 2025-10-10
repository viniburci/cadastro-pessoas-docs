package com.doban.cadastro_pessoas_docs.recurso.recurso_celular;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recursos/celulares")
public class RecursoCelularController {
    
    private final RecursoCelularService recursoCelularService;

    public RecursoCelularController(RecursoCelularService recursoCelularService) {
        this.recursoCelularService = recursoCelularService;
    }

    @GetMapping
    public List<RecursoCelular> listar() {
        return recursoCelularService.listarTodos();
    }

    @GetMapping("/{id}")
    public RecursoCelular buscar(@PathVariable Long id) {
        return recursoCelularService.buscarPorId(id);
    }

    @PostMapping
    public RecursoCelular criar(@RequestBody RecursoCelular recursoCelular) {
        return recursoCelularService.salvar(recursoCelular);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        recursoCelularService.deletar(id);
    }
}
