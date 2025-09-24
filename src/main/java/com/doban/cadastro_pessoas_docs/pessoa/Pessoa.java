package com.doban.cadastro_pessoas_docs.pessoa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.doban.cadastro_pessoas_docs.celular.Celular;
import com.doban.cadastro_pessoas_docs.vaga.Vaga;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String endereco;
    private String bairro;
    private String cep;
    private String cidade; 
    private String uf;
    private String fone;

    private LocalDate dataNascimento;
    private String localNascimento;
    private String pai;
    private String mae;

    private String numeroRg;
    private LocalDate dataEmissaoRg;
    private String ufRg;

    private String cpf;
    private String numeroCtps;
    private String serieCtps;
    private LocalDate dataEmissaoCtps;

    private String pis;
    private LocalDate dataPis;

    private String tituloEleitor;

    @OneToOne(mappedBy = "pessoa")
    private Celular celular;

    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL)
    private List<Vaga> vagas = new ArrayList<>();
}