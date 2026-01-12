package com.doban.cadastro_pessoas_docs.recurso.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO de atualização para ItemDinamico.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDinamicoUpdateDTO {

    private String identificador;
    private Map<String, Object> atributos;
    private Boolean ativo;
}
