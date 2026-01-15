package com.doban.cadastro_pessoas_docs.recurso.dinamico;

import com.doban.cadastro_pessoas_docs.domain.pessoa.Pessoa;
import com.doban.cadastro_pessoas_docs.recurso.item.ItemDinamico;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Entidade que representa um empréstimo de recurso dinâmico para uma pessoa.
 */
@Entity
@Table(name = "recursos_dinamicos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecursoDinamico {

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private ItemDinamico item;

    private LocalDate dataEntrega;

    private LocalDate dataDevolucao;

    @Column(columnDefinition = "text")
    private String atributosSnapshotJson;

    @Transient
    @Builder.Default
    private Map<String, Object> atributosSnapshot = new HashMap<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    /**
     * Retorna os atributos do snapshot. Se o campo transient estiver vazio mas existir JSON,
     * deserializa do JSON.
     */
    public Map<String, Object> getAtributosSnapshot() {
        if (atributosSnapshot != null && !atributosSnapshot.isEmpty()) {
            return atributosSnapshot;
        }
        if (atributosSnapshotJson != null && !atributosSnapshotJson.isEmpty()) {
            try {
                atributosSnapshot = mapper.readValue(atributosSnapshotJson, MAP_TYPE);
            } catch (JsonProcessingException e) {
                System.err.println("Erro ao deserializar atributos snapshot: " + e.getMessage());
                return new HashMap<>();
            }
        }
        return atributosSnapshot != null ? atributosSnapshot : new HashMap<>();
    }

    /**
     * Define os atributos do snapshot e sincroniza com atributosSnapshotJson.
     */
    public void setAtributosSnapshot(Map<String, Object> atributosSnapshot) {
        this.atributosSnapshot = atributosSnapshot;
        serializarSnapshotParaJson();
    }

    /**
     * Callback JPA executado antes de persistir ou atualizar.
     * Garante que atributosSnapshotJson esteja sincronizado com atributosSnapshot.
     */
    @PrePersist
    @PreUpdate
    public void serializarSnapshotParaJson() {
        if (atributosSnapshot != null && !atributosSnapshot.isEmpty()) {
            try {
                this.atributosSnapshotJson = mapper.writeValueAsString(atributosSnapshot);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao serializar atributos snapshot", e);
            }
        }
    }
}
