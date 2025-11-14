package com.doban.cadastro_pessoas_docs.documentos;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class PdfGeneratorService {

    private final TemplateEngine templateEngine;

    // Injeção do TemplateEngine do Thymeleaf
    public PdfGeneratorService(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    /**
     * Gera um PDF a partir de um template Thymeleaf. param - templateName: O
     * nome do arquivo HTML (ex: "contrato") param - data: Model (Map) com os
     * dados a serem preenchidos no template return - O array de bytes do
     * arquivo PDF
     */
    public byte[] generatePdfFromHtml(String templateName, Map<String, Object> data) {

        // 1. Processa o Template com os Dados (Thymeleaf)
        Context context = new Context();
        context.setVariables(data);
        String htmlContent = templateEngine.process(templateName, context);

        // 2. Converte o HTML processado para PDF (Flying Saucer)
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            ITextRenderer renderer = new ITextRenderer();

            // É essencial para que ele encontre recursos (imagens, fontes) se você tiver
            String baseUrl = "file://" + System.getProperty("user.dir") + "/src/main/resources/static/";
            renderer.setDocumentFromString(htmlContent, baseUrl);

            renderer.layout();
            renderer.createPDF(bos);

            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF a partir do HTML.", e);
        }
    }

    public byte[] generatePdfFromHtml1(String templateName, Map<String, Object> data) {
        

        // 1. Processa o Template com os Dados (Thymeleaf)
        Context context = new Context();
        if (data != null) {
            context.setVariables(data);
        }
        String htmlContent = templateEngine.process(templateName, context);

        // 2. Converte o HTML processado para PDF (Flying Saucer)
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {

            ITextRenderer renderer = new ITextRenderer();

            String baseUrl = new ClassPathResource("static/").getURL().toString();
            renderer.setDocumentFromString(htmlContent, baseUrl);

            renderer.layout();
            renderer.createPDF(bos);

            return bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar PDF a partir do HTML.", e);
        }
    }
}
