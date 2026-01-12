package com.doban.cadastro_pessoas_docs.recurso.dinamico;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para registrar devolução de recurso.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevolucaoDTO {

    @NotNull(message = "Data de devolução é obrigatória")
    private LocalDate dataDevolucao;
}
