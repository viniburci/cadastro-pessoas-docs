package com.doban.cadastro_pessoas_docs.domain.pessoa;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/v1/pessoa")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class PessoaController {

    private final PessoaService pessoaService;

    @GetMapping
    public ResponseEntity<List<PessoaDTO>> buscarTodasPessoas() {
        List<PessoaDTO> pessoas = pessoaService.buscarTodasPessoas();
        return pessoas.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(pessoas);
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<PessoaDTO>> buscarPessoasAtivas() {
        List<PessoaDTO> pessoas = pessoaService.buscarPessoasAtivas();
        return pessoas.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(pessoas);
    }

    @GetMapping("/inativas")
    public ResponseEntity<List<PessoaDTO>> buscarPessoasInativas() {
        List<PessoaDTO> pessoas = pessoaService.buscarPessoasInativas();
        return pessoas.isEmpty()
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(pessoas);
    }

    @GetMapping("/{pessoaId}")
    public ResponseEntity<PessoaDTO> buscarPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(pessoaService.buscarPessoaPorId(pessoaId));
    }

    @PostMapping
    public ResponseEntity<PessoaDTO> criarPessoa(@RequestBody PessoaDTO pessoaDTO) {
        PessoaDTO pessoaSalva = pessoaService.salvarPessoa(pessoaDTO);
        return ResponseEntity.ok(pessoaSalva);
    }


    @PutMapping("atualizar/{id}")
    public ResponseEntity<PessoaDTO> putMethodName(@PathVariable String id, @RequestBody PessoaDTO pessoaDTO) {
        return ResponseEntity.ok(pessoaService.atualizarPessoa(Long.valueOf(id), pessoaDTO));
    }

    @DeleteMapping("/{pessoaId}")
    public ResponseEntity<Void> deletarPessoa(@PathVariable Long pessoaId) {
        pessoaService.deletarPessoa(pessoaId);
        return ResponseEntity.noContent().build();
    }
    

}
