package com.doban.cadastro_pessoas_docs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DobanCadastroDePessoasEDocumentosApplication {
	
	private final ExcelImportService excelImportService;
	
	public DobanCadastroDePessoasEDocumentosApplication(ExcelImportService excelImportService) {
		this.excelImportService = excelImportService;
	}

	public static void main(String[] args) {
		SpringApplication.run(DobanCadastroDePessoasEDocumentosApplication.class, args);
	}
	
//	@Override
//    public void run(String... args) throws Exception {
//        String path = "C:/dados/pessoas.xlsx"; // caminho fixo ou via application.properties
//        excelImportService.importarPessoas(path);
//        System.out.println("📂 Importação concluída ao iniciar aplicação");
//    }

}
