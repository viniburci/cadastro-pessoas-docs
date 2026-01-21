package com.doban.cadastro_pessoas_docs.documentos;

import com.doban.cadastro_pessoas_docs.documentos.template.TemplateDocumento;
import com.doban.cadastro_pessoas_docs.documentos.template.TemplateDocumentoService;
import com.doban.cadastro_pessoas_docs.domain.pessoa.PessoaDTO;
import com.doban.cadastro_pessoas_docs.domain.pessoa.PessoaService;
import com.doban.cadastro_pessoas_docs.domain.vaga.VagaDTO;
import com.doban.cadastro_pessoas_docs.domain.vaga.VagaService;
import com.doban.cadastro_pessoas_docs.domain.vaga.tipo.TipoVaga;
import com.doban.cadastro_pessoas_docs.domain.vaga.tipo.TipoVagaRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Controller para geração dinâmica de documentos usando templates do banco de dados.
 */
@RestController
@RequestMapping("/api/v1/documentos-dinamicos")
public class DocumentoGeracaoController {

    private final TemplateDocumentoService templateDocumentoService;
    private final VagaService vagaService;
    private final PessoaService pessoaService;
    private final TipoVagaRepository tipoVagaRepository;
    private final PdfGeneratorService pdfGeneratorService;

    public DocumentoGeracaoController(TemplateDocumentoService templateDocumentoService,
                                      VagaService vagaService,
                                      PessoaService pessoaService,
                                      TipoVagaRepository tipoVagaRepository,
                                      PdfGeneratorService pdfGeneratorService) {
        this.templateDocumentoService = templateDocumentoService;
        this.vagaService = vagaService;
        this.pessoaService = pessoaService;
        this.tipoVagaRepository = tipoVagaRepository;
        this.pdfGeneratorService = pdfGeneratorService;
    }

    /**
     * Retorna os dados pré-preenchidos para um template e vaga específicos.
     * O frontend usa esses dados para exibir o formulário de edição.
     */
    @GetMapping("/dados/{templateCodigo}/{vagaId}")
    public ResponseEntity<Map<String, Object>> obterDadosParaTemplate(
            @PathVariable String templateCodigo,
            @PathVariable Long vagaId) {

        VagaDTO vagaDTO = vagaService.obterVagaPorId(vagaId);
        PessoaDTO pessoaDTO = pessoaService.buscarPessoaPorId(vagaDTO.getPessoaId());

        Map<String, Object> dados = new HashMap<>();
        dados.put("pessoa", criarMapPessoa(pessoaDTO));
        dados.put("vaga", criarMapVaga(vagaDTO));
        dados.put("dataAtualExtenso", obterDataPorExtenso());

        // Buscar itens padrão do TipoVaga se existir
        List<Map<String, Object>> itensPadrao = new ArrayList<>();
        if (vagaDTO.getTipoVagaId() != null) {
            Optional<TipoVaga> tipoVagaOpt = tipoVagaRepository.findById(vagaDTO.getTipoVagaId());
            if (tipoVagaOpt.isPresent()) {
                itensPadrao = tipoVagaOpt.get().getItensPadrao();
            }
        }
        dados.put("itens", itensPadrao != null ? itensPadrao : new ArrayList<>());

        // Incluir schema do template para o frontend saber a estrutura dos itens
        TemplateDocumento template = templateDocumentoService.buscarEntidadePorCodigo(templateCodigo);
        if (template.getSchemaItens() != null) {
            dados.put("schemaItens", template.getSchemaItens());
        }

        return ResponseEntity.ok(dados);
    }

    /**
     * Gera o PDF usando o template do banco e os dados enviados pelo frontend.
     */
    @PostMapping("/gerar/{templateCodigo}/{vagaId}")
    public ResponseEntity<byte[]> gerarDocumento(
            @PathVariable String templateCodigo,
            @PathVariable Long vagaId,
            @RequestBody(required = false) Map<String, Object> dadosEditados) {

        VagaDTO vagaDTO = vagaService.obterVagaPorId(vagaId);
        PessoaDTO pessoaDTO = pessoaService.buscarPessoaPorId(vagaDTO.getPessoaId());
        TemplateDocumento template = templateDocumentoService.buscarEntidadePorCodigo(templateCodigo);

        // Montar dados base
        Map<String, Object> dados = new HashMap<>();
        dados.put("pessoa", criarMapPessoa(pessoaDTO));
        dados.put("vaga", criarMapVaga(vagaDTO));
        dados.put("dataAtualExtenso", obterDataPorExtenso());

        // Mesclar com dados editados do frontend
        if (dadosEditados != null) {
            // Itens editados têm prioridade
            if (dadosEditados.containsKey("itens")) {
                dados.put("itens", dadosEditados.get("itens"));
            }
            // Outros campos customizados
            if (dadosEditados.containsKey("outrosCampos")) {
                @SuppressWarnings("unchecked")
                Map<String, Object> outrosCampos = (Map<String, Object>) dadosEditados.get("outrosCampos");
                dados.putAll(outrosCampos);
            }
        }

        // Se não há itens editados, usar itens padrão do TipoVaga
        if (!dados.containsKey("itens") || dados.get("itens") == null) {
            List<Map<String, Object>> itensPadrao = new ArrayList<>();
            if (vagaDTO.getTipoVagaId() != null) {
                Optional<TipoVaga> tipoVagaOpt = tipoVagaRepository.findById(vagaDTO.getTipoVagaId());
                if (tipoVagaOpt.isPresent()) {
                    itensPadrao = tipoVagaOpt.get().getItensPadrao();
                }
            }
            dados.put("itens", itensPadrao != null ? itensPadrao : new ArrayList<>());
        }

        // Gerar PDF usando o template do banco
        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtmlString(template.getConteudoHtml(), dados);

        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = template.getCodigo().toLowerCase() + "_" + pessoaDTO.getNome().replaceAll(" ", "_") + ".pdf";

        headers.setContentLength(pdfBytes.length);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    /**
     * Retorna o HTML renderizado para preview (sem converter para PDF).
     */
    @GetMapping("/preview/{templateCodigo}/{vagaId}")
    public ResponseEntity<String> previewDocumento(
            @PathVariable String templateCodigo,
            @PathVariable Long vagaId) {

        VagaDTO vagaDTO = vagaService.obterVagaPorId(vagaId);
        PessoaDTO pessoaDTO = pessoaService.buscarPessoaPorId(vagaDTO.getPessoaId());
        TemplateDocumento template = templateDocumentoService.buscarEntidadePorCodigo(templateCodigo);

        Map<String, Object> dados = new HashMap<>();
        dados.put("pessoa", criarMapPessoa(pessoaDTO));
        dados.put("vaga", criarMapVaga(vagaDTO));
        dados.put("dataAtualExtenso", obterDataPorExtenso());

        // Buscar itens padrão do TipoVaga
        List<Map<String, Object>> itensPadrao = new ArrayList<>();
        if (vagaDTO.getTipoVagaId() != null) {
            Optional<TipoVaga> tipoVagaOpt = tipoVagaRepository.findById(vagaDTO.getTipoVagaId());
            if (tipoVagaOpt.isPresent()) {
                itensPadrao = tipoVagaOpt.get().getItensPadrao();
            }
        }
        dados.put("itens", itensPadrao != null ? itensPadrao : new ArrayList<>());

        String htmlRenderizado = pdfGeneratorService.renderHtmlFromString(template.getConteudoHtml(), dados);

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_HTML)
                .body(htmlRenderizado);
    }

    /**
     * Gera PDF a partir de HTML customizado enviado pelo frontend.
     */
    @PostMapping("/gerar-custom/{vagaId}")
    public ResponseEntity<byte[]> gerarDocumentoCustom(
            @PathVariable Long vagaId,
            @RequestBody Map<String, Object> request) {

        VagaDTO vagaDTO = vagaService.obterVagaPorId(vagaId);
        PessoaDTO pessoaDTO = pessoaService.buscarPessoaPorId(vagaDTO.getPessoaId());

        String htmlContent = (String) request.get("htmlContent");
        if (htmlContent == null || htmlContent.isEmpty()) {
            throw new IllegalArgumentException("htmlContent é obrigatório");
        }

        // Dados para processar placeholders no HTML
        Map<String, Object> dados = new HashMap<>();
        dados.put("pessoa", criarMapPessoa(pessoaDTO));
        dados.put("vaga", criarMapVaga(vagaDTO));
        dados.put("dataAtualExtenso", obterDataPorExtenso());

        if (request.containsKey("itens")) {
            dados.put("itens", request.get("itens"));
        }

        byte[] pdfBytes = pdfGeneratorService.generatePdfFromHtmlString(htmlContent, dados);

        HttpHeaders headers = new HttpHeaders();
        String nomeArquivo = "documento_custom_" + pessoaDTO.getNome().replaceAll(" ", "_") + ".pdf";

        headers.setContentLength(pdfBytes.length);
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + nomeArquivo);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

    private Map<String, Object> criarMapPessoa(PessoaDTO pessoaDTO) {
        Map<String, Object> pessoa = new HashMap<>();
        pessoa.put("id", pessoaDTO.getId());
        pessoa.put("nome", pessoaDTO.getNome());
        pessoa.put("cpf", pessoaDTO.getCpf());
        pessoa.put("rg", pessoaDTO.getNumeroRg());
        pessoa.put("estadoCivil", pessoaDTO.getEstadoCivil());
        pessoa.put("endereco", pessoaDTO.getEndereco());
        pessoa.put("bairro", pessoaDTO.getBairro());
        pessoa.put("cidade", pessoaDTO.getCidade());
        pessoa.put("uf", pessoaDTO.getEstado());
        pessoa.put("cep", pessoaDTO.getCep());
        pessoa.put("telefone", pessoaDTO.getTelefone());
        pessoa.put("ctps", pessoaDTO.getNumeroCtps());
        pessoa.put("ctpsSerie", pessoaDTO.getSerieCtps());
        pessoa.put("pix", pessoaDTO.getChavePix());
        return pessoa;
    }

    private Map<String, Object> criarMapVaga(VagaDTO vagaDTO) {
        Map<String, Object> vaga = new HashMap<>();
        vaga.put("id", vagaDTO.getId());
        vaga.put("cargo", vagaDTO.getCargo());
        vaga.put("cliente", vagaDTO.getCliente());
        vaga.put("clienteNome", vagaDTO.getClienteNome());
        vaga.put("cidade", vagaDTO.getCidade());
        vaga.put("uf", vagaDTO.getUf());
        vaga.put("salario", vagaDTO.getSalario());
        vaga.put("dataAdmissao", vagaDTO.getDataAdmissao());
        vaga.put("dataDemissao", vagaDTO.getDataDemissao());
        vaga.put("horarioEntrada", vagaDTO.getHorarioEntrada());
        vaga.put("horarioSaida", vagaDTO.getHorarioSaida());
        vaga.put("tipoVagaCodigo", vagaDTO.getTipoVagaCodigo());
        vaga.put("tipoVagaNome", vagaDTO.getTipoVagaNome());
        return vaga;
    }

    private String obterDataPorExtenso() {
        LocalDate hoje = LocalDate.now();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern(
                "d 'de' MMMM 'de' yyyy",
                new Locale.Builder().setLanguage("pt").setRegion("BR").build());
        return hoje.format(formatador);
    }
}
