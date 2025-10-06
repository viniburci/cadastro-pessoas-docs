package com.doban.cadastro_pessoas_docs.recurso;

import com.doban.cadastro_pessoas_docs.celular.Celular;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@jakarta.persistence.Table(name = "recursos_celulares")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RecursoRocadeira extends Recurso {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "celular_id", nullable = false)
    private Celular celular;
}