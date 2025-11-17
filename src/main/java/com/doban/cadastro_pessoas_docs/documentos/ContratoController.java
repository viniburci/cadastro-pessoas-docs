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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping("/documentos")
public class ContratoController {

    private final VagaService vagaService;
    private final PessoaService pessoaService;
    private final PdfGeneratorService pdfGeneratorService;

    public ContratoController(PdfGeneratorService pdfGeneratorService, VagaService vagaService, PessoaService pessoaService) {
        this.pdfGeneratorService = pdfGeneratorService;
        this.vagaService = vagaService;
        this.pessoaService = pessoaService;
    }

    @GetMapping("/contrato/{vagaId}")
    public ResponseEntity<byte[]> downloadContratoPdf(@PathVariable Long vagaId) {

        VagaDTO vagaDTO = vagaService.obterVagaPorId(vagaId);
        PessoaDTO pessoaDTO = pessoaService.buscarPessoaPorId(vagaDTO.getPessoaId());
        
        Map<String, Object> data = new HashMap<>();
        
        Map<String, String> empregado = Map.of(
            "nome", pessoaDTO.getNome(),
            "estadoCivil", pessoaDTO.getEstadoCivil(),
            "rg", pessoaDTO.getNumeroRg(),
            "cpf", pessoaDTO.getCpf(),
            "ctps", pessoaDTO.getNumeroCtps(),
            "ctpsSerie", pessoaDTO.getSerieCtps(),
            "endereco", pessoaDTO.getEndereco(),
            "cidade", pessoaDTO.getCidade(),
            "uf", pessoaDTO.getEstado()
        );
        
        Map<String, Object> contrato = Map.of(
            "funcao", vagaDTO.getCargo(),
            "salario", vagaDTO.getSalario(),
            "cidadeTrabalho", vagaDTO.getCidade(),
            "dataInicio", vagaDTO.getDataAdmissao(),
            "dataFim", vagaDTO.getDataDemissao(),
            "horarioEntrada", vagaDTO.getHorarioEntrada(),
            "horarioSaida", vagaDTO.getHorarioSaida()
        );

        data.put("empregado", empregado);
        data.put("contrato", contrato);
        data.put("dataAtualExtenso", obterDataPorExtenso());
        
        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml1("contrato", data);
        
        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = "contrato_" + Math.random() + ".pdf";
        
        headers.setContentLength(pdfBytes.length);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo);

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
        data.put("dataAtual", obterDataPorExtenso());
        
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

    public static String obterDataPorExtenso() {
        LocalDate hoje = LocalDate.now();

        DateTimeFormatter formatador = DateTimeFormatter.ofPattern(
            "d 'de' MMMM 'de' yyyy", 
            new Locale.Builder().setLanguage("pt").setRegion("BR").build()
        );

        return hoje.format(formatador);
    }
}
