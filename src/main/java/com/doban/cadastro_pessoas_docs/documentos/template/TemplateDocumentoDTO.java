package com.doban.cadastro_pessoas_docs.documentos.template;

import com.doban.cadastro_pessoas_docs.domain.vaga.tipo.TipoVagaResumoDTO;
import com.doban.cadastro_pessoas_docs.shared.schema.FieldSchema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateDocumentoDTO {

    private Long id;
    private String codigo;
    private String nome;
    private String descricao;
    private String conteudoHtml;
    private FieldSchema schemaItens;
    private List<String> variaveisDisponiveis;
    private List<TipoVagaResumoDTO> tiposVagaPermitidos;
    private Boolean ativo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TemplateDocumentoDTO(TemplateDocumento entity) {
        this.id = entity.getId();
        this.codigo = entity.getCodigo();
        this.nome = entity.getNome();
        this.descricao = entity.getDescricao();
        this.conteudoHtml = entity.getConteudoHtml();
        this.schemaItens = entity.getSchemaItens();
        this.variaveisDisponiveis = entity.getVariaveisDisponiveis();
        this.ativo = entity.getAtivo();
        this.createdAt = entity.getCreatedAt();
        this.updatedAt = entity.getUpdatedAt();

        if (entity.getTiposVagaPermitidos() != null) {
            this.tiposVagaPermitidos = entity.getTiposVagaPermitidos().stream()
                    .map(tv -> new TipoVagaResumoDTO(tv.getId(), tv.getCodigo(), tv.getNome()))
                    .collect(Collectors.toList());
        }
    }
}
