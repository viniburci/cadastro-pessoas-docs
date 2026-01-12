package com.doban.cadastro_pessoas_docs.recurso.tipo;

import com.doban.cadastro_pessoas_docs.shared.schema.FieldSchema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * DTO de resposta para TipoRecurso.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoRecursoDTO {

    private Long id;
    private String codigo;
    private String nome;
    private String descricao;
    private Boolean ativo;
    private Boolean legado;
    private FieldSchema schema;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TipoRecursoDTO(TipoRecurso entity) {
        this.id = entity.getId();
        this.codigo = entity.getCodigo();
        this.nome = entity.getNome();
        this.descricao = entity.getDescricao();
        this.ativo = entity.getAtivo();
        this.legado = entity.getLegado();
        this.schema = entity.getSchema();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
