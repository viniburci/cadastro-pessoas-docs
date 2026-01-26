package com.doban.cadastro_pessoas_docs.documentos;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import static java.util.Map.entry;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.doban.cadastro_pessoas_docs.domain.pessoa.PessoaDTO;
import com.doban.cadastro_pessoas_docs.domain.pessoa.PessoaService;
import com.doban.cadastro_pessoas_docs.domain.vaga.VagaDTO;
import com.doban.cadastro_pessoas_docs.domain.vaga.VagaService;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.util.ULocale;

@RestController
@RequestMapping("/api/v1/documentos")
public class ContratoController {

    private final VagaService vagaService;
    private final PessoaService pessoaService;
    private final PdfGeneratorService pdfGeneratorService;

    public ContratoController(PdfGeneratorService pdfGeneratorService, VagaService vagaService,
            PessoaService pessoaService) {
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
                "uf", pessoaDTO.getEstado());

        Map<String, Object> contrato = Map.of(
                "funcao", vagaDTO.getTipoVagaNome() != null ? vagaDTO.getTipoVagaNome() : "N/A",
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
                "nome", vagaDTO.getClienteNome());

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
                "funcao", vagaDTO.getTipoVagaNome() != null ? vagaDTO.getTipoVagaNome() : "N/A",
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
                "cliente", vagaDTO.getClienteNome(),
                "funcao", vagaDTO.getTipoVagaNome() != null ? vagaDTO.getTipoVagaNome() : "N/A",
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
                "cliente", vagaDTO.getClienteNome(),
                "funcao", vagaDTO.getTipoVagaNome() != null ? vagaDTO.getTipoVagaNome() : "N/A",
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

    @GetMapping("/recibo_pagamento/{vagaId}")
    public ResponseEntity<byte[]> downloadReciboPagamentoPdf(@PathVariable Long vagaId) {

        VagaDTO vagaDTO = vagaService.obterVagaPorId(vagaId);
        PessoaDTO pessoaDTO = pessoaService.buscarPessoaPorId(vagaDTO.getPessoaId());

        // Calcular valores
        BigDecimal salarioBruto = vagaDTO.getSalario() != null ? vagaDTO.getSalario() : BigDecimal.ZERO;
        BigDecimal valeTransporte = BigDecimal.ZERO;

        // Se optante por VT, descontar 6% do salário
        if (vagaDTO.getOptanteVT() != null && vagaDTO.getOptanteVT()) {
            valeTransporte = salarioBruto.multiply(new BigDecimal("0.06"))
                    .setScale(2, java.math.RoundingMode.HALF_UP);
        }

        BigDecimal liquido = salarioBruto.subtract(valeTransporte);

        Map<String, Object> data = new HashMap<>();

        Map<String, String> empregado = Map.of(
                "nome", pessoaDTO.getNome(),
                "cpf", pessoaDTO.getCpf(),
                "cargo", vagaDTO.getTipoVagaNome() != null ? vagaDTO.getTipoVagaNome() : "N/A"
        );

        // Determinar mês de referência (mês atual ou mês da vaga)
        String mesReferencia = LocalDate.now().getMonth().toString() + "/" + LocalDate.now().getYear();

        Map<String, Object> contrato = new HashMap<>();
        contrato.put("cliente", vagaDTO.getClienteNome() != null ? vagaDTO.getClienteNome() : "N/A");
        contrato.put("salarioBruto", salarioBruto);
        contrato.put("valeTransporte", valeTransporte);
        contrato.put("salarioLiquido", liquido);
        contrato.put("mesReferencia", mesReferencia);

        data.put("empregado", empregado);
        data.put("contrato", contrato);
        data.put("dataAtualExtenso", obterDataPorExtenso());

        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml1("recibo_pagamento", data);

        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = "recibo_pagamento_" + pessoaDTO.getNome().replaceAll(" ", "_") + ".pdf";

        headers.setContentLength(pdfBytes.length);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/termo_devolucao/{pessoaId}")
    public ResponseEntity<byte[]> downloadTermoDevolucaoPdf(@PathVariable Long pessoaId) {

        PessoaDTO pessoaDTO = pessoaService.buscarPessoaPorId(pessoaId);

        // Criar lista vazia de recursos devolvidos
        // Nota: Quando RecursoDinamicoService tiver método para buscar recursos devolvidos,
        // substituir por: recursoDinamicoService.listarPorPessoaComDevolucao(pessoaId)
        List<Map<String, Object>> recursosDevolvidos = new ArrayList<>();

        // Exemplo de estrutura esperada no template:
        // Map com: tipoRecursoNome, identificador, dataEntrega, dataDevolucao, diasUso

        Map<String, Object> data = new HashMap<>();

        Map<String, String> pessoa = Map.of(
                "nome", pessoaDTO.getNome(),
                "cpf", pessoaDTO.getCpf(),
                "rg", pessoaDTO.getNumeroRg() != null ? pessoaDTO.getNumeroRg() : "N/A"
        );

        data.put("pessoa", pessoa);
        data.put("recursos", recursosDevolvidos);
        data.put("dataAtualExtenso", obterDataPorExtenso());

        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml1("termo_devolucao", data);

        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = "termo_devolucao_" + pessoaDTO.getNome().replaceAll(" ", "_") + ".pdf";

        headers.setContentLength(pdfBytes.length);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
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
                "funcao", vagaDTO.getTipoVagaNome() != null ? vagaDTO.getTipoVagaNome() : "N/A",
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

    @GetMapping("/documentos_combinados/{vagaId}")
    public ResponseEntity<byte[]> downloadDocumentosCombinados(
            @PathVariable Long vagaId,
            @RequestParam List<String> tipos) {

        VagaDTO vagaDTO = vagaService.obterVagaPorId(vagaId);
        PessoaDTO pessoaDTO = pessoaService.buscarPessoaPorId(vagaDTO.getPessoaId());

        List<String> templates = new ArrayList<>();
        List<Map<String, Object>> dataList = new ArrayList<>();

        for (String tipo : tipos) {
            Map<String, Object> data = prepararDadosParaTipo(tipo, vagaDTO, pessoaDTO);
            if (data != null) {
                templates.add(tipo);
                dataList.add(data);
            }
        }

        if (templates.isEmpty()) {
            throw new IllegalArgumentException("Nenhum tipo de documento válido informado");
        }

        byte[] pdfBytes = pdfGeneratorService.generateMultiplePdfs(templates, dataList);

        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = "documentos_" + pessoaDTO.getNome().replaceAll(" ", "_") + ".pdf";

        headers.setContentLength(pdfBytes.length);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    private Map<String, Object> prepararDadosParaTipo(String tipo, VagaDTO vagaDTO, PessoaDTO pessoaDTO) {
        Map<String, Object> data = new HashMap<>();

        switch (tipo) {
            case "contrato":
                data.put("empregado", criarMapEmpregadoBasico(pessoaDTO));
                data.put("contrato", criarMapContratoBasico(vagaDTO));
                data.put("dataAtualExtenso", obterDataPorExtenso());
                break;

            case "vt":
                data.put("cliente", Map.of("nome", vagaDTO.getClienteNome()));
                data.put("funcionario", Map.of(
                        "nome", pessoaDTO.getNome(),
                        "cpf", pessoaDTO.getCpf(),
                        "endereço", pessoaDTO.getEndereco(),
                        "bairro", pessoaDTO.getBairro(),
                        "cidade", pessoaDTO.getCidade(),
                        "uf", pessoaDTO.getEstado(),
                        "cep", pessoaDTO.getCep(),
                        "telefone", pessoaDTO.getTelefone()));
                data.put("dataAtual", obterDataPorExtenso());
                break;

            case "contrato_ps":
                Map<String, String> empregadoPs = new HashMap<>(criarMapEmpregadoBasico(pessoaDTO));
                empregadoPs.put("pix", pessoaDTO.getChavePix());
                data.put("empregado", empregadoPs);

                Map<String, Object> contratoPs = new HashMap<>(criarMapContratoBasico(vagaDTO));
                contratoPs.put("salarioExtenso", converterParaValorExtenso(vagaDTO.getSalario()));
                data.put("contrato", contratoPs);
                data.put("dataAtualExtenso", obterDataPorExtenso());
                break;

            case "termo_materiais":
            case "entrega_epi":
                data.put("empregado", criarMapEmpregadoCompleto(pessoaDTO));
                data.put("contrato", criarMapContratoCompleto(vagaDTO));
                data.put("dataAtualExtenso", obterDataPorExtenso());
                break;

            case "recibo_pagamento":
                BigDecimal salarioBruto = vagaDTO.getSalario() != null ? vagaDTO.getSalario() : BigDecimal.ZERO;
                BigDecimal valeTransporte = BigDecimal.ZERO;
                if (vagaDTO.getOptanteVT() != null && vagaDTO.getOptanteVT()) {
                    valeTransporte = salarioBruto.multiply(new BigDecimal("0.06"))
                            .setScale(2, java.math.RoundingMode.HALF_UP);
                }
                BigDecimal liquido = salarioBruto.subtract(valeTransporte);

                data.put("empregado", Map.of(
                        "nome", pessoaDTO.getNome(),
                        "cpf", pessoaDTO.getCpf(),
                        "cargo", vagaDTO.getTipoVagaNome() != null ? vagaDTO.getTipoVagaNome() : "N/A"
                ));

                Map<String, Object> contratoRecibo = new HashMap<>();
                contratoRecibo.put("cliente", vagaDTO.getClienteNome() != null ? vagaDTO.getClienteNome() : "N/A");
                contratoRecibo.put("salarioBruto", salarioBruto);
                contratoRecibo.put("valeTransporte", valeTransporte);
                contratoRecibo.put("salarioLiquido", liquido);
                contratoRecibo.put("mesReferencia", LocalDate.now().getMonth().toString() + "/" + LocalDate.now().getYear());
                data.put("contrato", contratoRecibo);
                data.put("dataAtualExtenso", obterDataPorExtenso());
                break;

            default:
                return null;
        }

        return data;
    }

    private Map<String, String> criarMapEmpregadoBasico(PessoaDTO pessoaDTO) {
        return Map.of(
                "nome", pessoaDTO.getNome(),
                "estadoCivil", pessoaDTO.getEstadoCivil(),
                "rg", pessoaDTO.getNumeroRg(),
                "cpf", pessoaDTO.getCpf(),
                "ctps", pessoaDTO.getNumeroCtps(),
                "ctpsSerie", pessoaDTO.getSerieCtps(),
                "endereco", pessoaDTO.getEndereco(),
                "cidade", pessoaDTO.getCidade(),
                "uf", pessoaDTO.getEstado());
    }

    private Map<String, String> criarMapEmpregadoCompleto(PessoaDTO pessoaDTO) {
        return Map.ofEntries(
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
                entry("telefone", pessoaDTO.getTelefone()));
    }

    private Map<String, Object> criarMapContratoBasico(VagaDTO vagaDTO) {
        return Map.of(
                "funcao", vagaDTO.getTipoVagaNome() != null ? vagaDTO.getTipoVagaNome() : "N/A",
                "salario", vagaDTO.getSalario(),
                "cidadeTrabalho", vagaDTO.getCidade(),
                "dataInicio", vagaDTO.getDataAdmissao(),
                "dataFim", vagaDTO.getDataDemissao(),
                "horarioEntrada", vagaDTO.getHorarioEntrada(),
                "horarioSaida", vagaDTO.getHorarioSaida());
    }

    private Map<String, Object> criarMapContratoCompleto(VagaDTO vagaDTO) {
        return Map.of(
                "cliente", vagaDTO.getClienteNome(),
                "funcao", vagaDTO.getTipoVagaNome() != null ? vagaDTO.getTipoVagaNome() : "N/A",
                "salario", vagaDTO.getSalario(),
                "salarioExtenso", converterParaValorExtenso(vagaDTO.getSalario()),
                "cidadeTrabalho", vagaDTO.getCidade(),
                "estadoTrabalho", vagaDTO.getUf(),
                "dataInicio", vagaDTO.getDataAdmissao(),
                "dataFim", vagaDTO.getDataDemissao(),
                "horarioEntrada", vagaDTO.getHorarioEntrada(),
                "horarioSaida", vagaDTO.getHorarioSaida());
    }
}
