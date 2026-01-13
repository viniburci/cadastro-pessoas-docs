package com.doban.cadastro_pessoas_docs.domain.pessoa;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para transferência de dados bancários.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DadosBancariosDTO {

    private Long id;
    private Long pessoaId;
    private String banco;
    private String agencia;
    private String conta;
    private TipoConta tipoConta;

    /**
     * Construtor a partir da entidade.
     *
     * @param dadosBancarios Entidade DadosBancarios
     */
    public DadosBancariosDTO(DadosBancarios dadosBancarios) {
        this.id = dadosBancarios.getId();
        this.pessoaId = dadosBancarios.getPessoa() != null ? dadosBancarios.getPessoa().getId() : null;
        this.banco = dadosBancarios.getBanco();
        this.agencia = dadosBancarios.getAgencia();
        this.conta = dadosBancarios.getConta();
        this.tipoConta = dadosBancarios.getTipoConta();
    }

    /**
     * Converte DTO em entidade.
     *
     * @return Entidade DadosBancarios
     */
    public DadosBancarios toEntity() {
        return DadosBancarios.builder()
                .id(this.id)
                .banco(this.banco)
                .agencia(this.agencia)
                .conta(this.conta)
                .tipoConta(this.tipoConta)
                .build();
    }

    /**
     * Verifica se os dados bancários estão vazios.
     *
     * @return true se todos os campos estão vazios
     */
    public boolean isEmpty() {
        return (banco == null || banco.isBlank()) &&
               (agencia == null || agencia.isBlank()) &&
               (conta == null || conta.isBlank());
    }
}
