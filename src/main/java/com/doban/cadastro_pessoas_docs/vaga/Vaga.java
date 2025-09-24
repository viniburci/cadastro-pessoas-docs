package com.doban.cadastro_pessoas_docs.vaga;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.doban.cadastro_pessoas_docs.carro.Carro;
import com.doban.cadastro_pessoas_docs.pessoa.Pessoa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Entity
@Data
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String contratante;
    private String cliente;
    private String setor;
    private String cargo;
    private String cidade;
    private String uf;
    private BigDecimal salario;

    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;

    private String tipoContrato;     // "acresc." ou "substit."
    private Regime regime;           // "CLT", "PJ" ou "TEMPORAIO"
    private String tipo;             // ex: "1 - clt_ce_cj"

    private String numeroCnh;
    private String registroCnh;
    private String categoriaCnh;
    private LocalDate validadeCnh;

    @Column(name = "horario_entrada")
    private LocalTime horarioEntrada;
    @Column(name = "horario_saida")
    private LocalTime horarioSaida;

    private String motivoContratacao;

    private boolean optanteVt;

    @Enumerated(EnumType.STRING)
    private AtestadoSaudeOcupacional atestadoSaudeOcupacional;
    private String matricula;

    @ManyToOne
    @JoinColumn(name = "pessoa_id")
    private Pessoa pessoa;

    @ManyToOne
    @JoinColumn(name = "carro_id")
    private Carro carro;
}