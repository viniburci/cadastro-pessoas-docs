package com.doban.cadastro_pessoas_docs.shared.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa um erro de validação de campo.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {
    private String campo;
    private String mensagem;
}
