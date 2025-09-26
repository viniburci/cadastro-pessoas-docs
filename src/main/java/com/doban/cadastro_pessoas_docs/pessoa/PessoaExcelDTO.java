package com.doban.cadastro_pessoas_docs.pessoa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import com.doban.cadastro_pessoas_docs.vaga.TipoContrato;
import com.doban.cadastro_pessoas_docs.vaga.Vaga;

import lombok.Data;

@Data
public class PessoaExcelDTO {

    //Pessoa
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


    //Vaga

    private String contratante;
    private String cliente;
    private String setor;
    private String cargo;
    private String cidadeVaga;
    private String ufVaga;
    private BigDecimal salario;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;
    private String acrescOuSubst; // Acresc ou substituição
    private TipoContrato tipoContrato;   // CLT ou TEMP
    private String numeroCnh;
    private String registroCnh;
    private String categoriaCnh;
    private LocalDate validadeCnh;
    private LocalTime horarioEntrada;
    private LocalTime horarioSaida;
    private String motivoContratacao;
    private Boolean optanteVT;
    private String aso; // AD, DEM, RET, POST, RUIDO ou outro
    private String matricula;

    //Carro (via Recurso)
    private String carroMarca;
    private String carroModelo;
    private String carroCor;
    private String carroChassi;
    private String carroPlaca;
    private String carroAnoModelo;
    private String carroTelefone;
    private String carroDdd;

    //Celular (via Recurso)
    private String celularMarca;
    private String celularModelo;
    private String celularChip;
    private String celularImei;

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


        // Vaga ativa (última ou conforme regra de negócio)
        Optional<Vaga> vagaOpt = pessoa.getVagas().stream()
                .filter(v -> v.getDataDemissao() == null || v.getDataDemissao().isAfter(LocalDate.now()))
                .findFirst();

        vagaOpt.ifPresent(vaga -> {
            this.contratante = vaga.getContratante();
            this.cliente = vaga.getCliente();
            this.setor = vaga.getSetor();
            this.cargo = vaga.getCargo();
            this.cidadeVaga = vaga.getCidade();
            this.ufVaga = vaga.getUf();
            this.salario = vaga.getSalario();
            this.dataAdmissao = vaga.getDataAdmissao();
            this.dataDemissao = vaga.getDataDemissao();
            this.acrescOuSubst = vaga.getAcrescOuSubst();
            this.tipoContrato = vaga.getTipoContrato(); 
            this.numeroCnh = vaga.getNumeroCnh();
            this.registroCnh = vaga.getRegistroCnh();
            this.categoriaCnh = vaga.getCategoriaCnh();
            this.validadeCnh = vaga.getValidadeCnh();
            this.horarioEntrada = vaga.getHorarioEntrada();
            this.horarioSaida = vaga.getHorarioSaida();
            this.motivoContratacao = vaga.getMotivoContratacao();
            this.optanteVT = vaga.getOptanteVT();
            this.aso = vaga.getAso();
            this.matricula = vaga.getMatricula();

            // Recursos vinculados
            vaga.getRecursos().forEach(recurso -> {
                if (recurso.getCarro() != null) {
                    this.carroMarca = recurso.getCarro().getMarca();
                    this.carroModelo = recurso.getCarro().getModelo();
                    this.carroCor = recurso.getCarro().getCor();
                    this.carroChassi = recurso.getCarro().getChassi();
                    this.carroPlaca = recurso.getCarro().getPlaca();
                    this.carroAnoModelo = recurso.getCarro().getAnoModelo();
                    this.carroTelefone = recurso.getCarro().getTelefone();
                    this.carroDdd = recurso.getCarro().getDdd();
                }
                if (recurso.getCelular() != null) {
                    this.celularMarca = recurso.getCelular().getMarca();
                    this.celularModelo = recurso.getCelular().getModelo();
                    this.celularChip = recurso.getCelular().getChip();
                    this.celularImei = recurso.getCelular().getImei();
                }
            });
        });
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
        return pessoa;
    }
}
