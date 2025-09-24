package com.doban.cadastro_pessoas_docs.recurso_entrega;

import java.time.LocalDate;

import com.doban.cadastro_pessoas_docs.carro.Carro;
import com.doban.cadastro_pessoas_docs.celular.Celular;
import com.doban.cadastro_pessoas_docs.pessoa.Pessoa;
import com.doban.cadastro_pessoas_docs.vaga.Vaga;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class RecursoEntrega {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataEntrega;
    private LocalDate dataRetorno;

    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;

    @ManyToOne
    @JoinColumn(name = "celular_id", nullable = true)
    private Celular celular;

    @ManyToOne
    @JoinColumn(name = "carro_id", nullable = true)
    private Carro carro;

    @ManyToOne
    @JoinColumn(name = "vaga_id", nullable = true)
    private Vaga vaga;
}

