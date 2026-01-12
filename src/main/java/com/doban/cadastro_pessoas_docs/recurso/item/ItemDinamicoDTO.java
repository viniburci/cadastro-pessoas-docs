package com.doban.cadastro_pessoas_docs.recurso.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO de resposta para ItemDinamico.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDinamicoDTO {

    private Long id;
    private Long tipoRecursoId;
    private String tipoRecursoCodigo;
    private String tipoRecursoNome;
    private String identificador;
    private Map<String, Object> atributos;
    private Boolean ativo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ItemDinamicoDTO(ItemDinamico entity) {
        this.id = entity.getId();
        this.tipoRecursoId = entity.getTipoRecurso().getId();
        this.tipoRecursoCodigo = entity.getTipoRecurso().getCodigo();
        this.tipoRecursoNome = entity.getTipoRecurso().getNome();
        this.identificador = entity.getIdentificador();
        this.atributos = entity.getAtributos();
        this.ativo = entity.getAtivo();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
