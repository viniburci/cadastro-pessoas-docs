package com.doban.cadastro_pessoas_docs.documentos.template;

import com.doban.cadastro_pessoas_docs.shared.schema.FieldSchema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateDocumentoUpdateDTO {

    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    private String descricao;

    private String conteudoHtml;

    private FieldSchema schemaItens;

    private List<String> variaveisDisponiveis;

    private Boolean ativo;
}
