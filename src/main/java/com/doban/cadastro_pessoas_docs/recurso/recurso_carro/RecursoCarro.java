package com.doban.cadastro_pessoas_docs.recurso.recurso_carro;


import com.doban.cadastro_pessoas_docs.domain.carro.Carro;
import com.doban.cadastro_pessoas_docs.recurso.Recurso;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "recursos_carros")
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class RecursoCarro extends Recurso {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "carro_id", nullable = false)
    private Carro carro;
}
