package com.doban.cadastro_pessoas_docs.recurso;

import com.doban.cadastro_pessoas_docs.celular.Celular;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "recursos_celulares")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecursoCelular extends Recurso {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "celular_id", nullable = false)
    private Celular celular;
}
