package com.doban.cadastro_pessoas_docs.domain.vaga.tipo;

import com.doban.cadastro_pessoas_docs.shared.schema.FieldSchema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * DTO de atualização para TipoVaga.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoVagaUpdateDTO {

    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    private String nome;

    private String descricao;

    @Valid
    private FieldSchema schema;

    private List<Map<String, Object>> itensPadrao;

    private Boolean ativo;

    public void atualizarEntidade(TipoVaga entity) {
        if (this.nome != null) {
            entity.setNome(this.nome);
        }
        if (this.descricao != null) {
            entity.setDescricao(this.descricao);
        }
        if (this.schema != null) {
            entity.setSchema(this.schema);
        }
        if (this.itensPadrao != null) {
            entity.setItensPadrao(this.itensPadrao);
        }
        if (this.ativo != null) {
            entity.setAtivo(this.ativo);
        }
    }
}
