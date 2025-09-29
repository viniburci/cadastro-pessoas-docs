package com.doban.cadastro_pessoas_docs.vaga;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.doban.cadastro_pessoas_docs.pessoa.Pessoa;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vagas")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cliente;
    private String cidade;
    private String uf;
    private String cargo;
    private String setor;
    private BigDecimal salario;
    private TipoContrato tipoContrato;
    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;
    private String acrescimoOuSubstituicao;
    private String aso;
    private Boolean optanteVT;
    private LocalTime horarioEntrada;
    private LocalTime horarioSaida;
    private String motivoContratacao;
    private String contratante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    private Pessoa pessoa;
}
