package com.doban.cadastro_pessoas_docs.shared.schema;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Schema que define os campos dinâmicos de um tipo de recurso ou vaga.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldSchema {

    @JsonProperty("fields")
    @Builder.Default
    private List<FieldDefinition> fields = new ArrayList<>();

    /**
     * Obtém uma definição de campo pelo nome.
     */
    public FieldDefinition getField(String nome) {
        return fields.stream()
                .filter(f -> f.getNome().equals(nome))
                .findFirst()
                .orElse(null);
    }

    /**
     * Verifica se o schema contém um campo com o nome especificado.
     */
    public boolean hasField(String nome) {
        return fields.stream().anyMatch(f -> f.getNome().equals(nome));
    }

    /**
     * Retorna os nomes de todos os campos obrigatórios.
     * Anotado com @JsonIgnore para não ser serializado/deserializado.
     */
    @JsonIgnore
    public List<String> getCamposObrigatorios() {
        return fields.stream()
                .filter(f -> Boolean.TRUE.equals(f.getObrigatorio()))
                .map(FieldDefinition::getNome)
                .toList();
    }
}
