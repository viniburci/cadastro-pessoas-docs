package com.doban.cadastro_pessoas_docs.recurso.recurso_celular;

import com.doban.cadastro_pessoas_docs.domain.celular.Celular;
import com.doban.cadastro_pessoas_docs.recurso.Recurso;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recursos_celulares")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RecursoCelular extends Recurso {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "celular_id", nullable = false)
    private Celular celular;
}
