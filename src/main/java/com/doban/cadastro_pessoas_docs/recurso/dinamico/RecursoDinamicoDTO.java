package com.doban.cadastro_pessoas_docs.recurso.dinamico;

import com.doban.cadastro_pessoas_docs.recurso.item.ItemDinamicoDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO de resposta para RecursoDinamico.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecursoDinamicoDTO {

    private Long id;
    private Long pessoaId;
    private String pessoaNome;
    private ItemDinamicoDTO item;
    private LocalDate dataEntrega;
    private LocalDate dataDevolucao;
    private Map<String, Object> atributosSnapshot;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RecursoDinamicoDTO(RecursoDinamico entity) {
        this.id = entity.getId();
        this.pessoaId = entity.getPessoa().getId();
        this.pessoaNome = entity.getPessoa().getNome();
        this.item = new ItemDinamicoDTO(entity.getItem());
        this.dataEntrega = entity.getDataEntrega();
        this.dataDevolucao = entity.getDataDevolucao();
        this.atributosSnapshot = entity.getAtributosSnapshot();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }
}
