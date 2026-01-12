package com.doban.cadastro_pessoas_docs.recurso.tipo;

import com.doban.cadastro_pessoas_docs.shared.schema.FieldSchema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de atualização para TipoRecurso.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoRecursoUpdateDTO {

    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    private String descricao;

    @Valid
    private FieldSchema schema;

    private Boolean ativo;

    public void atualizarEntidade(TipoRecurso entity) {
        if (this.nome != null) {
            entity.setNome(this.nome);
        }
        if (this.descricao != null) {
            entity.setDescricao(this.descricao);
        }
        if (this.schema != null) {
            entity.setSchema(this.schema);
        }
        if (this.ativo != null) {
            entity.setAtivo(this.ativo);
        }
    }
}
