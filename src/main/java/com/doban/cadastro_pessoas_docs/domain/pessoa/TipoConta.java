package com.doban.cadastro_pessoas_docs.domain.pessoa;

/**
 * Enum que representa os tipos de conta bancária.
 */
public enum TipoConta {
    /**
     * Conta Corrente - conta bancária padrão para movimentações diárias
     */
    CORRENTE,

    /**
     * Conta Poupança - conta para investimento com rendimento
     */
    POUPANCA,

    /**
     * Conta Salário - conta específica para recebimento de salário
     */
    SALARIO
}
