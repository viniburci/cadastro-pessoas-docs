package com.doban.cadastro_pessoas_docs.domain.vaga.tipo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.doban.cadastro_pessoas_docs.shared.schema.FieldSchema;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de resposta para TipoVaga.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoVagaDTO {

    private Long id;
    private String codigo;
    private String nome;
    private String descricao;
    private Boolean ativo;
    private FieldSchema schema;
    private List<Map<String, Object>> itensPadrao;
    private List<TipoRecursoResumoDTO> recursosPermitidos;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TipoVagaDTO(TipoVaga entity) {
        this.id = entity.getId();
        this.codigo = entity.getCodigo();
        this.nome = entity.getNome();
        this.descricao = entity.getDescricao();
        this.ativo = entity.getAtivo();
        this.schema = entity.getSchema();
        this.itensPadrao = entity.getItensPadrao();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();

        if (entity.getRecursosPermitidos() != null) {
            this.recursosPermitidos = entity.getRecursosPermitidos().stream()
                    .map(TipoRecursoResumoDTO::new)
                    .collect(Collectors.toList());
        }
    }
}
