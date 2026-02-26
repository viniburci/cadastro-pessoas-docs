package com.doban.cadastro_pessoas_docs.domain.vaga.tipo;

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
import java.util.List;
import java.util.Map;

/**
 * Entidade que representa um tipo de vaga dinâmico.
 * Define quais tipos de recursos são permitidos para esse tipo de vaga.
 */
@Entity
@Table(name = "tipos_vaga")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoVaga {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String codigo;

    @Column(nullable = false, length = 100)
    private String nome;

    private String descricao;

    @Builder.Default
    private Boolean ativo = true;

    @Column(columnDefinition = "text")
    private String schemaJson;

    @Transient
    private FieldSchema schema;

    @Column(columnDefinition = "text")
    private String itensPadraoJson;

    @Transient
    @Builder.Default
    private List<Map<String, Object>> itensPadrao = new ArrayList<>();

    /**
     * Retorna o schema. Se o campo transient estiver vazio mas existir JSON,
     * deserializa do JSON.
     */
    public FieldSchema getSchema() {
        if (schema != null) {
            return schema;
        }
        if (schemaJson != null && !schemaJson.isEmpty()) {
            try {
                schema = mapper.readValue(schemaJson, FieldSchema.class);
            } catch (JsonProcessingException e) {
                System.err.println("Erro ao deserializar schema: " + e.getMessage());
                return null;
            }
        }
        return schema;
    }

    /**
     * Define o schema e sincroniza com schemaJson.
     */
    public void setSchema(FieldSchema schema) {
        this.schema = schema;
        serializarParaJson();
    }

    /**
     * Retorna os itens padrão. Se o campo transient estiver vazio mas existir JSON,
     * deserializa do JSON.
     */
    public List<Map<String, Object>> getItensPadrao() {
        if (itensPadrao != null && !itensPadrao.isEmpty()) {
            return itensPadrao;
        }
        if (itensPadraoJson != null && !itensPadraoJson.isEmpty()) {
            try {
                itensPadrao = mapper.readValue(itensPadraoJson, new TypeReference<List<Map<String, Object>>>() {});
            } catch (JsonProcessingException e) {
                System.err.println("Erro ao deserializar itensPadrao: " + e.getMessage());
                return new ArrayList<>();
            }
        }
        return itensPadrao != null ? itensPadrao : new ArrayList<>();
    }

    /**
     * Define os itens padrão e sincroniza com itensPadraoJson.
     */
    public void setItensPadrao(List<Map<String, Object>> itensPadrao) {
        this.itensPadrao = itensPadrao;
        serializarItensPadraoParaJson();
    }

    /**
     * Callback JPA executado antes de persistir ou atualizar.
     * Garante que schemaJson e itensPadraoJson estejam sincronizados.
     */
    @PrePersist
    @PreUpdate
    public void serializarParaJson() {
        if (schema != null) {
            try {
                this.schemaJson = mapper.writeValueAsString(schema);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao serializar schema", e);
            }
        }
        serializarItensPadraoParaJson();
    }

    private void serializarItensPadraoParaJson() {
        if (itensPadrao != null && !itensPadrao.isEmpty()) {
            try {
                this.itensPadraoJson = mapper.writeValueAsString(itensPadrao);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao serializar itensPadrao", e);
            }
        }
    }

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
