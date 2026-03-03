package com.doban.cadastro_pessoas_docs.shared.validation;

import com.doban.cadastro_pessoas_docs.shared.schema.FieldDefinition;
import com.doban.cadastro_pessoas_docs.shared.schema.FieldSchema;
import com.doban.cadastro_pessoas_docs.shared.schema.FieldType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class SchemaValidatorServiceTest {

    private SchemaValidatorService service;

    @BeforeEach
    void setUp() {
        service = new SchemaValidatorService();
    }

    @Test
    void validar_schemaNulo_retornaSucesso() {
        ValidationResult result = service.validar(Map.of(), null);
        assertThat(result.isValido()).isTrue();
    }

    @Test
    void validar_schemaVazio_retornaSucesso() {
        FieldSchema schema = FieldSchema.builder().fields(List.of()).build();
        ValidationResult result = service.validar(Map.of(), schema);
        assertThat(result.isValido()).isTrue();
    }

    @Test
    void validar_campoObrigatorioAusente_retornaErro() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("nome")
                .rotulo("Nome")
                .tipo(FieldType.STRING)
                .obrigatorio(true)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();

        ValidationResult result = service.validar(Map.of(), schema);

        assertThat(result.isValido()).isFalse();
        assertThat(result.getErros()).hasSize(1);
        assertThat(result.getErros().get(0).getCampo()).isEqualTo("nome");
    }

    @Test
    void validar_campoOpcionalAusente_retornaSucesso() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("observacao")
                .tipo(FieldType.STRING)
                .obrigatorio(false)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();

        ValidationResult result = service.validar(Map.of(), schema);

        assertThat(result.isValido()).isTrue();
    }

    @Test
    void validar_stringDentroDoTamanhoMaximo_retornaSucesso() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("codigo")
                .tipo(FieldType.STRING)
                .tamanhoMaximo(10)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("codigo", "ABC123");

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isTrue();
    }

    @Test
    void validar_stringExcedeTamanhoMaximo_retornaErro() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("codigo")
                .rotulo("Codigo")
                .tipo(FieldType.STRING)
                .tamanhoMaximo(5)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("codigo", "ABCDEFGHIJ");

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isFalse();
        assertThat(result.getErros().get(0).getMensagem()).contains("tamanho máximo");
    }

    @Test
    void validar_stringComRegexValido_retornaSucesso() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("cpf")
                .tipo(FieldType.STRING)
                .regex("\\d{11}")
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("cpf", "12345678901");

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isTrue();
    }

    @Test
    void validar_stringComRegexInvalido_retornaErro() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("cpf")
                .rotulo("CPF")
                .tipo(FieldType.STRING)
                .regex("\\d{11}")
                .mensagemErro("CPF deve ter 11 digitos")
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("cpf", "123-456");

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isFalse();
        assertThat(result.getErros().get(0).getMensagem()).isEqualTo("CPF deve ter 11 digitos");
    }

    @Test
    void validar_integerValido_retornaSucesso() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("quantidade")
                .tipo(FieldType.INTEGER)
                .valorMinimo(1.0)
                .valorMaximo(100.0)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("quantidade", 50);

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isTrue();
    }

    @Test
    void validar_integerAbaixoDoMinimo_retornaErro() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("quantidade")
                .rotulo("Quantidade")
                .tipo(FieldType.INTEGER)
                .valorMinimo(1.0)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("quantidade", 0);

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isFalse();
        assertThat(result.getErros().get(0).getMensagem()).contains("maior ou igual");
    }

    @Test
    void validar_integerAcimaDoMaximo_retornaErro() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("quantidade")
                .rotulo("Quantidade")
                .tipo(FieldType.INTEGER)
                .valorMaximo(10.0)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("quantidade", 99);

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isFalse();
        assertThat(result.getErros().get(0).getMensagem()).contains("menor ou igual");
    }

    @Test
    void validar_integerNaoNumerico_retornaErro() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("quantidade")
                .rotulo("Quantidade")
                .tipo(FieldType.INTEGER)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("quantidade", "abc");

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isFalse();
        assertThat(result.getErros().get(0).getMensagem()).contains("número inteiro");
    }

    @Test
    void validar_decimalValido_retornaSucesso() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("preco")
                .tipo(FieldType.DECIMAL)
                .valorMinimo(0.0)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("preco", 99.99);

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isTrue();
    }

    @Test
    void validar_decimalInvalido_retornaErro() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("preco")
                .rotulo("Preco")
                .tipo(FieldType.DECIMAL)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("preco", "nao-numerico");

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isFalse();
        assertThat(result.getErros().get(0).getMensagem()).contains("número decimal");
    }

    @Test
    void validar_dateComoLocalDate_retornaSucesso() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("nascimento")
                .tipo(FieldType.DATE)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = new HashMap<>();
        dados.put("nascimento", LocalDate.of(1990, 1, 15));

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isTrue();
    }

    @Test
    void validar_dateComoStringValida_retornaSucesso() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("nascimento")
                .tipo(FieldType.DATE)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("nascimento", "1990-01-15");

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isTrue();
    }

    @Test
    void validar_dateComoStringInvalida_retornaErro() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("nascimento")
                .rotulo("Nascimento")
                .tipo(FieldType.DATE)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("nascimento", "15/01/1990");

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isFalse();
        assertThat(result.getErros().get(0).getMensagem()).contains("AAAA-MM-DD");
    }

    @Test
    void validar_datetimeComoLocalDateTime_retornaSucesso() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("criacao")
                .tipo(FieldType.DATETIME)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = new HashMap<>();
        dados.put("criacao", LocalDateTime.of(2024, 1, 15, 10, 30));

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isTrue();
    }

    @Test
    void validar_datetimeComoStringInvalida_retornaErro() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("criacao")
                .rotulo("Criacao")
                .tipo(FieldType.DATETIME)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("criacao", "nao-e-datetime");

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isFalse();
    }

    @Test
    void validar_booleanComoBoolean_retornaSucesso() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("ativo")
                .tipo(FieldType.BOOLEAN)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("ativo", true);

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isTrue();
    }

    @Test
    void validar_booleanComoStringInvalida_retornaErro() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("ativo")
                .rotulo("Ativo")
                .tipo(FieldType.BOOLEAN)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("ativo", "sim");

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isFalse();
        assertThat(result.getErros().get(0).getMensagem()).contains("verdadeiro ou falso");
    }

    @Test
    void validar_enumValorValido_retornaSucesso() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("status")
                .tipo(FieldType.ENUM)
                .opcoes(List.of("ATIVO", "INATIVO", "PENDENTE"))
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("status", "ATIVO");

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isTrue();
    }

    @Test
    void validar_enumValorInvalido_retornaErro() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("status")
                .rotulo("Status")
                .tipo(FieldType.ENUM)
                .opcoes(List.of("ATIVO", "INATIVO"))
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("status", "DESCONHECIDO");

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isFalse();
        assertThat(result.getErros().get(0).getMensagem()).contains("ATIVO, INATIVO");
    }

    @Test
    void validar_multiploCamposComErros_retornaTodosOsErros() {
        FieldDefinition campo1 = FieldDefinition.builder()
                .nome("nome")
                .rotulo("Nome")
                .tipo(FieldType.STRING)
                .obrigatorio(true)
                .build();
        FieldDefinition campo2 = FieldDefinition.builder()
                .nome("idade")
                .rotulo("Idade")
                .tipo(FieldType.INTEGER)
                .valorMinimo(0.0)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo1, campo2)).build();
        Map<String, Object> dados = Map.of("idade", -1);

        ValidationResult result = service.validar(dados, schema);

        assertThat(result.isValido()).isFalse();
        assertThat(result.getErros()).hasSize(2);
    }

    @Test
    void validarOuLancarExcecao_dadosValidos_naoLancaExcecao() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("nome")
                .tipo(FieldType.STRING)
                .obrigatorio(true)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();
        Map<String, Object> dados = Map.of("nome", "Joao");

        service.validarOuLancarExcecao(dados, schema);
    }

    @Test
    void validarOuLancarExcecao_dadosInvalidos_lancaSchemaValidationException() {
        FieldDefinition campo = FieldDefinition.builder()
                .nome("nome")
                .rotulo("Nome")
                .tipo(FieldType.STRING)
                .obrigatorio(true)
                .build();
        FieldSchema schema = FieldSchema.builder().fields(List.of(campo)).build();

        assertThatThrownBy(() -> service.validarOuLancarExcecao(Map.of(), schema))
                .isInstanceOf(SchemaValidationException.class);
    }
}
