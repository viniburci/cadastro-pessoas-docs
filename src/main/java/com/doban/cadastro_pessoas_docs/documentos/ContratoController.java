package com.doban.cadastro_pessoas_docs.documentos;

import com.doban.cadastro_pessoas_docs.pessoa.PessoaDTO;
import com.doban.cadastro_pessoas_docs.pessoa.PessoaService;
import com.doban.cadastro_pessoas_docs.vaga.VagaDTO;
import com.doban.cadastro_pessoas_docs.vaga.VagaService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/contrato")
public class ContratoController {

    private final VagaService vagaService;
    private final PessoaService pessoaService;
    private final PdfGeneratorService pdfGeneratorService;

    public ContratoController(PdfGeneratorService pdfGeneratorService, VagaService vagaService, PessoaService pessoaService) {
        this.pdfGeneratorService = pdfGeneratorService;
        this.vagaService = vagaService;
        this.pessoaService = pessoaService;
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

    @GetMapping("/vt/{vagaId}")
    public ResponseEntity<byte[]> downloadValeTransportePdf(@PathVariable Long vagaId) {

        VagaDTO vagaDTO = vagaService.obterVagaPorId(vagaId);
        PessoaDTO pessoaDTO = pessoaService.buscarPessoaPorId(vagaDTO.getPessoaId());

        Map<String, Object> data = new HashMap<>();
        
        Map<String, String> cliente = Map.of(
            "nome", vagaDTO.getCliente()
        );
        
        Map<String, Object> funcionario = Map.of(
            "nome", pessoaDTO.getNome(),
            "cpf", pessoaDTO.getCpf(),
            "endereço", pessoaDTO.getEndereco(),
            "bairro", pessoaDTO.getBairro(),
            "cidade", pessoaDTO.getCidade(),
            "uf", pessoaDTO.getEstado(),
            "cep", pessoaDTO.getCep(),
            "telefone", pessoaDTO.getTelefone()
        );

        data.put("cliente", cliente);
        data.put("funcionario", funcionario);
        
        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml1("vt", data);
        
        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = "contrato_" + Math.random() + ".pdf";
        
        headers.setContentLength(pdfBytes.length);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
