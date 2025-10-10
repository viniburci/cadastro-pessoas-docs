package com.doban.cadastro_pessoas_docs.vaga;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.doban.cadastro_pessoas_docs.pessoa.Pessoa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class VagaDTO {
    private Long id;
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

    public VagaDTO(Vaga vaga) {
        this.id = vaga.getId();
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
        Vaga.VagaBuilder vagaBuilder = Vaga.builder()
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
                .pessoa(pessoa);

        if (this.id != null) {
            vagaBuilder.id(this.id);
        }

        return vagaBuilder.build();
    }

    public void atualizarEntidade(Vaga vaga) {
        vaga.setCliente(this.cliente);
        vaga.setCidade(this.cidade);
        vaga.setUf(this.uf);
        vaga.setCargo(this.cargo);
        vaga.setSetor(this.setor);
        vaga.setSalario(this.salario);
        vaga.setTipoContrato(this.tipoContrato);
        vaga.setDataAdmissao(this.dataAdmissao);
        vaga.setDataDemissao(this.dataDemissao);
        vaga.setAcrescimoOuSubstituicao(this.acrescimoOuSubstituicao);
        vaga.setAso(this.aso);
        vaga.setOptanteVT(this.optanteVT);
        vaga.setHorarioEntrada(this.horarioEntrada);
        vaga.setHorarioSaida(this.horarioSaida);
    }
}
