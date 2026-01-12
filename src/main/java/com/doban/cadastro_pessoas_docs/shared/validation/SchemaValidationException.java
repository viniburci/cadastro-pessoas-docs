package com.doban.cadastro_pessoas_docs.shared.validation;

import lombok.Getter;

import java.util.List;

/**
 * Exceção lançada quando a validação de um schema falha.
 */
@Getter
public class SchemaValidationException extends RuntimeException {

    private final List<ValidationError> erros;

    public SchemaValidationException(List<ValidationError> erros) {
        super("Erro de validação de schema: " + erros.size() + " erro(s) encontrado(s)");
        this.erros = erros;
    }

    public SchemaValidationException(String campo, String mensagem) {
        super("Erro de validação de schema: " + mensagem);
        this.erros = List.of(new ValidationError(campo, mensagem));
    }
}
