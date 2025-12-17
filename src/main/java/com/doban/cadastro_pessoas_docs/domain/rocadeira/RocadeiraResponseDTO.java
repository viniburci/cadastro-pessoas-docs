package com.doban.cadastro_pessoas_docs.domain.rocadeira;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RocadeiraResponseDTO {

    private Long id;
    private String marca;
    private String numeroSerie;

    public RocadeiraResponseDTO(Rocadeira rocadeira) {
        this.id = rocadeira.getId();
        this.marca = rocadeira.getMarca();
        this.numeroSerie = rocadeira.getNumeroSerie();
    }

}
