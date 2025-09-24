package com.doban.cadastro_pessoas_docs.pessoa;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.doban.cadastro_pessoas_docs.vaga.Regime;

import lombok.Data;

@Data
public class PessoaExcelDTO {
	private String nome;
	private String endereco;
	private String bairro;
	private String cidade;
	private String uf;
	private String cep;
	private String fone;
	private String cpf;
	private String numeroRg;
	private LocalDate dataNascimento;
	private String localNascimento;
	private String mae;
	private String pai;

	private String numeroCtps;
	private String serieCtps;
	private LocalDate dataEmissaoCtps;

	private String pis;
	private LocalDate dataPis;

	private String tituloEleitor;

	// Vaga
	private String cliente;
	private String cidadeVaga;
	private String ufVaga;
	private String cargo;
	private String setor;
	private BigDecimal salario;
	private String tipoContrato;
	private LocalDate dataAdmissao;
	private LocalDate dataDemissao;
	private Regime regime;
	private LocalTime horarioEntrada;
	private LocalTime horarioSaida;
	private String motivoContratacao;
	private String contratante;
	private String matricula;

	// Carro
	private String carroMarca;
	private String carroCor;
	private String carroChassi;
	private String carroPlaca;
	private String carroModelo;
	private String carroDdd;
	private String carroTelefone;
	private String carroAnoModelo;

	// Celular
	private String celularMarca;
	private String celularModelo;
	private String celularChip;
	private String celularImei;

	// ---------- Construtores ----------
	public PessoaExcelDTO() {
	}

	public PessoaExcelDTO(Pessoa pessoa) {
		this.nome = pessoa.getNome();
		this.endereco = pessoa.getEndereco();
		this.bairro = pessoa.getBairro();
		this.cidade = pessoa.getCidade();
		this.uf = pessoa.getUf();
		this.cep = pessoa.getCep();
		this.fone = pessoa.getFone();
		this.cpf = pessoa.getCpf();
		this.numeroRg = pessoa.getNumeroRg();
		this.dataNascimento = pessoa.getDataNascimento();
		this.localNascimento = pessoa.getLocalNascimento();
		this.mae = pessoa.getMae();
		this.pai = pessoa.getPai();
		this.numeroCtps = pessoa.getNumeroCtps();
		this.serieCtps = pessoa.getSerieCtps();
		this.dataEmissaoCtps = pessoa.getDataEmissaoCtps();
		this.pis = pessoa.getPis();
		this.dataPis = pessoa.getDataPis();
		this.tituloEleitor = pessoa.getTituloEleitor();

		if (!pessoa.getVagas().isEmpty()) {
			var vaga = pessoa.getVagas().get(0);
			this.cliente = vaga.getCliente();
			this.cidadeVaga = vaga.getCidade();
			this.ufVaga = vaga.getUf();
			this.cargo = vaga.getCargo();
			this.setor = vaga.getSetor();
			this.salario = vaga.getSalario();
			this.tipoContrato = vaga.getTipo();
			this.dataAdmissao = vaga.getDataAdmissao();
			this.dataDemissao = vaga.getDataDemissao();
			this.regime = vaga.getRegime();
			this.horarioEntrada = vaga.getHorarioEntrada();
			this.horarioSaida = vaga.getHorarioSaida();
			this.motivoContratacao = vaga.getMotivoContratacao();
			this.contratante = vaga.getContratante();
			this.matricula = vaga.getMatricula();

			if (vaga.getCarro() != null) {
				this.carroMarca = vaga.getCarro().getMarca();
				this.carroCor = vaga.getCarro().getCor();
				this.carroChassi = vaga.getCarro().getChassi();
				this.carroPlaca = vaga.getCarro().getPlaca();
				this.carroModelo = vaga.getCarro().getModelo();
				this.carroDdd = vaga.getCarro().getDdd();
				this.carroTelefone = vaga.getCarro().getTelefone();
				this.carroAnoModelo = vaga.getCarro().getAnoModelo();
			}
		}

		if (pessoa.getCelular() != null) {
			this.celularMarca = pessoa.getCelular().getMarca();
			this.celularModelo = pessoa.getCelular().getModelo();
			this.celularChip = pessoa.getCelular().getChip();
			this.celularImei = pessoa.getCelular().getImei();
		}
	}

	// ---------- Método reverso ----------
	public Pessoa toEntity() {
		Pessoa pessoa = new Pessoa();
		pessoa.setNome(this.nome);
		pessoa.setEndereco(this.endereco);
		pessoa.setBairro(this.bairro);
		pessoa.setCidade(this.cidade);
		pessoa.setUf(this.uf);
		pessoa.setCep(this.cep);
		pessoa.setFone(this.fone);
		pessoa.setCpf(this.cpf);
		pessoa.setNumeroRg(this.numeroRg);
		pessoa.setDataNascimento(this.dataNascimento);
		pessoa.setLocalNascimento(this.localNascimento);
		pessoa.setMae(this.mae);
		pessoa.setPai(this.pai);
		pessoa.setNumeroCtps(this.numeroCtps);
		pessoa.setSerieCtps(this.serieCtps);
		pessoa.setDataEmissaoCtps(this.dataEmissaoCtps);
		pessoa.setPis(this.pis);
		pessoa.setDataPis(this.dataPis);
		pessoa.setTituloEleitor(this.tituloEleitor);
		return pessoa;
	}
}
