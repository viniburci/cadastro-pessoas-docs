package com.doban.cadastro_pessoas_docs.domain.vaga.tipo;

import com.doban.cadastro_pessoas_docs.recurso.tipo.TipoRecurso;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO resumido de TipoRecurso para uso dentro de TipoVagaDTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoRecursoResumoDTO {

    private Long id;
    private String codigo;
    private String nome;

    public TipoRecursoResumoDTO(TipoRecurso entity) {
        this.id = entity.getId();
        this.codigo = entity.getCodigo();
        this.nome = entity.getNome();
    }
}
