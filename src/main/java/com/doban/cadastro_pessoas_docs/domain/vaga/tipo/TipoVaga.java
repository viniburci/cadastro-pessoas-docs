package com.doban.cadastro_pessoas_docs.domain.vaga.tipo;

import com.doban.cadastro_pessoas_docs.recurso.tipo.TipoRecurso;
import com.doban.cadastro_pessoas_docs.shared.schema.FieldSchema;
import com.doban.cadastro_pessoas_docs.shared.schema.JsonSchemaConverter;
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

    @Column(columnDefinition = "jsonb")
    @Convert(converter = JsonSchemaConverter.class)
    private FieldSchema schema;

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
