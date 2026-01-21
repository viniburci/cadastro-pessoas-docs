package com.doban.cadastro_pessoas_docs.documentos.template;

import com.doban.cadastro_pessoas_docs.domain.vaga.tipo.TipoVaga;
import com.doban.cadastro_pessoas_docs.shared.schema.FieldSchema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Entidade que representa um template de documento customizável.
 * Permite criar documentos dinâmicos sem alterar o código backend.
 */
@Entity
@Table(name = "templates_documento")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateDocumento {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nome;

    private String descricao;

    @Column(columnDefinition = "text", nullable = false)
    private String conteudoHtml;

    @Column(columnDefinition = "text")
    private String schemaItensJson;

    @Transient
    private FieldSchema schemaItens;

    @Column(columnDefinition = "text")
    private String variaveisDisponiveisJson;

    @Transient
    @Builder.Default
    private List<String> variaveisDisponiveis = new ArrayList<>();

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "template_documento_tipo_vaga",
            joinColumns = @JoinColumn(name = "template_documento_id"),
            inverseJoinColumns = @JoinColumn(name = "tipo_vaga_id")
    )
    private Set<TipoVaga> tiposVagaPermitidos = new HashSet<>();

    @Builder.Default
    private Boolean ativo = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * Retorna o schema de itens. Se o campo transient estiver vazio mas existir JSON,
     * deserializa do JSON.
     */
    public FieldSchema getSchemaItens() {
        if (schemaItens != null) {
            return schemaItens;
        }
        if (schemaItensJson != null && !schemaItensJson.isEmpty()) {
            try {
                schemaItens = mapper.readValue(schemaItensJson, FieldSchema.class);
            } catch (JsonProcessingException e) {
                System.err.println("Erro ao deserializar schemaItens: " + e.getMessage());
                return null;
            }
        }
        return schemaItens;
    }

    /**
     * Define o schema de itens e sincroniza com schemaItensJson.
     */
    public void setSchemaItens(FieldSchema schemaItens) {
        this.schemaItens = schemaItens;
        serializarSchemaItensParaJson();
    }

    /**
     * Retorna as variáveis disponíveis. Se o campo transient estiver vazio mas existir JSON,
     * deserializa do JSON.
     */
    public List<String> getVariaveisDisponiveis() {
        if (variaveisDisponiveis != null && !variaveisDisponiveis.isEmpty()) {
            return variaveisDisponiveis;
        }
        if (variaveisDisponiveisJson != null && !variaveisDisponiveisJson.isEmpty()) {
            try {
                variaveisDisponiveis = mapper.readValue(variaveisDisponiveisJson, new TypeReference<List<String>>() {});
            } catch (JsonProcessingException e) {
                System.err.println("Erro ao deserializar variaveisDisponiveis: " + e.getMessage());
                return new ArrayList<>();
            }
        }
        return variaveisDisponiveis != null ? variaveisDisponiveis : new ArrayList<>();
    }

    /**
     * Define as variáveis disponíveis e sincroniza com variaveisDisponiveisJson.
     */
    public void setVariaveisDisponiveis(List<String> variaveisDisponiveis) {
        this.variaveisDisponiveis = variaveisDisponiveis;
        serializarVariaveisDisponiveisParaJson();
    }

    /**
     * Callback JPA executado antes de persistir ou atualizar.
     */
    @PrePersist
    @PreUpdate
    public void serializarParaJson() {
        serializarSchemaItensParaJson();
        serializarVariaveisDisponiveisParaJson();
    }

    private void serializarSchemaItensParaJson() {
        if (schemaItens != null) {
            try {
                this.schemaItensJson = mapper.writeValueAsString(schemaItens);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao serializar schemaItens", e);
            }
        }
    }

    private void serializarVariaveisDisponiveisParaJson() {
        if (variaveisDisponiveis != null && !variaveisDisponiveis.isEmpty()) {
            try {
                this.variaveisDisponiveisJson = mapper.writeValueAsString(variaveisDisponiveis);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao serializar variaveisDisponiveis", e);
            }
        }
    }

    public void adicionarTipoVagaPermitido(TipoVaga tipoVaga) {
        tiposVagaPermitidos.add(tipoVaga);
    }

    public void removerTipoVagaPermitido(TipoVaga tipoVaga) {
        tiposVagaPermitidos.remove(tipoVaga);
    }

    public boolean permiteTipoVaga(TipoVaga tipoVaga) {
        if (tiposVagaPermitidos == null || tiposVagaPermitidos.isEmpty()) {
            return true; // Se não há restrição, permite todos
        }
        return tiposVagaPermitidos.contains(tipoVaga);
    }

    public boolean permiteTipoVaga(String tipoVagaCodigo) {
        if (tiposVagaPermitidos == null || tiposVagaPermitidos.isEmpty()) {
            return true;
        }
        return tiposVagaPermitidos.stream()
                .anyMatch(tv -> tv.getCodigo().equals(tipoVagaCodigo));
    }
}
