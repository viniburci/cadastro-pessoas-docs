package com.doban.cadastro_pessoas_docs;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

	@Value("${DOBAN_EXCEL_FOLDER}")
	private String excelFolder;


	@Bean
	CommandLineRunner initDatabase(ExcelImportService excelImportService) {
	    return args -> {
	        try {
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

}
