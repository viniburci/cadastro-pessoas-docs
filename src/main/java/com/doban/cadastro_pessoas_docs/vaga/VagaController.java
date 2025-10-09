package com.doban.cadastro_pessoas_docs.vaga;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
@RequestMapping("/api/v1/vaga")
@RequiredArgsConstructor
public class VagaController {

    private final VagaService vagaService;

    @GetMapping("/pessoa/{pessoaId}")
    public ResponseEntity<List<VagaDTO>> obterVagasPorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(vagaService.obterVagasPorPessoa(pessoaId));
    }

    @GetMapping("/mais-recente/{pessoaId}")
    public ResponseEntity<VagaDTO> obterVagaMaisRecentePorPessoa(@PathVariable Long pessoaId) {
        return ResponseEntity.ok(vagaService.obterVagaMaisRecentePorPessoa(pessoaId));
    }

    @PostMapping("/criar/{pessoaId}")
    public ResponseEntity<VagaDTO> criarVagaParaPessoa (@PathVariable Long pessoaId, @RequestBody VagaDTO vagaDTO) {
        return ResponseEntity.ok(vagaService.criarVaga(pessoaId, vagaDTO));
    }

    @PutMapping("atualizar/{vagaId}")
    public ResponseEntity<VagaDTO> atualizarVaga(@PathVariable Long vagaId, @RequestBody VagaDTO vagaDTO) {    
        return ResponseEntity.ok(vagaService.atualizarVaga(vagaId, vagaDTO));
        
    }

    @DeleteMapping("/{vagaId}")
    public ResponseEntity<Void> deletarVaga(@PathVariable Long vagaId) {
        vagaService.deletarVaga(vagaId);
        return ResponseEntity.noContent().build();
    }
    

}
