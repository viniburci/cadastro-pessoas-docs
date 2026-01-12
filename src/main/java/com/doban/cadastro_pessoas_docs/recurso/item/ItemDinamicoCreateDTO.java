package com.doban.cadastro_pessoas_docs.recurso.item;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * DTO de criação para ItemDinamico.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDinamicoCreateDTO {

    @NotBlank(message = "Código do tipo de recurso é obrigatório")
    private String tipoRecursoCodigo;

    @NotBlank(message = "Identificador é obrigatório")
    private String identificador;

    @NotNull(message = "Atributos são obrigatórios")
    private Map<String, Object> atributos;
}
