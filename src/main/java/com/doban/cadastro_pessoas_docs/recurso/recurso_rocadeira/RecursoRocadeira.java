package com.doban.cadastro_pessoas_docs.recurso.recurso_rocadeira;

import com.doban.cadastro_pessoas_docs.recurso.Recurso;
import com.doban.cadastro_pessoas_docs.rocadeira.Rocadeira;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recursos_rocadeiras")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RecursoRocadeira extends Recurso {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rocadeira_id", nullable = false)
    private Rocadeira rocadeira;
}