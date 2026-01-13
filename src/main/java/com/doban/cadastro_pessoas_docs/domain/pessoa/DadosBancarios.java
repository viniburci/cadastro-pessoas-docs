package com.doban.cadastro_pessoas_docs.domain.pessoa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Entidade que representa os dados bancários de uma pessoa.
 * Relacionamento 1:1 com Pessoa.
 */
@Entity
@Table(name = "dados_bancarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DadosBancarios {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pessoa_id", nullable = false, unique = true)
    private Pessoa pessoa;

    /**
     * Nome do banco (ex: ITAU, BRADESCO, CAIXA, BANCO DO BRASIL)
     */
    @Column(length = 100)
    private String banco;

    /**
     * Número da agência bancária
     */
    @Column(length = 20)
    private String agencia;

    /**
     * Número da conta bancária
     */
    @Column(length = 30)
    private String conta;

    /**
     * Tipo de conta bancária (CORRENTE, POUPANCA, SALARIO)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_conta", length = 20)
    private TipoConta tipoConta;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
