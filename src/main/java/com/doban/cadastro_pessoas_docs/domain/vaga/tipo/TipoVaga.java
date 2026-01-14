package com.doban.cadastro_pessoas_docs.domain.vaga.tipo;

import com.doban.cadastro_pessoas_docs.recurso.tipo.TipoRecurso;
import com.doban.cadastro_pessoas_docs.shared.schema.FieldSchema;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

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
        serializarSchemaParaJson();
    }

    /**
     * Callback JPA executado antes de persistir ou atualizar.
     * Garante que schemaJson esteja sincronizado com schema.
     */
    @PrePersist
    @PreUpdate
    public void serializarSchemaParaJson() {
        if (schema != null) {
            try {
                this.schemaJson = mapper.writeValueAsString(schema);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao serializar schema", e);
            }
        }
    }

    @Builder.Default
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tipo_vaga_tipo_recurso",
            joinColumns = @JoinColumn(name = "tipo_vaga_id"),
            inverseJoinColumns = @JoinColumn(name = "tipo_recurso_id")
    )
    private Set<TipoRecurso> recursosPermitidos = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void adicionarRecursoPermitido(TipoRecurso tipoRecurso) {
        recursosPermitidos.add(tipoRecurso);
    }

    public void removerRecursoPermitido(TipoRecurso tipoRecurso) {
        recursosPermitidos.remove(tipoRecurso);
    }

    public boolean permiteRecurso(TipoRecurso tipoRecurso) {
        return recursosPermitidos.contains(tipoRecurso);
    }

    public boolean permiteRecurso(String tipoRecursoCodigo) {
        return recursosPermitidos.stream()
                .anyMatch(tr -> tr.getCodigo().equals(tipoRecursoCodigo));
    }
}
