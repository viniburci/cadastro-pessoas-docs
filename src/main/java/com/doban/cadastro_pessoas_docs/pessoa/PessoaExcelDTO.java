package com.doban.cadastro_pessoas_docs.pessoa;
import com.doban.cadastro_pessoas_docs.vaga.TipoContrato;
import com.doban.cadastro_pessoas_docs.vaga.Vaga;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PessoaExcelDTO {

    // Pessoa
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
    private String matricula;

    //++++++++++++++++++CONSTRUTORES++++++++++++++++++
    public PessoaExcelDTO() {}

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
        this.matricula = pessoa.getMatricula();

        // Vagas
        this.vagas = pessoa.getVagas().stream()
                .map(VagaDTO::new)
                .collect(Collectors.toList());

        // Recursos (carros e celulares)
        this.carros = pessoa.getRecursos().stream()
                .filter(r -> r instanceof RecursoCarro)
                .map(r -> new RecursoCarroDTO((RecursoCarro) r))
                .collect(Collectors.toList());

        this.celulares = pessoa.getRecursos().stream()
                .filter(r -> r instanceof RecursoCelular)
                .map(r -> new RecursoCelularDTO((RecursoCelular) r))
                .collect(Collectors.toList());
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

        // Vagas
        if (this.vagas != null) {
            this.vagas.forEach(v -> pessoa.getVagas().add(v.toEntity(pessoa)));
        }

        // Carros
        if (this.carros != null) {
            this.carros.forEach(c -> pessoa.getRecursos().add(c.toEntity(pessoa)));
        }

        // Celulares
        if (this.celulares != null) {
            this.celulares.forEach(c -> pessoa.getRecursos().add(c.toEntity(pessoa)));
        }

        return pessoa;
    }

    // --- DTOs internos ---
    @Data
    public static class VagaDTO {
        private String contratante;
        private String cliente;
        private String setor;
        private String cargo;
        private String cidade;
        private String uf;
        private BigDecimal salario;
        private LocalDate dataAdmissao;
        private LocalDate dataDemissao;
        private String acrescimoOuSubstituicao;
        private TipoContrato tipoContrato;
        private LocalTime horarioEntrada;
        private LocalTime horarioSaida;
        private String motivoContratacao;
        private Boolean optanteVT;
        private String aso;

        public VagaDTO() {}
        public VagaDTO(Vaga vaga) {
            this.contratante = vaga.getContratante();
            this.cliente = vaga.getCliente();
            this.setor = vaga.getSetor();
            this.cargo = vaga.getCargo();
            this.cidade = vaga.getCidade();
            this.uf = vaga.getUf();
            this.salario = vaga.getSalario();
            this.dataAdmissao = vaga.getDataAdmissao();
            this.dataDemissao = vaga.getDataDemissao();
            this.acrescimoOuSubstituicao = vaga.getAcrescimoOuSubstituicao();
            this.tipoContrato = vaga.getTipoContrato();
            this.horarioEntrada = vaga.getHorarioEntrada();
            this.horarioSaida = vaga.getHorarioSaida();
            this.motivoContratacao = vaga.getMotivoContratacao();
            this.optanteVT = vaga.getOptanteVT();
            this.aso = vaga.getAso();
        }

        public Vaga toEntity(Pessoa pessoa) {
            return Vaga.builder()
                    .contratante(this.contratante)
                    .cliente(this.cliente)
                    .setor(this.setor)
                    .cargo(this.cargo)
                    .cidade(this.cidade)
                    .uf(this.uf)
                    .salario(this.salario)
                    .dataAdmissao(this.dataAdmissao)
                    .dataDemissao(this.dataDemissao)
                    .acrescimoOuSubstituicao(this.acrescimoOuSubstituicao)
                    .tipoContrato(this.tipoContrato)
                    .horarioEntrada(this.horarioEntrada)
                    .horarioSaida(this.horarioSaida)
                    .motivoContratacao(this.motivoContratacao)
                    .optanteVT(this.optanteVT)
                    .aso(this.aso)
                    .pessoa(pessoa)
                    .build();
        }
    }

    @Data
    public static class CarroDTO {
        private String marca;
        private String modelo;
        private String cor;
        private String chassi;
        private String placa;
        private String anoModelo;
        private String telefone;
        private String ddd;

        public CarroDTO() {}
        public CarroDTO(CarroDTO carro) {
            this.marca = carro.getMarca();
            this.modelo = carro.getModelo();
            this.cor = carro.getCor();
            this.chassi = carro.getChassi();
            this.placa = carro.getPlaca();
            this.anoModelo = carro.getAnoModelo();
            this.telefone = carro.getTelefone();
            this.ddd = carro.getDdd();
        }

        public CarroDTO toEntity(Pessoa pessoa) {
            return new RecursoCarro(null, null, null, pessoa,
                    this.marca, this.modelo, this.cor, this.chassi,
                    this.placa, this.anoModelo, this.telefone, this.ddd);
        }
    }

    @Data
    public static class CelularDTO {
        private String marca;
        private String modelo;
        private String chip;
        private String imei;

        public CelularDTO() {}
        public CelularDTO(CelularDTO celular) {
            this.marca = celular.getMarca();
            this.modelo = celular.getModelo();
            this.chip = celular.getChip();
            this.imei = celular.getImei();
        }

        public CelularDTO toEntity(Pessoa pessoa) {
            return new RecursoCelular(null, null, null, pessoa,
                    this.marca, this.modelo, this.chip, this.imei);
        }
    }
}
