package com.doban.cadastro_pessoas_docs;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.doban.cadastro_pessoas_docs.recurso.tipo.TipoRecurso;
import com.doban.cadastro_pessoas_docs.recurso.tipo.TipoRecursoRepository;
import com.doban.cadastro_pessoas_docs.shared.schema.FieldDefinition;
import com.doban.cadastro_pessoas_docs.shared.schema.FieldSchema;
import com.doban.cadastro_pessoas_docs.shared.schema.FieldType;

import java.util.List;

@Configuration
public class DataInitializer {

	@Value("${DOBAN_EXCEL_FOLDER}")
	private String excelFolder;

	@Bean
	CommandLineRunner initDatabase(ExcelImportService excelImportService, TipoRecursoRepository tipoRecursoRepository) {
	    return args -> {
	        try {
	            // Criar tipos de recurso se não existirem
	            criarTiposRecursoSeNecessario(tipoRecursoRepository);

	            if (!excelImportService.hasPessoas()) {
	                String caminhoArquivo = excelFolder;
	                excelImportService.importar(caminhoArquivo);
	                System.out.println("Importação concluída com sucesso!");
	            } else {
	                System.out.println("Banco já contém pessoas, importação ignorada.");
	            }
	        } catch (Exception e) {
	            System.err.println("Erro na importação do Excel: " + e.getMessage());
	            e.printStackTrace();
	            // Não relançar: evita que a aplicação pare no startup
	        }
	    };
	}

	private void criarTiposRecursoSeNecessario(TipoRecursoRepository tipoRecursoRepository) {
	    // CARRO
	    if (!tipoRecursoRepository.existsByCodigo("CARRO")) {
	        TipoRecurso carro = TipoRecurso.builder()
	            .codigo("CARRO")
	            .nome("Carro")
	            .descricao("Veículo automotor")
	            .ativo(true)
	            .legado(true)
	            .schema(FieldSchema.builder()
	                .fields(List.of(
	                    FieldDefinition.builder().nome("marca").rotulo("Marca").tipo(FieldType.STRING).obrigatorio(true).build(),
	                    FieldDefinition.builder().nome("modelo").rotulo("Modelo").tipo(FieldType.STRING).obrigatorio(true).build(),
	                    FieldDefinition.builder().nome("cor").rotulo("Cor").tipo(FieldType.STRING).obrigatorio(false).build(),
	                    FieldDefinition.builder().nome("chassi").rotulo("Chassi").tipo(FieldType.STRING).obrigatorio(true).build(),
	                    FieldDefinition.builder().nome("placa").rotulo("Placa").tipo(FieldType.STRING).obrigatorio(true).build(),
	                    FieldDefinition.builder().nome("anoModelo").rotulo("Ano/Modelo").tipo(FieldType.STRING).obrigatorio(false).build()
	                ))
	                .build())
	            .build();
	        tipoRecursoRepository.save(carro);
	        System.out.println("✅ TipoRecurso CARRO criado");
	    }

	    // CELULAR
	    if (!tipoRecursoRepository.existsByCodigo("CELULAR")) {
	        TipoRecurso celular = TipoRecurso.builder()
	            .codigo("CELULAR")
	            .nome("Celular")
	            .descricao("Aparelho celular")
	            .ativo(true)
	            .legado(true)
	            .schema(FieldSchema.builder()
	                .fields(List.of(
	                    FieldDefinition.builder().nome("marca").rotulo("Marca").tipo(FieldType.STRING).obrigatorio(true).build(),
	                    FieldDefinition.builder().nome("modelo").rotulo("Modelo").tipo(FieldType.STRING).obrigatorio(true).build(),
	                    FieldDefinition.builder().nome("chip").rotulo("Chip").tipo(FieldType.STRING).obrigatorio(false).build(),
	                    FieldDefinition.builder().nome("imei").rotulo("IMEI").tipo(FieldType.STRING).obrigatorio(true).build()
	                ))
	                .build())
	            .build();
	        tipoRecursoRepository.save(celular);
	        System.out.println("✅ TipoRecurso CELULAR criado");
	    }

	    // ROCADEIRA
	    if (!tipoRecursoRepository.existsByCodigo("ROCADEIRA")) {
	        TipoRecurso rocadeira = TipoRecurso.builder()
	            .codigo("ROCADEIRA")
	            .nome("Roçadeira")
	            .descricao("Equipamento de jardinagem")
	            .ativo(true)
	            .legado(true)
	            .schema(FieldSchema.builder()
	                .fields(List.of(
	                    FieldDefinition.builder().nome("marca").rotulo("Marca").tipo(FieldType.STRING).obrigatorio(true).build(),
	                    FieldDefinition.builder().nome("numeroSerie").rotulo("Número de Série").tipo(FieldType.STRING).obrigatorio(true).build()
	                ))
	                .build())
	            .build();
	        tipoRecursoRepository.save(rocadeira);
	        System.out.println("✅ TipoRecurso ROCADEIRA criado");
	    }
	}

}
