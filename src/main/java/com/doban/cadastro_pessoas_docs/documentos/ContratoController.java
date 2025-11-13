package com.doban.cadastro_pessoas_docs.documentos;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/contrato")
public class ContratoController {

    private final PdfGeneratorService pdfGeneratorService;

    public ContratoController(PdfGeneratorService pdfGeneratorService) {
        this.pdfGeneratorService = pdfGeneratorService;
    }

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadContratoPdf() {
        
        // Simulação dos dados que viriam do seu banco de dados
        Map<String, Object> data = new HashMap<>();
        
        // Usamos Mapas para simular objetos complexos
        Map<String, String> cliente = Map.of(
            "nome", "Carlos Eduardo Silva",
            "cpf", "123.456.789-00",
            "endereco", "Rua das Laranjeiras, 50 - Centro"
        );
        
        Map<String, String> contrato = Map.of(
            "numero", "045/2025"
        );
        
        Map<String, Object> servico = Map.of(
            "descricao", "Criação e Manutenção de Aplicação Web.",
            "prazoDias", 120
        );

        data.put("cliente", cliente);
        data.put("contrato", contrato);
        data.put("servico", servico);
        
        // 1. Gera o PDF usando o Service
        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml("contrato", data);
        
        // 2. Configura a resposta HTTP
        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = "contrato_" + contrato.get("numero").replace("/", "_") + ".pdf";
        
        headers.setContentLength(pdfBytes.length);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo);

        // 3. Retorna o PDF
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/download2")
    public ResponseEntity<byte[]> downloadValeTransportePdf() {
        
        // 1. Gera o PDF usando o Service
        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml1("vt", null);
        
        // 2. Configura a resposta HTTP
        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = "contrato_" + Math.random() + ".pdf";
        
        headers.setContentLength(pdfBytes.length);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo);

        // 3. Retorna o PDF
        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
