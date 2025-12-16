package com.doban.cadastro_pessoas_docs.rocadeira;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RocadeiraService {
    
    @Autowired
    private RocadeiraRepository rocadeiraRepository;

    public RocadeiraResponseDTO getRocadeiraByNumeroSerie(String numeroSerie) {
        Rocadeira rocadeira = rocadeiraRepository.findByNumeroSerie(numeroSerie)
            .orElseThrow(() -> new RuntimeException("Rocadeira não encontrada com o numero de serie: " + numeroSerie));
        return new RocadeiraResponseDTO(rocadeira);
    }

    public RocadeiraResponseDTO getRocadeiraById(Long id) {
        Rocadeira rocadeira = rocadeiraRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rocadeira não encontrada com id: " + id));
        return new RocadeiraResponseDTO(rocadeira);
    }

    public List<RocadeiraResponseDTO> getAllRocadeiras() {
        List<Rocadeira> rocadeiras = rocadeiraRepository.findAll();
        List<RocadeiraResponseDTO> rocadeiraDTOs = new java.util.ArrayList<>();
        for (Rocadeira rocadeira : rocadeiras) {
            rocadeiraDTOs.add(new RocadeiraResponseDTO(rocadeira));
        }
        return rocadeiraDTOs;
    }

    public RocadeiraResponseDTO createRocadeira(RocadeiraRequestDTO rocadeiraRequestDTO) {
        Rocadeira rocadeira = Rocadeira.builder()
            .marca(rocadeiraRequestDTO.getMarca())
            .numeroSerie(rocadeiraRequestDTO.getNumeroSerie())
            .build();
        rocadeiraRepository.save(rocadeira);
        return new RocadeiraResponseDTO(rocadeira);
    }

    public RocadeiraResponseDTO updateRocadeira(Long id, RocadeiraRequestDTO rocadeiraRequestDTO) {
        Rocadeira rocadeira = rocadeiraRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Rocadeira não encontrada com id: " + id));
        rocadeira.setMarca(rocadeiraRequestDTO.getMarca());
        rocadeira.setNumeroSerie(rocadeiraRequestDTO.getNumeroSerie());
        rocadeira = rocadeiraRepository.save(rocadeira);
        return new RocadeiraResponseDTO(rocadeira);
    }   

    public void deleteRocadeira(Long id) {
        rocadeiraRepository.deleteById(id);
    }

}
