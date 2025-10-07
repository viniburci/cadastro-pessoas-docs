package com.doban.cadastro_pessoas_docs.pessoa;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PessoaDTO {
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

    private String numeroCnh;
    private String registroCnh;
    private String categoriaCnh;
    private LocalDate validadeCnh;

    private String chavePix;

    public PessoaDTO() {
    }

    public PessoaDTO(Pessoa pessoa) {
        this.id = pessoa.getId();
        this.nome = pessoa.getNome();
        this.endereco = pessoa.getEndereco();
        this.bairro = pessoa.getBairro();
        this.cidade = pessoa.getCidade();
        this.estado = pessoa.getEstado();
        this.cep = pessoa.getCep();
        this.ddd = pessoa.getDdd();
        this.telefone = pessoa.getTelefone();
        this.email = pessoa.getEmail();
        this.numeroCtps = pessoa.getNumeroCtps();
        this.serieCtps = pessoa.getSerieCtps();
        this.dataEmissaoCtps = pessoa.getDataEmissaoCtps();
        this.numeroRg = pessoa.getNumeroRg();
        this.dataEmissaoRg = pessoa.getDataEmissaoRg();
        this.ufRg = pessoa.getUfRg();
        this.cpf = pessoa.getCpf();
        this.pis = pessoa.getPis();
        this.dataEmissaoPis = pessoa.getDataEmissaoPis();
        this.tituloEleitor = pessoa.getTituloEleitor();
        this.dataNascimento = pessoa.getDataNascimento();
        this.localNascimento = pessoa.getLocalNascimento();
        this.mae = pessoa.getMae();
        this.pai = pessoa.getPai();
        this.estadoCivil = pessoa.getEstadoCivil();
        this.numeroCnh = pessoa.getNumeroCnh();
        this.registroCnh = pessoa.getRegistroCnh();
        this.categoriaCnh = pessoa.getCategoriaCnh();
        this.validadeCnh = pessoa.getValidadeCnh();
        this.chavePix = pessoa.getChavePix();
    }

    public Pessoa toEntity() {
        Pessoa pessoa = new Pessoa();
        if (this.id != null) {
            pessoa.setId(this.id);
        }
        pessoa.setNome(this.nome);
        pessoa.setEndereco(this.endereco);
        pessoa.setBairro(this.bairro);
        pessoa.setCidade(this.cidade);
        pessoa.setEstado(this.estado);
        pessoa.setCep(this.cep);
        pessoa.setDdd(this.ddd);
        pessoa.setTelefone(this.telefone);
        pessoa.setEmail(this.email);
        pessoa.setNumeroCtps(this.numeroCtps);
        pessoa.setSerieCtps(this.serieCtps);
        pessoa.setDataEmissaoCtps(this.dataEmissaoCtps);
        pessoa.setNumeroRg(this.numeroRg);
        pessoa.setDataEmissaoRg(this.dataEmissaoRg);
        pessoa.setUfRg(this.ufRg);
        pessoa.setCpf(this.cpf);
        pessoa.setPis(this.pis);
        pessoa.setDataEmissaoPis(this.dataEmissaoPis);
        pessoa.setTituloEleitor(this.tituloEleitor);
        pessoa.setDataNascimento(this.dataNascimento);
        pessoa.setLocalNascimento(this.localNascimento);
        pessoa.setMae(this.mae);
        pessoa.setPai(this.pai);
        pessoa.setEstadoCivil(this.estadoCivil);
        pessoa.setNumeroCnh(this.numeroCnh);
        pessoa.setRegistroCnh(this.registroCnh);
        pessoa.setCategoriaCnh(this.categoriaCnh);
        pessoa.setValidadeCnh(this.validadeCnh);
        pessoa.setChavePix(this.chavePix);

        return pessoa;
    }
}
