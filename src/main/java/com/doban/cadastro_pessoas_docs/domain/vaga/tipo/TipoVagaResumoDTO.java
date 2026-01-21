package com.doban.cadastro_pessoas_docs.domain.vaga.tipo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO resumido de TipoVaga para uso em outros DTOs.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoVagaResumoDTO {

    private Long id;
    private String codigo;
    private String nome;

    public TipoVagaResumoDTO(TipoVaga entity) {
        this.id = entity.getId();
        this.codigo = entity.getCodigo();
        this.nome = entity.getNome();
    }
}
