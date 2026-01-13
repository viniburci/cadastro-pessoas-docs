package com.doban.cadastro_pessoas_docs.shared.schema;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Definição de um campo dinâmico dentro de um schema.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FieldDefinition {

    private String nome;           // Nome do campo (camelCase)
    private String rotulo;         // Rótulo para exibição
    private FieldType tipo;        // Tipo do campo

    @Builder.Default
    private Boolean obrigatorio = false;

    private String valorPadrao;    // Valor padrão
    private List<String> opcoes;   // Opções para tipo ENUM
    private Integer tamanhoMaximo; // Tamanho máximo para STRING
    private String regex;          // Padrão de validação
    private String mensagemErro;   // Mensagem de erro customizada

    private Double valorMinimo;    // Valor mínimo para INTEGER/DECIMAL
    private Double valorMaximo;    // Valor máximo para INTEGER/DECIMAL
}
