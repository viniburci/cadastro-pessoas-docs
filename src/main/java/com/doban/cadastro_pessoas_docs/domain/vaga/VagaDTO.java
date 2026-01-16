package com.doban.cadastro_pessoas_docs.domain.vaga;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import com.doban.cadastro_pessoas_docs.domain.pessoa.Pessoa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class VagaDTO {
    private Long id;
    private Long pessoaId;
    private TipoContratante contratante;
    private String cliente; // Campo legado
    private Long clienteId;
    private String clienteNome;
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

    // Campos dinâmicos
    private Long tipoVagaId;
    private String tipoVagaCodigo;
    private String tipoVagaNome;
    private Map<String, Object> atributosDinamicos;

    public VagaDTO() {
    }

    public VagaDTO(Vaga vaga) {
        this.id = vaga.getId();
        if (vaga.getPessoa() != null) {
            this.pessoaId = vaga.getPessoa().getId();
        }
        this.contratante = vaga.getContratante();
        this.cliente = vaga.getCliente();
        if (vaga.getClienteEntity() != null) {
            this.clienteId = vaga.getClienteEntity().getId();
            this.clienteNome = vaga.getClienteEntity().getNome();
        }
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

        // Campos dinâmicos
        if (vaga.getTipoVaga() != null) {
            this.tipoVagaId = vaga.getTipoVaga().getId();
            this.tipoVagaCodigo = vaga.getTipoVaga().getCodigo();
            this.tipoVagaNome = vaga.getTipoVaga().getNome();
        }
        this.atributosDinamicos = vaga.getAtributosDinamicos();
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
                .pessoa(pessoa)
                .atributosDinamicos(this.atributosDinamicos);

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
        if (this.atributosDinamicos != null) {
            vaga.setAtributosDinamicos(this.atributosDinamicos);
        }
    }
}
