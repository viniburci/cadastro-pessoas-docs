package com.doban.cadastro_pessoas_docs.pessoa;

import com.doban.cadastro_pessoas_docs.carro.Carro;
import com.doban.cadastro_pessoas_docs.celular.Celular;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
public class PessoaExcelDTO {

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

    // ++++++++++++++++++CONSTRUTORES++++++++++++++++++
    public PessoaExcelDTO() {
    }

    public PessoaExcelDTO(Pessoa pessoa) {
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

    // --- DTOs internos ---
    @Data
    @Builder
    public static class CarroDTO {
        private String marca;
        private String modelo;
        private String cor;
        private String chassi;
        private String placa;
        private String anoModelo;

        public CarroDTO() {
        }

        public CarroDTO(String marca, String modelo, String cor, String chassi, String placa, String anoModelo) {
            this.marca = marca;
            this.modelo = modelo;
            this.cor = cor;
            this.chassi = chassi;
            this.placa = placa;
            this.anoModelo = anoModelo;
        }

        public CarroDTO(CarroDTO carro) {
            this.marca = carro.getMarca();
            this.modelo = carro.getModelo();
            this.cor = carro.getCor();
            this.chassi = carro.getChassi();
            this.placa = carro.getPlaca();
            this.anoModelo = carro.getAnoModelo();
        }

        public Carro toEntity() {
            if (placa == null || placa.trim().isEmpty() || placa.equalsIgnoreCase("null") || placa.isBlank()
                    || placa.length() < 7) {
                return null;
            }
            return new Carro(
                    null, this.marca, this.modelo, this.cor, this.chassi,
                    this.placa, this.anoModelo);
        }
    }

    @Data
    @Builder
    public static class CelularDTO {
        private String marca;
        private String modelo;
        private String chip;
        private String imei;

        public CelularDTO() {
        }

        public CelularDTO(String marca, String modelo, String chip, String imei) {
            this.marca = marca;
            this.modelo = modelo;
            this.chip = chip;
            this.imei = imei;
        }

        public CelularDTO(CelularDTO celular) {
            this.marca = celular.getMarca();
            this.modelo = celular.getModelo();
            this.chip = celular.getChip();
            this.imei = celular.getImei();
        }

        public Celular toEntity() {
            return new Celular(
                    null, this.marca, this.modelo, this.chip, this.imei);
        }
    }
}
