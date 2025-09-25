package com.doban.cadastro_pessoas_docs;


import com.doban.cadastro_pessoas_docs.ExcelImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

	@Bean
	CommandLineRunner initDatabase(ExcelImportService excelImportService) {
	    return args -> {
	        try {
	            if (!excelImportService.hasPessoas()) {
	                String caminhoArquivo = "E:/DOBAN.xlsm";
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
