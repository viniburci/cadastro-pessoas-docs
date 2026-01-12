package com.doban.cadastro_pessoas_docs.domain.vaga.tipo;

import com.doban.cadastro_pessoas_docs.shared.schema.FieldSchema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO de criação para TipoVaga.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoVagaCreateDTO {

    @NotBlank(message = "Código é obrigatório")
    @Size(max = 50, message = "Código deve ter no máximo 50 caracteres")
    @Pattern(regexp = "^[A-Z_][A-Z0-9_]*$", message = "Código deve conter apenas letras maiúsculas, números e underscores")
    private String codigo;

    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    private String descricao;

    @Valid
    private FieldSchema schema;

    private List<Long> recursosPermitidosIds;

    public TipoVaga toEntity() {
        return TipoVaga.builder()
                .codigo(this.codigo)
                .nome(this.nome)
                .descricao(this.descricao)
                .schema(this.schema)
                .ativo(true)
                .build();
    }
}
