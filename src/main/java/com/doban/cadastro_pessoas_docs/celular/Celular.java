package com.doban.cadastro_pessoas_docs.celular;

import com.doban.cadastro_pessoas_docs.pessoa.Pessoa;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Celular {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String modelo;
    private String chip;
    private String imei;

    @OneToOne
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;
} 