package com.doban.cadastro_pessoas_docs.carro;

import java.util.ArrayList;
import java.util.List;

import com.doban.cadastro_pessoas_docs.vaga.Vaga;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Carro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String marca;
    private String cor;
    private String chassi;
    private String placa;
    private String modelo;
    private String ddd;
    private String telefone;
    private String anoModelo;

    @OneToMany(mappedBy = "carro")
    private List<Vaga> vagas = new ArrayList<>();
}