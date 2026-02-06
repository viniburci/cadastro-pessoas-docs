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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private static final TypeReference<List<Map<String, Object>>> LIST_MAP_TYPE = new TypeReference<>() {};

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

    @Column(columnDefinition = "text")
    private String itensExtrasJson;

    @Transient
    @Builder.Default
    private List<Map<String, Object>> itensExtras = new ArrayList<>();

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
        serializarParaJson();
    }

    /**
     * Retorna os itens extras. Se o campo transient estiver vazio mas existir JSON,
     * deserializa do JSON.
     */
    public List<Map<String, Object>> getItensExtras() {
        if (itensExtras != null && !itensExtras.isEmpty()) {
            return itensExtras;
        }
        if (itensExtrasJson != null && !itensExtrasJson.isEmpty()) {
            try {
                itensExtras = mapper.readValue(itensExtrasJson, LIST_MAP_TYPE);
            } catch (JsonProcessingException e) {
                System.err.println("Erro ao deserializar itens extras: " + e.getMessage());
                return new ArrayList<>();
            }
        }
        return itensExtras != null ? itensExtras : new ArrayList<>();
    }

    /**
     * Define os itens extras e sincroniza com itensExtrasJson.
     */
    public void setItensExtras(List<Map<String, Object>> itensExtras) {
        this.itensExtras = itensExtras;
        serializarItensExtrasParaJson();
    }

    /**
     * Callback JPA executado antes de persistir ou atualizar.
     * Garante que os campos JSON estejam sincronizados.
     */
    @PrePersist
    @PreUpdate
    public void serializarParaJson() {
        if (atributosSnapshot != null && !atributosSnapshot.isEmpty()) {
            try {
                this.atributosSnapshotJson = mapper.writeValueAsString(atributosSnapshot);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao serializar atributos snapshot", e);
            }
        } else {
            this.atributosSnapshotJson = null;
        }
        serializarItensExtrasParaJson();
    }

    private void serializarItensExtrasParaJson() {
        if (itensExtras != null && !itensExtras.isEmpty()) {
            try {
                this.itensExtrasJson = mapper.writeValueAsString(itensExtras);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao serializar itens extras", e);
            }
        } else {
            this.itensExtrasJson = null;
        }
    }
}
