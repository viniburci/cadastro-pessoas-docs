package com.doban.cadastro_pessoas_docs.shared.validation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Resultado de uma validação de schema.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationResult {
    private boolean valido;
    private List<ValidationError> erros = new ArrayList<>();

    public static ValidationResult sucesso() {
        return new ValidationResult(true, new ArrayList<>());
    }

    public static ValidationResult falha(List<ValidationError> erros) {
        return new ValidationResult(false, erros);
    }

    public void addErro(String campo, String mensagem) {
        erros.add(new ValidationError(campo, mensagem));
        valido = false;
    }
}
