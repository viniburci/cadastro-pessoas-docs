package com.doban.cadastro_pessoas_docs.recurso.item;

import com.doban.cadastro_pessoas_docs.recurso.tipo.TipoRecurso;
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
import java.util.HashMap;
import java.util.Map;

/**
 * Entidade que representa um item/equipamento dinâmico.
 * Os atributos são definidos pelo schema do TipoRecurso associado.
 */
@Entity
@Table(name = "itens_dinamicos",
        uniqueConstraints = @UniqueConstraint(columnNames = {"tipo_recurso_id", "identificador"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDinamico {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_recurso_id", nullable = false)
    private TipoRecurso tipoRecurso;

    @Column(nullable = false)
    private String identificador;

    @Column(columnDefinition = "text")
    private String atributosJson;

    @Transient
    @Builder.Default
    private Map<String, Object> atributos = new HashMap<>();

    /**
     * Retorna os atributos. Se o campo transient estiver vazio mas existir JSON,
     * deserializa do JSON.
     */
    public Map<String, Object> getAtributos() {
        if (atributos != null && !atributos.isEmpty()) {
            return atributos;
        }
        if (atributosJson != null && !atributosJson.isEmpty()) {
            try {
                atributos = mapper.readValue(atributosJson, MAP_TYPE);
            } catch (JsonProcessingException e) {
                System.err.println("Erro ao deserializar atributos: " + e.getMessage());
                return new HashMap<>();
            }
        }
        return atributos != null ? atributos : new HashMap<>();
    }

    /**
     * Define os atributos e sincroniza com atributosJson.
     */
    public void setAtributos(Map<String, Object> atributos) {
        this.atributos = atributos;
        serializarAtributosParaJson();
    }

    /**
     * Callback JPA executado antes de persistir ou atualizar.
     * Garante que atributosJson esteja sincronizado com atributos.
     */
    @PrePersist
    @PreUpdate
    public void serializarAtributosParaJson() {
        if (atributos != null && !atributos.isEmpty()) {
            try {
                this.atributosJson = mapper.writeValueAsString(atributos);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao serializar atributos", e);
            }
        }
    }

    @Builder.Default
    private Boolean ativo = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
