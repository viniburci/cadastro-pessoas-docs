package com.doban.cadastro_pessoas_docs.pessoa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.doban.cadastro_pessoas_docs.recurso.Recurso;
import com.doban.cadastro_pessoas_docs.vaga.Vaga;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "pessoas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String endereco;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private String ddd;
    private String telefone;
    private String email;
    private String numeroCtps;
    private String serieCtps;
    private LocalDate dataEmissaoCtps;
    private String numeroRg;
    private LocalDate dataEmissaoRg;
    private String ufRg;
    private String cpf;
    private String pis;
    private LocalDate dataEmissaoPis;
    private String tituloEleitor;
    private LocalDate dataNascimento;
    private String localNascimento;
    private String mae;
    private String pai;
    private String estadoCivil;

    private String categoriaCnh;
    private LocalDate validadeCnh;
    private String numeroCnh;
    private String registroCnh;

    private String chavePix;

    @Builder.Default
    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = false)
    @JsonManagedReference
    private List<Vaga> vagas = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "pessoa", cascade = CascadeType.ALL, orphanRemoval = false)
    private List<Recurso> recursos = new ArrayList<>();

    public void updateFromDTO(PessoaDTO dto) {
        this.nome = dto.getNome();
        this.endereco = dto.getEndereco();
        this.bairro = dto.getBairro();
        this.cidade = dto.getCidade();
        this.estado = dto.getEstado();
        this.cep = dto.getCep();
        this.ddd = dto.getDdd();
        this.telefone = dto.getTelefone();
        this.email = dto.getEmail();
        this.numeroCtps = dto.getNumeroCtps();
        this.serieCtps = dto.getSerieCtps();
        this.dataEmissaoCtps = dto.getDataEmissaoCtps();
        this.numeroRg = dto.getNumeroRg();
        this.dataEmissaoRg = dto.getDataEmissaoRg();
        this.ufRg = dto.getUfRg();
        this.cpf = dto.getCpf();
        this.pis = dto.getPis();
        this.dataEmissaoPis = dto.getDataEmissaoPis();
        this.tituloEleitor = dto.getTituloEleitor();
        this.dataNascimento = dto.getDataNascimento();
        this.localNascimento = dto.getLocalNascimento();
        this.mae = dto.getMae();
        this.pai = dto.getPai();
        this.estadoCivil = dto.getEstadoCivil();
        this.numeroCnh = dto.getNumeroCnh();
        this.registroCnh = dto.getRegistroCnh();
        this.categoriaCnh = dto.getCategoriaCnh();
        this.validadeCnh = dto.getValidadeCnh();
        this.chavePix = dto.getChavePix();
    }

}
