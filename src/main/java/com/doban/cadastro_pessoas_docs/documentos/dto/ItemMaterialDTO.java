package com.doban.cadastro_pessoas_docs.documentos.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemMaterialDTO {
    private Integer quantidade;
    private String marca;
    private String descricao;
    private String numeroSerie;
    private String ddd;
    private BigDecimal valor;
}
