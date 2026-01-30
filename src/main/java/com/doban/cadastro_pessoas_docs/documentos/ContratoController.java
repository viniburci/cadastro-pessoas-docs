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
import com.doban.cadastro_pessoas_docs.domain.vaga.tipo.TipoVaga;
import com.doban.cadastro_pessoas_docs.domain.vaga.tipo.TipoVagaRepository;
import com.doban.cadastro_pessoas_docs.recurso.item.ItemDinamico;
import com.doban.cadastro_pessoas_docs.recurso.item.ItemDinamicoRepository;
import com.ibm.icu.text.RuleBasedNumberFormat;
import com.ibm.icu.util.ULocale;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/documentos")
public class ContratoController {

    private final VagaService vagaService;
    private final PessoaService pessoaService;
    private final PdfGeneratorService pdfGeneratorService;
    private final TipoVagaRepository tipoVagaRepository;
    private final ItemDinamicoRepository itemDinamicoRepository;

    public ContratoController(PdfGeneratorService pdfGeneratorService, VagaService vagaService,
            PessoaService pessoaService, TipoVagaRepository tipoVagaRepository,
            ItemDinamicoRepository itemDinamicoRepository) {
        this.pdfGeneratorService = pdfGeneratorService;
        this.vagaService = vagaService;
        this.pessoaService = pessoaService;
        this.tipoVagaRepository = tipoVagaRepository;
        this.itemDinamicoRepository = itemDinamicoRepository;
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

    /**
     * Gera o Termo de Responsabilidade de Materiais baseado em Pessoa e ItemDinamico.
     * @param pessoaId ID da pessoa responsável
     * @param itemIds Lista de IDs dos itens/equipamentos emprestados
     */
    @GetMapping("/termo_responsabilidade_materiais/{pessoaId}")
    public ResponseEntity<byte[]> downloadTermoResponsabilidadeMateriaisPdf(
            @PathVariable Long pessoaId,
            @RequestParam List<Long> itemIds) {

        PessoaDTO pessoaDTO = pessoaService.buscarPessoaPorId(pessoaId);

        Map<String, Object> data = new HashMap<>();

        Map<String, String> empregado = Map.ofEntries(
                entry("nome", pessoaDTO.getNome()),
                entry("estadoCivil", pessoaDTO.getEstadoCivil() != null ? pessoaDTO.getEstadoCivil() : ""),
                entry("rg", pessoaDTO.getNumeroRg() != null ? pessoaDTO.getNumeroRg() : ""),
                entry("cpf", pessoaDTO.getCpf() != null ? pessoaDTO.getCpf() : ""),
                entry("ctps", pessoaDTO.getNumeroCtps() != null ? pessoaDTO.getNumeroCtps() : ""),
                entry("ctpsSerie", pessoaDTO.getSerieCtps() != null ? pessoaDTO.getSerieCtps() : ""),
                entry("endereco", pessoaDTO.getEndereco() != null ? pessoaDTO.getEndereco() : ""),
                entry("bairro", pessoaDTO.getBairro() != null ? pessoaDTO.getBairro() : ""),
                entry("cidade", pessoaDTO.getCidade() != null ? pessoaDTO.getCidade() : ""),
                entry("uf", pessoaDTO.getEstado() != null ? pessoaDTO.getEstado() : ""),
                entry("pix", pessoaDTO.getChavePix() != null ? pessoaDTO.getChavePix() : ""),
                entry("cep", pessoaDTO.getCep() != null ? pessoaDTO.getCep() : ""),
                entry("telefone", pessoaDTO.getTelefone() != null ? pessoaDTO.getTelefone() : "")
        );

        // Buscar os ItemDinamicos pelos IDs
        List<ItemDinamico> itensDinamicos = itemDinamicoRepository.findAllById(itemIds);

        // Converter ItemDinamico para formato do template
        List<Map<String, Object>> itens = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemDinamico item : itensDinamicos) {
            Map<String, Object> itemMap = new HashMap<>();
            Map<String, Object> atributos = item.getAtributos();

            // Mapear atributos do ItemDinamico para colunas do template
            itemMap.put("quantidade", atributos.getOrDefault("quantidade", 1));
            itemMap.put("marca", atributos.getOrDefault("marca", item.getTipoRecurso().getNome()));
            itemMap.put("descricao", atributos.getOrDefault("descricao", item.getIdentificador()));
            itemMap.put("numeroSerie", atributos.getOrDefault("numeroSerie", atributos.getOrDefault("imei", "")));
            itemMap.put("ddd", atributos.getOrDefault("ddd", ""));

            Object valorObj = atributos.get("valor");
            if (valorObj != null) {
                BigDecimal valor;
                if (valorObj instanceof BigDecimal) {
                    valor = (BigDecimal) valorObj;
                } else if (valorObj instanceof Number) {
                    valor = BigDecimal.valueOf(((Number) valorObj).doubleValue());
                } else {
                    valor = new BigDecimal(valorObj.toString());
                }
                itemMap.put("valor", valor);
                valorTotal = valorTotal.add(valor);
            }

            itens.add(itemMap);
        }

        data.put("empregado", empregado);
        data.put("contrato", Map.of("cliente", "")); // Não há contrato/vaga associado
        data.put("dataAtualExtenso", obterDataPorExtenso());
        data.put("itens", itens);
        data.put("valorTotal", valorTotal);

        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml1("termo_responsabilidade_materiais", data);

        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = "termo_responsabilidade_materiais_" + pessoaDTO.getNome().replaceAll(" ", "_") + ".pdf";

        headers.setContentLength(pdfBytes.length);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    /**
     * Gera a Declaracao de Devolucao de Aparelho Corporativo baseado em Pessoa e ItemDinamico.
     * @param pessoaId ID da pessoa que esta devolvendo
     * @param itemIds Lista de IDs dos itens/equipamentos devolvidos
     */
    @GetMapping("/declaracao_devolucao_aparelho/{pessoaId}")
    public ResponseEntity<byte[]> downloadDeclaracaoDevolucaoAparelhoPdf(
            @PathVariable Long pessoaId,
            @RequestParam List<Long> itemIds) {

        PessoaDTO pessoaDTO = pessoaService.buscarPessoaPorId(pessoaId);

        Map<String, Object> data = new HashMap<>();

        Map<String, String> empregado = Map.ofEntries(
                entry("nome", pessoaDTO.getNome() != null ? pessoaDTO.getNome() : ""),
                entry("rg", pessoaDTO.getNumeroRg() != null ? pessoaDTO.getNumeroRg() : ""),
                entry("cpf", pessoaDTO.getCpf() != null ? pessoaDTO.getCpf() : "")
        );

        // Buscar os ItemDinamicos pelos IDs
        List<ItemDinamico> itensDinamicos = itemDinamicoRepository.findAllById(itemIds);

        // Converter ItemDinamico para formato do template
        List<Map<String, Object>> itens = new ArrayList<>();

        for (ItemDinamico item : itensDinamicos) {
            Map<String, Object> itemMap = new HashMap<>();
            Map<String, Object> atributos = item.getAtributos();

            itemMap.put("quantidade", atributos.getOrDefault("quantidade", 1));
            itemMap.put("marca", atributos.getOrDefault("marca", item.getTipoRecurso().getNome()));
            itemMap.put("descricao", atributos.getOrDefault("descricao", item.getIdentificador()));
            itemMap.put("modelo", atributos.getOrDefault("modelo", ""));
            itemMap.put("numeroSerie", atributos.getOrDefault("numeroSerie", atributos.getOrDefault("imei", "")));

            Object valorObj = atributos.get("valor");
            if (valorObj != null) {
                BigDecimal valor;
                if (valorObj instanceof BigDecimal) {
                    valor = (BigDecimal) valorObj;
                } else if (valorObj instanceof Number) {
                    valor = BigDecimal.valueOf(((Number) valorObj).doubleValue());
                } else {
                    valor = new BigDecimal(valorObj.toString());
                }
                itemMap.put("valor", valor);
            }

            itens.add(itemMap);
        }

        data.put("empregado", empregado);
        data.put("itens", itens);

        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml1("declaracao_devolucao_aparelho", data);

        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = "declaracao_devolucao_aparelho_" + pessoaDTO.getNome().replaceAll(" ", "_") + ".pdf";

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

        // Buscar itens do TipoVaga e resolver tamanhos
        List<Map<String, Object>> itens = buscarItensComTamanhos(vagaDTO, pessoaDTO);
        data.put("itens", itens);

        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml1("entrega_epi", data);

        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = "entrega_epi_" + pessoaDTO.getNome().replaceAll(" ", "_") + ".pdf";

        headers.setContentLength(pdfBytes.length);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/registro_entrega_epi/{vagaId}")
    public ResponseEntity<byte[]> downloadRegistroEntregaEpiPdf(@PathVariable Long vagaId) {

        VagaDTO vagaDTO = vagaService.obterVagaPorId(vagaId);
        PessoaDTO pessoaDTO = pessoaService.buscarPessoaPorId(vagaDTO.getPessoaId());

        Map<String, Object> data = new HashMap<>();

        Map<String, String> empregado = Map.ofEntries(
                entry("nome", pessoaDTO.getNome() != null ? pessoaDTO.getNome() : ""),
                entry("cpf", pessoaDTO.getCpf() != null ? pessoaDTO.getCpf() : ""),
                entry("endereco", pessoaDTO.getEndereco() != null ? pessoaDTO.getEndereco() : ""),
                entry("bairro", pessoaDTO.getBairro() != null ? pessoaDTO.getBairro() : ""),
                entry("cidade", pessoaDTO.getCidade() != null ? pessoaDTO.getCidade() : ""),
                entry("uf", pessoaDTO.getEstado() != null ? pessoaDTO.getEstado() : ""),
                entry("cep", pessoaDTO.getCep() != null ? pessoaDTO.getCep() : ""),
                entry("telefone", pessoaDTO.getTelefone() != null ? pessoaDTO.getTelefone() : "")
        );

        Map<String, Object> contrato = Map.of(
                "cliente", vagaDTO.getClienteNome() != null ? vagaDTO.getClienteNome() : "",
                "funcao", vagaDTO.getTipoVagaNome() != null ? vagaDTO.getTipoVagaNome() : "",
                "cidadeTrabalho", vagaDTO.getCidade() != null ? vagaDTO.getCidade() : "");

        data.put("empregado", empregado);
        data.put("contrato", contrato);
        data.put("dataAtualExtenso", obterDataPorExtenso());

        // Buscar itens do TipoVaga e resolver tamanhos
        List<Map<String, Object>> itens = buscarItensComTamanhos(vagaDTO, pessoaDTO);
        data.put("itens", itens);

        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml1("registro_entrega_epi", data);

        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = "registro_entrega_epi_" + pessoaDTO.getNome().replaceAll(" ", "_") + ".pdf";

        headers.setContentLength(pdfBytes.length);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    @GetMapping("/termo_devolucao_epi/{vagaId}")
    public ResponseEntity<byte[]> downloadTermoDevolucaoEpiPdf(@PathVariable Long vagaId) {

        VagaDTO vagaDTO = vagaService.obterVagaPorId(vagaId);
        PessoaDTO pessoaDTO = pessoaService.buscarPessoaPorId(vagaDTO.getPessoaId());

        Map<String, Object> data = new HashMap<>();

        Map<String, String> empregado = Map.ofEntries(
                entry("nome", pessoaDTO.getNome() != null ? pessoaDTO.getNome() : ""),
                entry("cpf", pessoaDTO.getCpf() != null ? pessoaDTO.getCpf() : ""),
                entry("endereco", pessoaDTO.getEndereco() != null ? pessoaDTO.getEndereco() : ""),
                entry("bairro", pessoaDTO.getBairro() != null ? pessoaDTO.getBairro() : ""),
                entry("cidade", pessoaDTO.getCidade() != null ? pessoaDTO.getCidade() : ""),
                entry("uf", pessoaDTO.getEstado() != null ? pessoaDTO.getEstado() : ""),
                entry("cep", pessoaDTO.getCep() != null ? pessoaDTO.getCep() : ""),
                entry("telefone", pessoaDTO.getTelefone() != null ? pessoaDTO.getTelefone() : "")
        );

        Map<String, Object> contrato = Map.of(
                "cliente", vagaDTO.getClienteNome() != null ? vagaDTO.getClienteNome() : "",
                "funcao", vagaDTO.getTipoVagaNome() != null ? vagaDTO.getTipoVagaNome() : "",
                "cidadeTrabalho", vagaDTO.getCidade() != null ? vagaDTO.getCidade() : "");

        data.put("empregado", empregado);
        data.put("contrato", contrato);

        // Buscar itens do TipoVaga e resolver tamanhos
        List<Map<String, Object>> itens = buscarItensComTamanhos(vagaDTO, pessoaDTO);
        data.put("itens", itens);

        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtml1("termo_devolucao_epi", data);

        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = "termo_devolucao_epi_" + pessoaDTO.getNome().replaceAll(" ", "_") + ".pdf";

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

            // termo_responsabilidade_materiais não usa vaga - possui endpoint próprio com pessoaId e itemIds

            case "entrega_epi":
                data.put("empregado", criarMapEmpregadoCompleto(pessoaDTO));
                data.put("contrato", criarMapContratoCompleto(vagaDTO));
                data.put("dataAtualExtenso", obterDataPorExtenso());
                // Buscar itens do TipoVaga e resolver tamanhos
                List<Map<String, Object>> itensEpi = buscarItensComTamanhos(vagaDTO, pessoaDTO);
                data.put("itens", itensEpi);
                break;

            case "registro_entrega_epi":
                Map<String, String> empregadoReg = Map.ofEntries(
                        entry("nome", pessoaDTO.getNome() != null ? pessoaDTO.getNome() : ""),
                        entry("cpf", pessoaDTO.getCpf() != null ? pessoaDTO.getCpf() : ""),
                        entry("endereco", pessoaDTO.getEndereco() != null ? pessoaDTO.getEndereco() : ""),
                        entry("bairro", pessoaDTO.getBairro() != null ? pessoaDTO.getBairro() : ""),
                        entry("cidade", pessoaDTO.getCidade() != null ? pessoaDTO.getCidade() : ""),
                        entry("uf", pessoaDTO.getEstado() != null ? pessoaDTO.getEstado() : ""),
                        entry("cep", pessoaDTO.getCep() != null ? pessoaDTO.getCep() : ""),
                        entry("telefone", pessoaDTO.getTelefone() != null ? pessoaDTO.getTelefone() : "")
                );
                Map<String, Object> contratoReg = Map.of(
                        "cliente", vagaDTO.getClienteNome() != null ? vagaDTO.getClienteNome() : "",
                        "funcao", vagaDTO.getTipoVagaNome() != null ? vagaDTO.getTipoVagaNome() : "",
                        "cidadeTrabalho", vagaDTO.getCidade() != null ? vagaDTO.getCidade() : "");
                data.put("empregado", empregadoReg);
                data.put("contrato", contratoReg);
                data.put("dataAtualExtenso", obterDataPorExtenso());
                List<Map<String, Object>> itensReg = buscarItensComTamanhos(vagaDTO, pessoaDTO);
                data.put("itens", itensReg);
                break;

            case "termo_devolucao_epi":
                Map<String, String> empregadoDev = Map.ofEntries(
                        entry("nome", pessoaDTO.getNome() != null ? pessoaDTO.getNome() : ""),
                        entry("cpf", pessoaDTO.getCpf() != null ? pessoaDTO.getCpf() : ""),
                        entry("endereco", pessoaDTO.getEndereco() != null ? pessoaDTO.getEndereco() : ""),
                        entry("bairro", pessoaDTO.getBairro() != null ? pessoaDTO.getBairro() : ""),
                        entry("cidade", pessoaDTO.getCidade() != null ? pessoaDTO.getCidade() : ""),
                        entry("uf", pessoaDTO.getEstado() != null ? pessoaDTO.getEstado() : ""),
                        entry("cep", pessoaDTO.getCep() != null ? pessoaDTO.getCep() : ""),
                        entry("telefone", pessoaDTO.getTelefone() != null ? pessoaDTO.getTelefone() : "")
                );
                Map<String, Object> contratoDev = Map.of(
                        "cliente", vagaDTO.getClienteNome() != null ? vagaDTO.getClienteNome() : "",
                        "funcao", vagaDTO.getTipoVagaNome() != null ? vagaDTO.getTipoVagaNome() : "",
                        "cidadeTrabalho", vagaDTO.getCidade() != null ? vagaDTO.getCidade() : "");
                data.put("empregado", empregadoDev);
                data.put("contrato", contratoDev);
                List<Map<String, Object>> itensDev = buscarItensComTamanhos(vagaDTO, pessoaDTO);
                data.put("itens", itensDev);
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
        Map<String, String> empregado = new HashMap<>();
        empregado.put("nome", pessoaDTO.getNome());
        empregado.put("estadoCivil", pessoaDTO.getEstadoCivil());
        empregado.put("rg", pessoaDTO.getNumeroRg());
        empregado.put("cpf", pessoaDTO.getCpf());
        empregado.put("ctps", pessoaDTO.getNumeroCtps());
        empregado.put("ctpsSerie", pessoaDTO.getSerieCtps());
        empregado.put("endereco", pessoaDTO.getEndereco());
        empregado.put("bairro", pessoaDTO.getBairro());
        empregado.put("cidade", pessoaDTO.getCidade());
        empregado.put("uf", pessoaDTO.getEstado());
        empregado.put("pix", pessoaDTO.getChavePix());
        empregado.put("cep", pessoaDTO.getCep());
        empregado.put("telefone", pessoaDTO.getTelefone());
        // Tamanhos de roupa/EPI
        empregado.put("tamanhoCamisa", pessoaDTO.getTamanhoCamisa());
        empregado.put("tamanhoCalca", pessoaDTO.getTamanhoCalca());
        empregado.put("tamanhoCalcado", pessoaDTO.getTamanhoCalcado());
        empregado.put("tamanhoLuva", pessoaDTO.getTamanhoLuva());
        empregado.put("tamanhoCapacete", pessoaDTO.getTamanhoCapacete());
        return empregado;
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

    /**
     * Busca os itens padrão do TipoVaga e resolve os tamanhos baseado na Pessoa.
     */
    private List<Map<String, Object>> buscarItensComTamanhos(VagaDTO vagaDTO, PessoaDTO pessoaDTO) {
        List<Map<String, Object>> itensPadrao = new ArrayList<>();

        if (vagaDTO.getTipoVagaId() != null) {
            Optional<TipoVaga> tipoVagaOpt = tipoVagaRepository.findById(vagaDTO.getTipoVagaId());
            if (tipoVagaOpt.isPresent()) {
                itensPadrao = tipoVagaOpt.get().getItensPadrao();
            }
        }

        return resolverTamanhosItens(itensPadrao != null ? itensPadrao : new ArrayList<>(), pessoaDTO);
    }

    /**
     * Resolve os tamanhos dos itens baseado nos campos de tamanho da Pessoa.
     */
    private List<Map<String, Object>> resolverTamanhosItens(List<Map<String, Object>> itens, PessoaDTO pessoaDTO) {
        if (itens == null || itens.isEmpty()) {
            return itens;
        }

        // Mapa de campos de tamanho da Pessoa
        Map<String, String> tamanhosPessoa = new HashMap<>();
        tamanhosPessoa.put("tamanhoCamisa", pessoaDTO.getTamanhoCamisa());
        tamanhosPessoa.put("tamanhoCalca", pessoaDTO.getTamanhoCalca());
        tamanhosPessoa.put("tamanhoCalcado", pessoaDTO.getTamanhoCalcado());
        tamanhosPessoa.put("tamanhoLuva", pessoaDTO.getTamanhoLuva());
        tamanhosPessoa.put("tamanhoCapacete", pessoaDTO.getTamanhoCapacete());

        List<Map<String, Object>> itensResolvidos = new ArrayList<>();
        for (Map<String, Object> item : itens) {
            Map<String, Object> itemCopia = new HashMap<>(item);

            // Se o item tem campoTamanhoPessoa, resolve o tamanho
            Object campoTamanho = itemCopia.get("campoTamanhoPessoa");
            if (campoTamanho != null && !campoTamanho.toString().isEmpty()) {
                String valorTamanho = tamanhosPessoa.get(campoTamanho.toString());
                if (valorTamanho != null && !valorTamanho.isEmpty()) {
                    itemCopia.put("tamanho", valorTamanho);
                }
            }

            itensResolvidos.add(itemCopia);
        }

        return itensResolvidos;
    }

    /**
     * Calcula o valor total dos itens.
     */
    private BigDecimal calcularValorTotal(List<Map<String, Object>> itens) {
        BigDecimal total = BigDecimal.ZERO;
        for (Map<String, Object> item : itens) {
            Object valor = item.get("valor");
            if (valor != null) {
                if (valor instanceof BigDecimal) {
                    total = total.add((BigDecimal) valor);
                } else if (valor instanceof Number) {
                    total = total.add(BigDecimal.valueOf(((Number) valor).doubleValue()));
                }
            }
        }
        return total;
    }
}
