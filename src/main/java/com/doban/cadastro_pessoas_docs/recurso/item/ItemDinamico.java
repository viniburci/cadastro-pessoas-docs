package com.doban.cadastro_pessoas_docs.recurso.item;

import com.doban.cadastro_pessoas_docs.recurso.tipo.TipoRecurso;
import com.doban.cadastro_pessoas_docs.shared.schema.JsonMapConverter;
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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_recurso_id", nullable = false)
    private TipoRecurso tipoRecurso;

    @Column(nullable = false)
    private String identificador;

    @Builder.Default
    @Column(columnDefinition = "jsonb")
    @Convert(converter = JsonMapConverter.class)
    private Map<String, Object> atributos = new HashMap<>();

    @Builder.Default
    private Boolean ativo = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
