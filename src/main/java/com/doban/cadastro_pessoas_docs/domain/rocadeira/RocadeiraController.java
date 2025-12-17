package com.doban.cadastro_pessoas_docs.domain.rocadeira;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/rocadeira")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class RocadeiraController {

    @Autowired
    private RocadeiraService service;

    @GetMapping("/numeroSerie/{numeroSerie}")
    public ResponseEntity<RocadeiraResponseDTO> obterPorNumeroSerie (@PathVariable String numeroSerie) {
        RocadeiraResponseDTO responseDTO = service.getRocadeiraByNumeroSerie(numeroSerie);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RocadeiraResponseDTO> obterPorId (@PathVariable Long id) {
        RocadeiraResponseDTO responseDTO = service.getRocadeiraById(id);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<RocadeiraResponseDTO>> obterTodas () {
        List<RocadeiraResponseDTO> responseDTOs = service.getAllRocadeiras();
        return ResponseEntity.status(HttpStatus.OK).body(responseDTOs);
    }

    @PostMapping
    public ResponseEntity<RocadeiraResponseDTO> criar (@RequestBody RocadeiraRequestDTO dto) {
        RocadeiraResponseDTO responseDTO = service.createRocadeira(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RocadeiraResponseDTO> atualizar (@PathVariable Long id, @RequestBody RocadeiraRequestDTO dto) {
        RocadeiraResponseDTO responseDTO = service.updateRocadeira(id, dto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar (@PathVariable Long id) {
        service.deleteRocadeira(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
