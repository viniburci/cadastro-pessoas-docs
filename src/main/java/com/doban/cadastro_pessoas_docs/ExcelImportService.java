package com.doban.cadastro_pessoas_docs;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.doban.cadastro_pessoas_docs.domain.pessoa.DadosBancarios;
import com.doban.cadastro_pessoas_docs.domain.pessoa.DadosBancariosDTO;
import com.doban.cadastro_pessoas_docs.domain.pessoa.Pessoa;
import com.doban.cadastro_pessoas_docs.domain.pessoa.PessoaExcelDTO;
import com.doban.cadastro_pessoas_docs.domain.pessoa.PessoaRepository;
import com.doban.cadastro_pessoas_docs.domain.pessoa.TipoConta;
import com.doban.cadastro_pessoas_docs.domain.vaga.AtestadoSaudeOcupacional;
import com.doban.cadastro_pessoas_docs.domain.vaga.TipoAcrescimoSubstituicao;
import com.doban.cadastro_pessoas_docs.domain.vaga.TipoContratante;
import com.doban.cadastro_pessoas_docs.domain.vaga.TipoContrato;
import com.doban.cadastro_pessoas_docs.domain.vaga.Vaga;
import com.doban.cadastro_pessoas_docs.domain.vaga.VagaDTO;
import com.doban.cadastro_pessoas_docs.domain.vaga.VagaRepository;
import com.doban.cadastro_pessoas_docs.recurso.dinamico.RecursoDinamico;
import com.doban.cadastro_pessoas_docs.recurso.dinamico.RecursoDinamicoRepository;
import com.doban.cadastro_pessoas_docs.recurso.item.ItemDinamico;
import com.doban.cadastro_pessoas_docs.recurso.item.ItemDinamicoService;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ExcelImportService {

    private final PessoaRepository pessoaRepository;
    private final VagaRepository vagaRepository;
    private final ItemDinamicoService itemDinamicoService;
    private final RecursoDinamicoRepository recursoDinamicoRepository;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter dtfPortugueseAbbr = DateTimeFormatter.ofPattern("dd-MMM-yyyy",
            new Locale("pt", "BR"));
    private final DateTimeFormatter dtfPortugueseFull = DateTimeFormatter.ofPattern("dd-MMMM-yyyy",
            new Locale("pt", "BR"));

    public ExcelImportService(
            PessoaRepository pessoaRepository,
            VagaRepository vagaRepository,
            ItemDinamicoService itemDinamicoService,
            RecursoDinamicoRepository recursoDinamicoRepository) {
        this.pessoaRepository = pessoaRepository;
        this.vagaRepository = vagaRepository;
        this.itemDinamicoService = itemDinamicoService;
        this.recursoDinamicoRepository = recursoDinamicoRepository;
    }

    public void importar(String caminhoArquivo) throws IOException {
        try (FileInputStream fis = new FileInputStream(caminhoArquivo);
                Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(4);
            Set<String> cpfsImportados = new HashSet<>();

            // Detectar última linha com dados (máximo 309)
            int ultimaLinha = 46;
            System.out.println("📊 Iniciando importação do Excel. Linhas para processar: " + (ultimaLinha - 10));

            int importadas = 0;
            int puladas = 0;

            // Processar de trás para frente (última linha até linha 11)
            for (int i = ultimaLinha; i >= 11; i--) {
                Row row = sheet.getRow(i);
                if (row == null || isLinhaVazia(row)) {
                    puladas++;
                    continue;
                }

                try {
                    ImportacaoDTO importacaoDto = lerLinhaDTO(row);
                    importarPessoa(importacaoDto, row);
                    cpfsImportados.add(importacaoDto.getPessoa().getCpf());
                    importadas++;

                    if (importadas % 10 == 0) {
                        System.out.println("✓ Importadas: " + importadas + " pessoas");
                    }
                } catch (Exception e) {
                    System.out.println("⚠ Erro ao importar linha " + (i + 1) + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }

            System.out.println("✅ Importação concluída! Total: " + importadas + " pessoas importadas, " + puladas + " linhas vazias puladas.");
        }
    }

    private ImportacaoDTO lerLinhaDTO(Row row) {
        PessoaExcelDTO pessoaDto = new PessoaExcelDTO();

        pessoaDto.setNome(getString(row, 2));
        pessoaDto.setEndereco(getString(row, 3));
        pessoaDto.setBairro(getString(row, 4));
        pessoaDto.setCidade(getString(row, 5));
        pessoaDto.setEstado(getString(row, 6));
        pessoaDto.setCep(getString(row, 7));
        pessoaDto.setTelefone(getString(row, 8).trim() + getString(row, 9));
        pessoaDto.setEmail(getString(row, 10));
        pessoaDto.setNumeroCtps(getString(row, 11));
        pessoaDto.setSerieCtps(getString(row, 12));
        pessoaDto.setDataEmissaoCtps(parseDate(row, 13));
        pessoaDto.setNumeroRg(getString(row, 14));
        pessoaDto.setDataEmissaoRg(parseDate(row, 15));
        pessoaDto.setUfRg(getString(row, 16));

        pessoaDto.setPis(getString(row, 18));
        pessoaDto.setDataEmissaoPis(parseDate(row, 19));
        pessoaDto.setTituloEleitor(getString(row, 20));
        pessoaDto.setDataNascimento(parseDate(row, 21));
        pessoaDto.setLocalNascimento(getString(row, 22));
        pessoaDto.setMae(getString(row, 23));
        pessoaDto.setPai(getString(row, 24));
        pessoaDto.setEstadoCivil(getString(row, 25));

        pessoaDto.setNumeroCnh(getString(row, 45));
        pessoaDto.setRegistroCnh(getString(row, 46));

        pessoaDto.setCategoriaCnh(getString(row, 66));
        pessoaDto.setValidadeCnh(parseDate(row, 67));

        pessoaDto.setChavePix(getString(row, 52));

        String cpf = getString(row, 17);
        if (cpf.length() < 11) {
            int n = 11 - cpf.length();
            cpf = "0".repeat(n) + cpf;
        }
        pessoaDto.setCpf(cpf);

        VagaDTO vagaDto = VagaDTO.builder()
                .cliente(getString(row, 26))
                .cidade(getString(row, 27))
                .uf(getString(row, 28))
                .cargo(getString(row, 29))
                .setor(getString(row, 30))
                .salario(parseBigDecimal(getString(row, 31)))
                .dataAdmissao(parseDate(row, 33))
                .dataDemissao(parseDate(row, 34))
                .horarioEntrada(getHorario(row, 50))
                .horarioSaida(getHorario(row, 51))
                .contratante(TipoContratante.DOBAN_PRESTADORA_DE_SERVIÇOS_LTDA) // 1 - 2
                .build();

        String tipoContratoStr = Optional.ofNullable(getString(row, 32))
                .map(String::trim)
                .orElse("6");
        switch (String.valueOf(tipoContratoStr.charAt(0))) {
            case "1" -> vagaDto.setTipoContrato(TipoContrato.CLT_CE_CJ);
            case "2" -> vagaDto.setTipoContrato(TipoContrato.CLT_CE_SJ);
            case "4" -> vagaDto.setTipoContrato(TipoContrato.CLT_SE_SJ);
            case "5" -> vagaDto.setTipoContrato(TipoContrato.TEMP_CJ);
            default -> vagaDto.setTipoContrato(TipoContrato.INDEFINIDO);
        }

        AtestadoSaudeOcupacional aso = null;
        if (getString(row, 39) != null && getString(row, 39).equals("X")) {
            aso = AtestadoSaudeOcupacional.ADMISSIONAL;
        } else if (getString(row, 40) != null && getString(row, 40).equals("X")) {
            aso = AtestadoSaudeOcupacional.DEMISSIONAL;
        } else if (getString(row, 41) != null && getString(row, 41).equals("X")) {
            aso = AtestadoSaudeOcupacional.RETORNO;
        }
        vagaDto.setAso(aso);

        Boolean optanteVT = null;
        if (getString(row, 47) != null && getString(row, 47).equals("X")) {
            optanteVT = true;
        } else if (getString(row, 48) != null && getString(row, 48).equals("X")) {
            optanteVT = false;
        }
        vagaDto.setOptanteVT(optanteVT);

        TipoAcrescimoSubstituicao tipoAcrescimoSubstituicao = null;
        if (getString(row, 37) != null && getString(row, 37).equals("X")) {
            tipoAcrescimoSubstituicao = TipoAcrescimoSubstituicao.ACRESCIMO;
        } else if (getString(row, 38) != null && getString(row, 38).equals("X")) {
            tipoAcrescimoSubstituicao = TipoAcrescimoSubstituicao.SUBSTITUICAO;
        }
        vagaDto.setAcrescimoOuSubstituicao(tipoAcrescimoSubstituicao);


        // Foto será null - deve ser adicionada manualmente via API
        // Campo foto existe no banco (BYTEA) e pode ser populado posteriormente

        // Ler dados bancários (colunas 42-44)
        DadosBancariosDTO dadosBancariosDto =
                DadosBancariosDTO.builder()
                .banco(getString(row, 42))
                .agencia(getString(row, 43))
                .conta(getString(row, 44))
                .tipoConta(inferirTipoConta(getString(row, 44)))
                .build();

        ImportacaoDTO importacaoDto = new ImportacaoDTO();
        importacaoDto.setPessoa(pessoaDto);
        importacaoDto.setVaga(vagaDto);
        importacaoDto.setFoto(null); // Foto será adicionada via API posteriormente
        importacaoDto.setDadosBancarios(dadosBancariosDto);

        return importacaoDto;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    private void importarPessoa(ImportacaoDTO importacaoDto, Row row) {
        Optional<Pessoa> existente = pessoaRepository.findByCpf(importacaoDto.getPessoa().getCpf());

        Pessoa pessoa = new Pessoa();

        if (existente.isPresent()) {
            System.out.println("Pessoa já existe: " + importacaoDto.getPessoa().getNome() + ". Atualizando dados...");
            pessoa = atualizarPessoaExistente(existente.get(), importacaoDto.getPessoa().toEntity());
        } else {
            pessoa = importacaoDto.getPessoa().toEntity();
        }

        // Salvar foto se disponível
        if (importacaoDto.getFoto() != null && importacaoDto.getFoto().length > 0) {
            pessoa.setFoto(importacaoDto.getFoto());
        }

        // Salvar dados bancários se disponíveis ANTES de salvar a pessoa
        if (importacaoDto.getDadosBancarios() != null &&
            !importacaoDto.getDadosBancarios().isEmpty()) {

            DadosBancarios dadosBancarios =
                importacaoDto.getDadosBancarios().toEntity();
            dadosBancarios.setPessoa(pessoa);
            pessoa.setDadosBancarios(dadosBancarios);
        }

        // Salvar a pessoa primeiro para obter o ID
        Pessoa pessoaBanco = pessoaRepository.save(pessoa);

        // Importar CARRO usando sistema dinâmico
        String placa = getString(row, 57);
        if (placa != null && !placa.isBlank()) {
            try {
                Map<String, Object> atributosCarro = new HashMap<>();
                atributosCarro.put("marca", getString(row, 54));
                atributosCarro.put("modelo", getString(row, 58));
                atributosCarro.put("cor", getString(row, 55));
                atributosCarro.put("chassi", getString(row, 56));
                atributosCarro.put("anoModelo", getString(row, 61));

                // Buscar ou criar item de carro
                ItemDinamico itemCarro = itemDinamicoService.buscarOuCriarPorIdentificador(
                    "CARRO",
                    placa,
                    atributosCarro
                );

                // Criar empréstimo se não existir já um ativo para este item e pessoa
                if (!recursoDinamicoRepository.existsByItemIdAndPessoaIdAndDataDevolucaoIsNull(
                        itemCarro.getId(), pessoaBanco.getId())) {

                    RecursoDinamico emprestimoCarro = RecursoDinamico.builder()
                        .pessoa(pessoaBanco)
                        .item(itemCarro)
                        .dataEntrega(importacaoDto.getVaga() != null ?
                            importacaoDto.getVaga().getDataAdmissao() : LocalDate.now())
                        .dataDevolucao(importacaoDto.getVaga() != null ?
                            importacaoDto.getVaga().getDataDemissao() : null)
                        .build();

                    recursoDinamicoRepository.save(emprestimoCarro);
                }
            } catch (Exception e) {
                System.out.println("⚠️ Erro ao importar carro: " + e.getMessage());
            }
        }

        // Importar CELULAR usando sistema dinâmico
        String imei = getString(row, 65);
        if (imei != null && imei.length() >= 10) {
            try {
                Map<String, Object> atributosCelular = new HashMap<>();
                atributosCelular.put("marca", getString(row, 62));
                atributosCelular.put("modelo", getString(row, 63));
                atributosCelular.put("chip", getString(row, 64));

                // Buscar ou criar item de celular
                ItemDinamico itemCelular = itemDinamicoService.buscarOuCriarPorIdentificador(
                    "CELULAR",
                    imei,
                    atributosCelular
                );

                // Criar empréstimo se não existir já um ativo para este item e pessoa
                if (!recursoDinamicoRepository.existsByItemIdAndPessoaIdAndDataDevolucaoIsNull(
                        itemCelular.getId(), pessoaBanco.getId())) {

                    RecursoDinamico emprestimoCelular = RecursoDinamico.builder()
                        .pessoa(pessoaBanco)
                        .item(itemCelular)
                        .dataEntrega(importacaoDto.getVaga() != null ?
                            importacaoDto.getVaga().getDataAdmissao() : LocalDate.now())
                        .dataDevolucao(importacaoDto.getVaga() != null ?
                            importacaoDto.getVaga().getDataDemissao() : null)
                        .build();

                    recursoDinamicoRepository.save(emprestimoCelular);
                }
            } catch (Exception e) {
                System.out.println("⚠️ Erro ao importar celular: " + e.getMessage());
            }
        }

        // Importar vaga
        if (importacaoDto.getVaga() != null) {
            Vaga vaga = importacaoDto.getVaga().toEntity(pessoaBanco);
            vagaRepository.save(vaga);
            pessoaBanco.getVagas().add(vaga);
            pessoaRepository.save(pessoaBanco);
        }
    }

    private Pessoa atualizarPessoaExistente(Pessoa existente, Pessoa nova) {
        existente.setNome(nova.getNome());
        existente.setEndereco(nova.getEndereco());
        existente.setComplemento(nova.getComplemento());
        existente.setBairro(nova.getBairro());
        existente.setCidade(nova.getCidade());
        existente.setEstado(nova.getEstado());
        existente.setCep(nova.getCep());
        existente.setTelefone(nova.getTelefone());
        existente.setEmail(nova.getEmail());
        existente.setNumeroCtps(nova.getNumeroCtps());
        existente.setSerieCtps(nova.getSerieCtps());
        existente.setDataEmissaoCtps(nova.getDataEmissaoCtps());
        existente.setNumeroRg(nova.getNumeroRg());
        existente.setDataEmissaoRg(nova.getDataEmissaoRg());
        existente.setUfRg(nova.getUfRg());
        existente.setPis(nova.getPis());
        existente.setDataEmissaoPis(nova.getDataEmissaoPis());
        existente.setTituloEleitor(nova.getTituloEleitor());
        existente.setDataNascimento(nova.getDataNascimento());
        existente.setLocalNascimento(nova.getLocalNascimento());
        existente.setMae(nova.getMae());
        existente.setPai(nova.getPai());
        existente.setEstadoCivil(nova.getEstadoCivil());
        existente.setNumeroCnh(nova.getNumeroCnh());
        existente.setRegistroCnh(nova.getRegistroCnh());
        existente.setCategoriaCnh(nova.getCategoriaCnh());
        existente.setValidadeCnh(nova.getValidadeCnh());

        return pessoaRepository.save(existente);
    }

    private static final DataFormatter formatter = new DataFormatter();

    private String getString(Row row, int index) {
        Cell cell = row.getCell(index);
        return (cell != null) ? formatter.formatCellValue(cell).trim() : null;
    }

    private LocalDate parseDate(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            // System.out.println("Célula vazia ou nula na coluna " + index);
            return null;
        }

        try {
            if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
                LocalDate date = cell.getLocalDateTimeCellValue().toLocalDate();
                // System.out.println("Data numérica detectada: " + date);
                return date;
            }

            // Se for texto, limpa antes de qualquer verificação
            String valorOriginal = cell.toString().trim();
            System.out.println("Valor original da célula: '" + valorOriginal + "'");

            // Remove pontos e espaços extras
            String valorLimpo = valorOriginal.replace(".", "").trim();
            // System.out.println("Valor limpo para parsing: '" + valorLimpo + "'");

            if (valorLimpo.isEmpty()) {
                System.out.println("Valor da célula está vazio após limpeza.");
                return null;
            }

            // Tenta parsing com diferentes formatos
            List<DateTimeFormatter> formatters = Arrays.asList(dtf, dtfPortugueseAbbr, dtfPortugueseFull);
            for (DateTimeFormatter formatter : formatters) {
                try {
                    LocalDate parsedDate = LocalDate.parse(valorLimpo, formatter);
                    // System.out.println("Data convertida com formato '" + formatter + "': " +
                    // parsedDate);
                    return parsedDate;
                } catch (DateTimeParseException e) {
                    // System.out.println("Falha ao tentar converter com formato '" + formatter +
                    // "': " + e.getMessage());
                }
            }

            System.out.println("❌ Nenhum formato conseguiu converter a data: '" + valorOriginal + "'");
            return null;

        } catch (Exception e) {
            System.out.println("Erro inesperado ao processar data na coluna " + index + ": " + e.getMessage());
            return null;
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isBlank())
            return null;

        try {
            String cleaned = value.trim();

            // Remove "R$", espaços e outros caracteres que não sejam dígitos, ponto ou
            // vírgula
            cleaned = cleaned.replaceAll("[^\\d.,]", "");

            // Conta quantos pontos e vírgulas existem
            boolean temVirgula = cleaned.contains(",");
            boolean temPonto = cleaned.contains(".");

            if (temVirgula && temPonto) {
                // Ex: 1.800,00 → 1800.00 (formato brasileiro)
                cleaned = cleaned.replace(".", "").replace(",", ".");
            } else if (temVirgula) {
                // Ex: 1800,00 → 1800.00
                cleaned = cleaned.replace(",", ".");
            }
            // Se só tiver ponto, pode ser americano ou milhar — deixamos como está

            BigDecimal parsed = new BigDecimal(cleaned);

            // Corrige valores que vieram em centavos (ex: 180000 → 1800.00)
            if (parsed.compareTo(BigDecimal.valueOf(10000)) > 0) {
                parsed = parsed.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP);
            }

            return parsed.setScale(2, RoundingMode.HALF_UP);

        } catch (Exception e) {
            System.out.println("Erro ao parsear valor monetário: " + value);
            return null;
        }
    }

    private LocalTime getHorario(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell == null)
            return null;
        // System.out.println("célula de horário: '" + cell.toString() + "'");
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            Date date = cell.getDateCellValue();
            LocalTime time = date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalTime();

            // Arredondar para o minuto mais próximo
            int seconds = time.getSecond();
            time = time.withSecond(0).withNano(0);
            if (seconds >= 30) {
                time = time.plusMinutes(7);
            }

            return time;
        }

        // Se vier como texto
        if (cell.getCellType() == CellType.STRING) {
            String valor = cell.getStringCellValue().trim();
            if (!valor.isBlank()) {
                try {
                    LocalTime time = LocalTime.parse(valor, DateTimeFormatter.ofPattern("H:mm[:ss]"));
                    // Arredondar para o minuto mais próximo
                    int seconds = time.getSecond();
                    time = time.withSecond(0).withNano(0);
                    if (seconds >= 30) {
                        time = time.plusMinutes(1);
                    }
                    return time;
                } catch (Exception e) {
                    System.out.println("Erro ao parsear horário: " + valor);
                }
            }
        }

        return null;
    }

    public boolean hasPessoas() {
        return pessoaRepository.count() > 0;
    }

    /**
     * Verifica se uma linha do Excel está vazia (sem nome e sem CPF).
     *
     * @param row Linha do Excel
     * @return true se a linha estiver vazia
     */
    private boolean isLinhaVazia(Row row) {
        Cell nomeCell = row.getCell(2);
        Cell cpfCell = row.getCell(17);
        return (nomeCell == null || nomeCell.toString().isBlank()) &&
               (cpfCell == null || cpfCell.toString().isBlank());
    }

    /**
     * Infere o tipo de conta bancária baseado no texto da conta.
     *
     * @param conta String com número/descrição da conta
     * @return TipoConta inferido
     */
    private TipoConta inferirTipoConta(String conta) {
        if (conta == null || conta.isBlank()) {
            return TipoConta.CORRENTE;
        }

        String lower = conta.toLowerCase();
        if (lower.contains("poupança") || lower.contains("poupanca")) {
            return TipoConta.POUPANCA;
        }
        if (lower.contains("salário") || lower.contains("salario")) {
            return TipoConta.SALARIO;
        }

        return TipoConta.CORRENTE;
    }

}
