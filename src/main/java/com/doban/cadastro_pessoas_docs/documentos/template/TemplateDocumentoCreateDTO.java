package com.doban.cadastro_pessoas_docs.documentos.template;

import com.doban.cadastro_pessoas_docs.shared.schema.FieldSchema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class TemplateDocumentoCreateDTO {

    @NotBlank(message = "Código é obrigatório")
    @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
    @Pattern(regexp = "^[A-Z_][A-Z0-9_]*$", message = "Código deve conter apenas letras maiúsculas, números e underscore")
    private String codigo;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    private String descricao;

    @NotBlank(message = "Conteúdo HTML é obrigatório")
    private String conteudoHtml;

    private FieldSchema schemaItens;

    private List<String> variaveisDisponiveis;

    private List<Long> tiposVagaPermitidosIds;
}
