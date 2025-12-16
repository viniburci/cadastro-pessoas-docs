package com.doban.cadastro_pessoas_docs.rocadeira;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RocadeiraDTO {

    private String marca;
    private String NumeroSerie;

    public RocadeiraDTO() {
    }

    public RocadeiraDTO(String marca, String numeroSerie) {
        this.marca = marca;
        this.NumeroSerie = numeroSerie;
    }

    public RocadeiraDTO(Rocadeira rocadeira) {
        this.marca = rocadeira.getMarca();
        this.NumeroSerie = rocadeira.getNumeroSerie();
    }

    public Rocadeira toEntity() {
        return new Rocadeira(
                null, this.marca, this.NumeroSerie);
    }
}
