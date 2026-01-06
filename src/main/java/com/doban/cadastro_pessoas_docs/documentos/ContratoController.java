package com.doban.cadastro_pessoas_docs.documentos;

import com.doban.cadastro_pessoas_docs.domain.pessoa.PessoaDTO;
import com.doban.cadastro_pessoas_docs.domain.pessoa.PessoaService;
import com.doban.cadastro_pessoas_docs.domain.vaga.VagaDTO;
import com.doban.cadastro_pessoas_docs.domain.vaga.VagaService;
import com.doban.cadastro_pessoas_docs.recurso.recurso_carro.RecursoCarro;
import com.doban.cadastro_pessoas_docs.recurso.recurso_carro.RecursoCarroService;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.util.ULocale;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import static java.util.Map.entry;

@RestController
@RequestMapping("/api/v1/documentos")
public class ContratoController {

    private final VagaService vagaService;
    private final PessoaService pessoaService;
    private final RecursoCarroService recursoCarroService;
    private final PdfGeneratorService pdfGeneratorService;

    public ContratoController(PdfGeneratorService pdfGeneratorService, VagaService vagaService,
            PessoaService pessoaService, RecursoCarroService recursoCarroService) {
        this.pdfGeneratorService = pdfGeneratorService;
        this.vagaService = vagaService;
        this.pessoaService = pessoaService;
        this.recursoCarroService = recursoCarroService;
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
                "uf", pessoaDTO.getEstado());

        Map<String, Object> contrato = Map.of(
                "funcao", vagaDTO.getCargo(),
                "salario", vagaDTO.getSalario(),
                "cidadeTrabalho", vagaDTO.getCidade(),
                "dataInicio", vagaDTO.getDataAdmissao(),
                "dataFim", vagaDTO.getDataDemissao(),
                "horarioEntrada", vagaDTO.getHorarioEntrada(),
                "horarioSaida", vagaDTO.getHorarioSaida());

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
                "nome", vagaDTO.getCliente());

        Map<String, Object> funcionario = Map.of(
                "nome", pessoaDTO.getNome(),
                "cpf", pessoaDTO.getCpf(),
                "endereço", pessoaDTO.getEndereco(),
                "bairro", pessoaDTO.getBairro(),
                "cidade", pessoaDTO.getCidade(),
                "uf", pessoaDTO.getEstado(),
                "cep", pessoaDTO.getCep(),
                "telefone", pessoaDTO.getTelefone());

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

    @GetMapping("/contrato_ps/{vagaId}")
    public ResponseEntity<byte[]> downloadContratoPsPdf(@PathVariable Long vagaId) {

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
                "uf", pessoaDTO.getEstado(),
                "pix", pessoaDTO.getChavePix());

        Map<String, Object> contrato = Map.of(
                "funcao", vagaDTO.getCargo(),
                "salario", vagaDTO.getSalario(),
                "salarioExtenso", converterParaValorExtenso(vagaDTO.getSalario()),
                "cidadeTrabalho", vagaDTO.getCidade(),
                "dataInicio", vagaDTO.getDataAdmissao(),
                "dataFim", vagaDTO.getDataDemissao(),
                "horarioEntrada", vagaDTO.getHorarioEntrada(),
                "horarioSaida", vagaDTO.getHorarioSaida());

        data.put("empregado", empregado);
        data.put("contrato", contrato);
        data.put("dataAtualExtenso", obterDataPorExtenso());

        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml1("contrato_ps", data);

        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = "contrato_" + Math.random() + ".pdf";

        headers.setContentLength(pdfBytes.length);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/termo_materiais/{vagaId}")
    public ResponseEntity<byte[]> downloadTermoMateriaisPdf(@PathVariable Long vagaId) {

        VagaDTO vagaDTO = vagaService.obterVagaPorId(vagaId);
        PessoaDTO pessoaDTO = pessoaService.buscarPessoaPorId(vagaDTO.getPessoaId());

        Map<String, Object> data = new HashMap<>();

        Map<String, String> empregado = Map.ofEntries(
                entry("nome", pessoaDTO.getNome()),
                entry("estadoCivil", pessoaDTO.getEstadoCivil()),
                entry("rg", pessoaDTO.getNumeroRg()),
                entry("cpf", pessoaDTO.getCpf()),
                entry("ctps", pessoaDTO.getNumeroCtps()),
                entry("ctpsSerie", pessoaDTO.getSerieCtps()),
                entry("endereco", pessoaDTO.getEndereco()),
                entry("bairro", pessoaDTO.getBairro()),
                entry("cidade", pessoaDTO.getCidade()),
                entry("uf", pessoaDTO.getEstado()),
                entry("pix", pessoaDTO.getChavePix()),
                entry("cep", pessoaDTO.getCep()),
                entry("telefone", pessoaDTO.getTelefone())
        );

        Map<String, Object> contrato = Map.of(
                "cliente", vagaDTO.getCliente(),
                "funcao", vagaDTO.getCargo(),
                "salario", vagaDTO.getSalario(),
                "salarioExtenso", converterParaValorExtenso(vagaDTO.getSalario()),
                "cidadeTrabalho", vagaDTO.getCidade(),
                "estadoTrabalho", vagaDTO.getUf(),
                "dataInicio", vagaDTO.getDataAdmissao(),
                "dataFim", vagaDTO.getDataDemissao(),
                "horarioEntrada", vagaDTO.getHorarioEntrada(),
                "horarioSaida", vagaDTO.getHorarioSaida());

        data.put("empregado", empregado);
        data.put("contrato", contrato);
        data.put("dataAtualExtenso", obterDataPorExtenso());

        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml1("termo_materiais", data);

        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = "contrato_" + Math.random() + ".pdf";

        headers.setContentLength(pdfBytes.length);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/entrega_epi/{vagaId}")
    public ResponseEntity<byte[]> downloadEntregaEpiPdf(@PathVariable Long vagaId) {

        VagaDTO vagaDTO = vagaService.obterVagaPorId(vagaId);
        PessoaDTO pessoaDTO = pessoaService.buscarPessoaPorId(vagaDTO.getPessoaId());

        Map<String, Object> data = new HashMap<>();

        Map<String, String> empregado = Map.ofEntries(
                entry("nome", pessoaDTO.getNome()),
                entry("estadoCivil", pessoaDTO.getEstadoCivil()),
                entry("rg", pessoaDTO.getNumeroRg()),
                entry("cpf", pessoaDTO.getCpf()),
                entry("ctps", pessoaDTO.getNumeroCtps()),
                entry("ctpsSerie", pessoaDTO.getSerieCtps()),
                entry("endereco", pessoaDTO.getEndereco()),
                entry("bairro", pessoaDTO.getBairro()),
                entry("cidade", pessoaDTO.getCidade()),
                entry("uf", pessoaDTO.getEstado()),
                entry("pix", pessoaDTO.getChavePix()),
                entry("cep", pessoaDTO.getCep()),
                entry("telefone", pessoaDTO.getTelefone())
        );

        Map<String, Object> contrato = Map.of(
                "cliente", vagaDTO.getCliente(),
                "funcao", vagaDTO.getCargo(),
                "salario", vagaDTO.getSalario(),
                "salarioExtenso", converterParaValorExtenso(vagaDTO.getSalario()),
                "cidadeTrabalho", vagaDTO.getCidade(),
                "estadoTrabalho", vagaDTO.getUf(),
                "dataInicio", vagaDTO.getDataAdmissao(),
                "dataFim", vagaDTO.getDataDemissao(),
                "horarioEntrada", vagaDTO.getHorarioEntrada(),
                "horarioSaida", vagaDTO.getHorarioSaida());

        data.put("empregado", empregado);
        data.put("contrato", contrato);
        data.put("dataAtualExtenso", obterDataPorExtenso());

        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml1("entrega_epi", data);

        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = "contrato_" + Math.random() + ".pdf";

        headers.setContentLength(pdfBytes.length);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/carro_checklist/{recursoCarroId}")
    public ResponseEntity<byte[]> downloadCarroChecklistPdf(@PathVariable Long recursoCarroId) {

        RecursoCarro recursoCarro = recursoCarroService.buscarPorId(recursoCarroId);
        PessoaDTO pessoaDTO = pessoaService.buscarPessoaPorId(recursoCarro.getPessoa().getId());

        Map<String, Object> data = new HashMap<>();

        Map<String, String> empregado = Map.ofEntries(
                entry("nome", pessoaDTO.getNome()),
                entry("estadoCivil", pessoaDTO.getEstadoCivil()),
                entry("rg", pessoaDTO.getNumeroRg()),
                entry("cpf", pessoaDTO.getCpf()),
                entry("ctps", pessoaDTO.getNumeroCtps()),
                entry("ctpsSerie", pessoaDTO.getSerieCtps()),
                entry("endereco", pessoaDTO.getEndereco()),
                entry("bairro", pessoaDTO.getBairro()),
                entry("cidade", pessoaDTO.getCidade()),
                entry("uf", pessoaDTO.getEstado()),
                entry("pix", pessoaDTO.getChavePix()),
                entry("cep", pessoaDTO.getCep()),
                entry("telefone", pessoaDTO.getTelefone())
        );

        Map<String, String> carro = Map.ofEntries(
                entry("modelo", recursoCarro.getCarro().getModelo()),
                entry("cor", recursoCarro.getCarro().getCor()),
                entry("ano", recursoCarro.getCarro().getAnoModelo()),
                entry("chassi", recursoCarro.getCarro().getChassi()),
                entry("placa", recursoCarro.getCarro().getPlaca()),
                entry("marca", recursoCarro.getCarro().getMarca())
        );

        data.put("empregado", empregado);
        data.put("carro", carro);
        data.put("dataAtualExtenso", obterDataPorExtenso());

        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml1("carro_checklist", data);

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
                new Locale.Builder().setLanguage("pt").setRegion("BR").build());

        return hoje.format(formatador);
    }

    public String converterParaValorExtenso(BigDecimal valor) {
        ULocale locale = new ULocale("pt_BR");

        RuleBasedNumberFormat rbnf = new RuleBasedNumberFormat(locale, RuleBasedNumberFormat.SPELLOUT);

        String extenso = rbnf.format(valor);

        return extenso;
    }

    @GetMapping("/cracha/{vagaId}")
    public ResponseEntity<byte[]> downloadCrachaPdf(@PathVariable Long vagaId) {

        VagaDTO vagaDTO = vagaService.obterVagaPorId(vagaId);
        PessoaDTO pessoaDTO = pessoaService.buscarPessoaPorId(vagaDTO.getPessoaId());

        Map<String, Object> data = new HashMap<>();

        Map<String, String> pessoa = Map.of(
                "nome", pessoaDTO.getNome(),
                "cpf", pessoaDTO.getCpf()
        );

        Map<String, String> contrato = Map.of(
                "funcao", vagaDTO.getCargo(),
                "numero", vagaDTO.getDataAdmissao() != null ? vagaDTO.getDataAdmissao().toString() : ""
        );

        Map<String, String> contato = Map.of(
                "email", "dobanmaringa@gmail.com"
        );

        data.put("pessoa", pessoa);
        data.put("contrato", contrato);
        data.put("contato", contato);

        byte[] pdfBytes = pdfGeneratorService.generatePdfWithPhoto("cracha", data, pessoaDTO.getFoto());

        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = "cracha_" + pessoaDTO.getNome().replaceAll(" ", "_") + ".pdf";

        headers.setContentLength(pdfBytes.length);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }
}
