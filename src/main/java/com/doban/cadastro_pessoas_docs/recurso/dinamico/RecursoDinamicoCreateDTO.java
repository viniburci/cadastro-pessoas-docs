package com.doban.cadastro_pessoas_docs.recurso.dinamico;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO de criação para RecursoDinamico (empréstimo).
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecursoDinamicoCreateDTO {

    @NotNull(message = "ID da pessoa é obrigatório")
    private Long pessoaId;

    @NotNull(message = "ID do item é obrigatório")
    private Long itemId;

    @NotNull(message = "Data de entrega é obrigatória")
    private LocalDate dataEntrega;
}
