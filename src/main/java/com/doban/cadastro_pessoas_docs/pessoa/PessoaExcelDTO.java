package com.doban.cadastro_pessoas_docs.pessoa;
import com.doban.cadastro_pessoas_docs.carro.Carro;
import com.doban.cadastro_pessoas_docs.celular.Celular;
import com.doban.cadastro_pessoas_docs.vaga.AtestadoSaudeOcupacional;
import com.doban.cadastro_pessoas_docs.vaga.TipoAcrescimoSubstituicao;
import com.doban.cadastro_pessoas_docs.vaga.TipoContratante;
import com.doban.cadastro_pessoas_docs.vaga.TipoContrato;
import com.doban.cadastro_pessoas_docs.vaga.Vaga;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@AllArgsConstructor
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

        return pessoa;
    }

    // --- DTOs internos ---
    @Data
    @Builder
    public static class VagaDTO {
        private TipoContratante contratante;
        private String cliente;
        private String setor;
        private String cargo;
        private String cidade;
        private String uf;
        private BigDecimal salario;
        private LocalDate dataAdmissao;
        private LocalDate dataDemissao;
        private TipoAcrescimoSubstituicao acrescimoOuSubstituicao;
        private TipoContrato tipoContrato;
        private LocalTime horarioEntrada;
        private LocalTime horarioSaida;
        private String motivoContratacao;
        private Boolean optanteVT;
        private AtestadoSaudeOcupacional aso;

        public VagaDTO() {}

        public VagaDTO(TipoContratante contratante, String cliente, String setor, String cargo, String cidade, String uf, BigDecimal salario, LocalDate dataAdmissao, LocalDate dataDemissao, TipoAcrescimoSubstituicao acrescimoOuSubstituicao, TipoContrato tipoContrato, LocalTime horarioEntrada, LocalTime horarioSaida, String motivoContratacao, Boolean optanteVT, AtestadoSaudeOcupacional aso) {
            this.contratante = contratante;
            this.cliente = cliente;
            this.setor = setor;
            this.cargo = cargo;
            this.cidade = cidade;
            this.uf = uf;
            this.salario = salario;
            this.dataAdmissao = dataAdmissao;
            this.dataDemissao = dataDemissao;
            this.acrescimoOuSubstituicao = acrescimoOuSubstituicao;
            this.tipoContrato = tipoContrato;
            this.horarioEntrada = horarioEntrada;
            this.horarioSaida = horarioSaida;
            this.motivoContratacao = motivoContratacao;
            this.optanteVT = optanteVT;
            this.aso = aso;
        }

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
    @Builder
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
        
        public CarroDTO(String marca, String modelo, String cor, String chassi, String placa, String anoModelo, String telefone, String ddd) {
            this.marca = marca;
            this.modelo = modelo;
            this.cor = cor;
            this.chassi = chassi;
            this.placa = placa;
            this.anoModelo = anoModelo;
            this.telefone = telefone;
            this.ddd = ddd;
        }

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

        public Carro toEntity() {
            return new Carro(
                null, this.marca, this.modelo, this.cor, this.chassi,
                    this.placa, this.anoModelo, this.telefone, this.ddd);
        }
    }

    @Data
    @Builder
    public static class CelularDTO {
        private String marca;
        private String modelo;
        private String chip;
        private String imei;

        public CelularDTO() {}

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
