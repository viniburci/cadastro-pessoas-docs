package com.doban.cadastro_pessoas_docs.domain.cliente;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClienteDTO {

    private Long id;
    private String nome;
    private String descricao;
    private Boolean ativo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ClienteDTO(Cliente entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.descricao = entity.getDescricao();
        this.ativo = entity.getAtivo();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();
    }

    public Cliente toEntity() {
        return Cliente.builder()
                .id(this.id)
                .nome(this.nome)
                .descricao(this.descricao)
                .ativo(this.ativo != null ? this.ativo : true)
                .build();
    }
}
