package com.doban.cadastro_pessoas_docs.documentos.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TermoResponsabilidadeMateriaisRequest {
    private Long clienteId;
    private List<Long> itemIds;
    private List<ItemMaterialDTO> itensAdicionais;
}
