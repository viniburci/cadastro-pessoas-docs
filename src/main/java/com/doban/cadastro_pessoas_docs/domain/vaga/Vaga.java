package com.doban.cadastro_pessoas_docs.domain.vaga;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import com.doban.cadastro_pessoas_docs.domain.cliente.Cliente;
import com.doban.cadastro_pessoas_docs.domain.pessoa.Pessoa;
import com.doban.cadastro_pessoas_docs.domain.vaga.tipo.TipoVaga;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
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

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {};

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente clienteEntity;

    private String cidade;
    private String uf;
    private String setor;

    @Column(precision = 10, scale = 2)
    private BigDecimal salario;

    @Enumerated(EnumType.STRING)
    private TipoContrato tipoContrato;

    private LocalDate dataAdmissao;
    private LocalDate dataDemissao;

    @Enumerated(EnumType.STRING)
    private TipoAcrescimoSubstituicao acrescimoOuSubstituicao;

    @Enumerated(EnumType.STRING)
    private AtestadoSaudeOcupacional aso;

    private Boolean optanteVT;
    private LocalTime horarioEntrada;
    private LocalTime horarioSaida;

    @Enumerated(EnumType.STRING)
    private TipoContratante contratante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pessoa_id", nullable = false)
    @JsonBackReference
    private Pessoa pessoa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_vaga_id")
    private TipoVaga tipoVaga;

    @Column(columnDefinition = "text")
    private String atributosDinamicosJson;

    @Transient
    @Builder.Default
    private Map<String, Object> atributosDinamicos = new HashMap<>();

    /**
     * Retorna os atributos dinâmicos. Se o campo transient estiver vazio mas existir JSON,
     * deserializa do JSON.
     */
    public Map<String, Object> getAtributosDinamicos() {
        if (atributosDinamicos != null && !atributosDinamicos.isEmpty()) {
            return atributosDinamicos;
        }
        if (atributosDinamicosJson != null && !atributosDinamicosJson.isEmpty()) {
            try {
                atributosDinamicos = mapper.readValue(atributosDinamicosJson, MAP_TYPE);
            } catch (JsonProcessingException e) {
                System.err.println("Erro ao deserializar atributosDinamicos: " + e.getMessage());
                return new HashMap<>();
            }
        }
        return atributosDinamicos != null ? atributosDinamicos : new HashMap<>();
    }

    /**
     * Define os atributos dinâmicos e sincroniza com atributosDinamicosJson.
     */
    public void setAtributosDinamicos(Map<String, Object> atributosDinamicos) {
        this.atributosDinamicos = atributosDinamicos;
        serializarAtributosDinamicosParaJson();
    }

    /**
     * Callback JPA executado antes de persistir ou atualizar.
     * Garante que atributosDinamicosJson esteja sincronizado com atributosDinamicos.
     */
    @PrePersist
    @PreUpdate
    public void serializarAtributosDinamicosParaJson() {
        if (atributosDinamicos != null && !atributosDinamicos.isEmpty()) {
            try {
                this.atributosDinamicosJson = mapper.writeValueAsString(atributosDinamicos);
            } catch (JsonProcessingException e) {
                throw new RuntimeException("Erro ao serializar atributosDinamicos", e);
            }
        }
    }
}
