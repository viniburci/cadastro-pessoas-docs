package com.doban.cadastro_pessoas_docs.shared.validation;

import com.doban.cadastro_pessoas_docs.shared.schema.FieldDefinition;
import com.doban.cadastro_pessoas_docs.shared.schema.FieldSchema;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Serviço responsável por validar dados contra um schema dinâmico.
 */
@Service
public class SchemaValidatorService {

    /**
     * Valida os dados contra o schema especificado.
     *
     * @param dados  Map com os valores dos campos
     * @param schema Schema com as definições dos campos
     * @return ValidationResult indicando se é válido e quais erros foram encontrados
     */
    public ValidationResult validar(Map<String, Object> dados, FieldSchema schema) {
        if (schema == null || schema.getFields() == null || schema.getFields().isEmpty()) {
            return ValidationResult.sucesso();
        }

        List<ValidationError> erros = new ArrayList<>();

        for (FieldDefinition field : schema.getFields()) {
            Object valor = dados != null ? dados.get(field.getNome()) : null;
            validarCampo(field, valor, erros);
        }

        return erros.isEmpty() ? ValidationResult.sucesso() : ValidationResult.falha(erros);
    }

    /**
     * Valida e lança exceção se houver erros.
     */
    public void validarOuLancarExcecao(Map<String, Object> dados, FieldSchema schema) {
        ValidationResult resultado = validar(dados, schema);
        if (!resultado.isValido()) {
            throw new SchemaValidationException(resultado.getErros());
        }
    }

    private void validarCampo(FieldDefinition field, Object valor, List<ValidationError> erros) {
        String nome = field.getNome();
        String rotulo = field.getRotulo() != null ? field.getRotulo() : nome;

        // Validação de obrigatoriedade
        if (Boolean.TRUE.equals(field.getObrigatorio()) && isVazio(valor)) {
            erros.add(new ValidationError(nome, "Campo '" + rotulo + "' é obrigatório"));
            return;
        }

        // Se o valor é nulo/vazio e não é obrigatório, não precisa validar mais
        if (isVazio(valor)) {
            return;
        }

        // Validação por tipo
        switch (field.getTipo()) {
            case STRING -> validarString(field, valor, erros);
            case INTEGER -> validarInteger(field, valor, erros);
            case DECIMAL -> validarDecimal(field, valor, erros);
            case DATE -> validarDate(field, valor, erros);
            case DATETIME -> validarDateTime(field, valor, erros);
            case BOOLEAN -> validarBoolean(field, valor, erros);
            case ENUM -> validarEnum(field, valor, erros);
        }
    }

    private void validarString(FieldDefinition field, Object valor, List<ValidationError> erros) {
        String nome = field.getNome();
        String rotulo = field.getRotulo() != null ? field.getRotulo() : nome;
        String str = valor.toString();

        // Validar tamanho máximo
        if (field.getTamanhoMaximo() != null && str.length() > field.getTamanhoMaximo()) {
            erros.add(new ValidationError(nome,
                    "Campo '" + rotulo + "' excede o tamanho máximo de " + field.getTamanhoMaximo() + " caracteres"));
        }

        // Validar regex
        if (field.getRegex() != null && !field.getRegex().isEmpty()) {
            if (!Pattern.matches(field.getRegex(), str)) {
                String mensagem = field.getMensagemErro() != null
                        ? field.getMensagemErro()
                        : "Campo '" + rotulo + "' possui formato inválido";
                erros.add(new ValidationError(nome, mensagem));
            }
        }
    }

    private void validarInteger(FieldDefinition field, Object valor, List<ValidationError> erros) {
        String nome = field.getNome();
        String rotulo = field.getRotulo() != null ? field.getRotulo() : nome;

        Long numero;
        try {
            if (valor instanceof Number) {
                numero = ((Number) valor).longValue();
            } else {
                numero = Long.parseLong(valor.toString());
            }
        } catch (NumberFormatException e) {
            erros.add(new ValidationError(nome, "Campo '" + rotulo + "' deve ser um número inteiro"));
            return;
        }

        validarLimitesNumericos(field, numero.doubleValue(), erros);
    }

    private void validarDecimal(FieldDefinition field, Object valor, List<ValidationError> erros) {
        String nome = field.getNome();
        String rotulo = field.getRotulo() != null ? field.getRotulo() : nome;

        Double numero;
        try {
            if (valor instanceof Number) {
                numero = ((Number) valor).doubleValue();
            } else {
                numero = new BigDecimal(valor.toString()).doubleValue();
            }
        } catch (NumberFormatException e) {
            erros.add(new ValidationError(nome, "Campo '" + rotulo + "' deve ser um número decimal"));
            return;
        }

        validarLimitesNumericos(field, numero, erros);
    }

    private void validarLimitesNumericos(FieldDefinition field, Double valor, List<ValidationError> erros) {
        String nome = field.getNome();
        String rotulo = field.getRotulo() != null ? field.getRotulo() : nome;

        if (field.getValorMinimo() != null && valor < field.getValorMinimo()) {
            erros.add(new ValidationError(nome,
                    "Campo '" + rotulo + "' deve ser maior ou igual a " + field.getValorMinimo()));
        }

        if (field.getValorMaximo() != null && valor > field.getValorMaximo()) {
            erros.add(new ValidationError(nome,
                    "Campo '" + rotulo + "' deve ser menor ou igual a " + field.getValorMaximo()));
        }
    }

    private void validarDate(FieldDefinition field, Object valor, List<ValidationError> erros) {
        String nome = field.getNome();
        String rotulo = field.getRotulo() != null ? field.getRotulo() : nome;

        if (valor instanceof LocalDate) {
            return; // Já é uma data válida
        }

        try {
            LocalDate.parse(valor.toString());
        } catch (DateTimeParseException e) {
            erros.add(new ValidationError(nome,
                    "Campo '" + rotulo + "' deve ser uma data válida (formato: AAAA-MM-DD)"));
        }
    }

    private void validarDateTime(FieldDefinition field, Object valor, List<ValidationError> erros) {
        String nome = field.getNome();
        String rotulo = field.getRotulo() != null ? field.getRotulo() : nome;

        if (valor instanceof LocalDateTime) {
            return; // Já é um datetime válido
        }

        try {
            LocalDateTime.parse(valor.toString());
        } catch (DateTimeParseException e) {
            erros.add(new ValidationError(nome,
                    "Campo '" + rotulo + "' deve ser uma data/hora válida (formato: AAAA-MM-DDTHH:MM:SS)"));
        }
    }

    private void validarBoolean(FieldDefinition field, Object valor, List<ValidationError> erros) {
        String nome = field.getNome();
        String rotulo = field.getRotulo() != null ? field.getRotulo() : nome;

        if (valor instanceof Boolean) {
            return; // Já é booleano
        }

        String str = valor.toString().toLowerCase();
        if (!str.equals("true") && !str.equals("false")) {
            erros.add(new ValidationError(nome,
                    "Campo '" + rotulo + "' deve ser verdadeiro ou falso"));
        }
    }

    private void validarEnum(FieldDefinition field, Object valor, List<ValidationError> erros) {
        String nome = field.getNome();
        String rotulo = field.getRotulo() != null ? field.getRotulo() : nome;

        if (field.getOpcoes() == null || field.getOpcoes().isEmpty()) {
            return; // Sem opções definidas, aceita qualquer valor
        }

        String str = valor.toString();
        if (!field.getOpcoes().contains(str)) {
            erros.add(new ValidationError(nome,
                    "Campo '" + rotulo + "' deve ser um dos valores: " + String.join(", ", field.getOpcoes())));
        }
    }

    private boolean isVazio(Object valor) {
        if (valor == null) {
            return true;
        }
        if (valor instanceof String str) {
            return str.trim().isEmpty();
        }
        return false;
    }
}
