package com.doban.cadastro_pessoas_docs.vaga;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.doban.cadastro_pessoas_docs.pessoa.Pessoa;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VagaDTO {
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
    private Boolean optanteVT;
    private AtestadoSaudeOcupacional aso;

    public VagaDTO() {
    }

    public VagaDTO(TipoContratante contratante, String cliente, String setor, String cargo, String cidade,
            String uf, BigDecimal salario, LocalDate dataAdmissao, LocalDate dataDemissao,
            TipoAcrescimoSubstituicao acrescimoOuSubstituicao, TipoContrato tipoContrato, LocalTime horarioEntrada,
            LocalTime horarioSaida, Boolean optanteVT, AtestadoSaudeOcupacional aso) {
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
                .optanteVT(this.optanteVT)
                .aso(this.aso)
                .pessoa(pessoa)
                .build();
    }
}
