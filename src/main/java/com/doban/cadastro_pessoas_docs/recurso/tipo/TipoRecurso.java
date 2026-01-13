package com.doban.cadastro_pessoas_docs.recurso.tipo;

import com.doban.cadastro_pessoas_docs.shared.schema.FieldSchema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entidade que representa um tipo de recurso dinâmico.
 * Define o schema de campos que os itens desse tipo devem seguir.
 */
@Entity
@Table(name = "tipos_recurso")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoRecurso {

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

    @Builder.Default
    private Boolean legado = false;

    @Column(columnDefinition = "jsonb")
    @org.hibernate.annotations.JdbcTypeCode(org.hibernate.type.SqlTypes.JSON)
    private FieldSchema schema;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
