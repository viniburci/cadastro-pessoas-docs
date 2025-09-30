package com.doban.cadastro_pessoas_docs.pessoa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.doban.cadastro_pessoas_docs.carro.Carro;
import com.doban.cadastro_pessoas_docs.celular.Celular;
import com.doban.cadastro_pessoas_docs.recurso.Recurso;
import com.doban.cadastro_pessoas_docs.vaga.TipoContrato;
import com.doban.cadastro_pessoas_docs.vaga.Vaga;

import lombok.Data;

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

    // Vagas (histórico completo da pessoa)
    private List<VagaDTO> vagas = new ArrayList<>();

    // Recursos (diretamente na pessoa)
    private List<CarroDTO> carros = new ArrayList<>();
    private List<CelularDTO> celulares = new ArrayList<>();

    // -------------------- DTOs internos --------------------

    @Data
    public static class VagaDTO {
        private String contratante;
        private String cliente;
        private String setor;
        private String cargo;
        private String cidadeVaga;
        private String ufVaga;
        private BigDecimal salario;
        private LocalDate dataAdmissao;
        private LocalDate dataDemissao;
        private String acrescOuSubst;
        private TipoContrato tipoContrato;
        private String numeroCnh;
        private String registroCnh;
        private String categoriaCnh;
        private LocalDate validadeCnh;
        private LocalTime horarioEntrada;
        private LocalTime horarioSaida;
        private String motivoContratacao;
        private Boolean optanteVT;
        private String aso;
        private String matricula;
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
    }

    @Data
    public static class CelularDTO {
        private String marca;
        private String modelo;
        private String chip;
        private String imei;
    }

    // -------------------- Construtores --------------------

    public PessoaExcelDTO() {}

    public PessoaExcelDTO(Pessoa pessoa) {
        // Pessoa
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

        // Vagas
        this.vagas = pessoa.getVagas().stream().map(vaga -> {
            VagaDTO vdto = new VagaDTO();
            vdto.setContratante(vaga.getContratante());
            vdto.setCliente(vaga.getCliente());
            vdto.setSetor(vaga.getSetor());
            vdto.setCargo(vaga.getCargo());
            vdto.setCidadeVaga(vaga.getCidade());
            vdto.setUfVaga(vaga.getUf());
            vdto.setSalario(vaga.getSalario());
            vdto.setDataAdmissao(vaga.getDataAdmissao());
            vdto.setDataDemissao(vaga.getDataDemissao());
            vdto.setAcrescOuSubst(vaga.getAcrescOuSubst());
            vdto.setTipoContrato(vaga.getTipoContrato());
            vdto.setHorarioEntrada(vaga.getHorarioEntrada());
            vdto.setHorarioSaida(vaga.getHorarioSaida());
            vdto.setMotivoContratacao(vaga.getMotivoContratacao());
            vdto.setOptanteVT(vaga.getOptanteVT());
            vdto.setAso(vaga.getAso());
            vdto.setMatricula(vaga.getMatricula());
            return vdto;
        }).toList();

        // Recursos
        this.carros = pessoa.getRecursos().stream()
                .filter(r -> r instanceof Carro)
                .map(r -> {
                    Carro c = (Carro) r;
                    CarroDTO cdto = new CarroDTO();
                    cdto.setMarca(c.getMarca());
                    cdto.setModelo(c.getModelo());
                    cdto.setCor(c.getCor());
                    cdto.setChassi(c.getChassi());
                    cdto.setPlaca(c.getPlaca());
                    cdto.setAnoModelo(c.getAnoModelo());
                    cdto.setTelefone(c.getTelefone());
                    cdto.setDdd(c.getDdd());
                    return cdto;
                }).toList();

        this.celulares = pessoa.getRecursos().stream()
                .filter(r -> r instanceof Celular)
                .map(r -> {
                    Celular cel = (Celular) r;
                    CelularDTO cdto = new CelularDTO();
                    cdto.setMarca(cel.getMarca());
                    cdto.setModelo(cel.getModelo());
                    cdto.setChip(cel.getChip());
                    cdto.setImei(cel.getImei());
                    return cdto;
                }).toList();
    }

    // -------------------- Conversão inversa --------------------

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

        // Vagas
        List<Vaga> vagasEntity = this.vagas.stream().map(vdto -> {
            Vaga vaga = new Vaga();
            vaga.setContratante(vdto.getContratante());
            vaga.setCliente(vdto.getCliente());
            vaga.setSetor(vdto.getSetor());
            vaga.setCargo(vdto.getCargo());
            vaga.setCidade(vdto.getCidadeVaga());
            vaga.setUf(vdto.getUfVaga());
            vaga.setSalario(vdto.getSalario());
            vaga.setDataAdmissao(vdto.getDataAdmissao());
            vaga.setDataDemissao(vdto.getDataDemissao());
            vaga.setAcrescOuSubst(vdto.getAcrescOuSubst());
            vaga.setTipoContrato(vdto.getTipoContrato());
            vaga.setNumeroCnh(vdto.getNumeroCnh());
            vaga.setRegistroCnh(vdto.getRegistroCnh());
            vaga.setCategoriaCnh(vdto.getCategoriaCnh());
            vaga.setValidadeCnh(vdto.getValidadeCnh());
            vaga.setHorarioEntrada(vdto.getHorarioEntrada());
            vaga.setHorarioSaida(vdto.getHorarioSaida());
            vaga.setMotivoContratacao(vdto.getMotivoContratacao());
            vaga.setOptanteVT(vdto.getOptanteVT());
            vaga.setAso(vdto.getAso());
            vaga.setMatricula(vdto.getMatricula());
            return vaga;
        }).toList();

        pessoa.setVagas(vagasEntity);

        // Recursos
        List<Recurso> recursos = new ArrayList<>();
        this.carros.forEach(cdto -> {
            Carro carro = new Carro();
            carro.setMarca(cdto.getMarca());
            carro.setModelo(cdto.getModelo());
            carro.setCor(cdto.getCor());
            carro.setChassi(cdto.getChassi());
            carro.setPlaca(cdto.getPlaca());
            carro.setAnoModelo(cdto.getAnoModelo());
            carro.setTelefone(cdto.getTelefone());
            carro.setDdd(cdto.getDdd());
            recursos.add(carro);
        });
        this.celulares.forEach(cdto -> {
            Celular celular = new Celular();
            celular.setMarca(cdto.getMarca());
            celular.setModelo(cdto.getModelo());
            celular.setChip(cdto.getChip());
            celular.setImei(cdto.getImei());
            recursos.add(celular);
        });

        pessoa.setRecursos(recursos);

        return pessoa;
    }
}
