package com.doban.cadastro_pessoas_docs;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.doban.cadastro_pessoas_docs.carro.Carro;
import com.doban.cadastro_pessoas_docs.carro.CarroRepository;
import com.doban.cadastro_pessoas_docs.celular.Celular;
import com.doban.cadastro_pessoas_docs.celular.CelularRepository;
import com.doban.cadastro_pessoas_docs.pessoa.Pessoa;
import com.doban.cadastro_pessoas_docs.pessoa.PessoaExcelDTO;
import com.doban.cadastro_pessoas_docs.pessoa.PessoaRepository;
import com.doban.cadastro_pessoas_docs.pessoa.PessoaExcelDTO.CarroDTO;
import com.doban.cadastro_pessoas_docs.pessoa.PessoaExcelDTO.CelularDTO;
import com.doban.cadastro_pessoas_docs.pessoa.PessoaExcelDTO.VagaDTO;
import com.doban.cadastro_pessoas_docs.recurso.Recurso;
import com.doban.cadastro_pessoas_docs.recurso.RecursoCarro;
import com.doban.cadastro_pessoas_docs.recurso.RecursoCelular;
import com.doban.cadastro_pessoas_docs.vaga.TipoContrato;
import com.doban.cadastro_pessoas_docs.vaga.Vaga;
import com.doban.cadastro_pessoas_docs.ImportacaoDTO;

import jakarta.transaction.Transactional;

@Service
public class ExcelImportService {

    private final PessoaRepository pessoaRepository;
    private final CarroRepository carroRepository;
    private final CelularRepository celularRepository;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

    private final DateTimeFormatter dtfPortuguese = DateTimeFormatter.ofPattern("dd-MMM-yyyy", new Locale("pt", "BR"));

    public ExcelImportService(PessoaRepository pessoaRepository,
            CarroRepository carroRepository,
            CelularRepository celularRepository) {
        this.pessoaRepository = pessoaRepository;
        this.carroRepository = carroRepository;
        this.celularRepository = celularRepository;
    }

    @Transactional
    public void importar(String caminhoArquivo) throws IOException {
        try (FileInputStream fis = new FileInputStream(caminhoArquivo);
                Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(4);
            Set<String> cpfsImportados = new HashSet<>();

            for (int i = 12; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;

                ImportacaoDTO importacaoDto = lerLinhaDTO(row);

                if (importacaoDto.getPessoa().getCpf() == null
                        || cpfsImportados.contains(importacaoDto.getPessoa().getCpf())) {
                    System.out.println("Ignorando duplicado: " + importacaoDto.getPessoa().getNome());
                    continue;
                }

                importarPessoa(importacaoDto);
                cpfsImportados.add(importacaoDto.getPessoa().getCpf());
            }
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
        pessoaDto.setDdd(getString(row, 8));
        pessoaDto.setTelefone(getString(row, 9));
        pessoaDto.setEmail(getString(row, 10));
        pessoaDto.setNumeroCtps(getString(row, 11));
        pessoaDto.setSerieCtps(getString(row, 12));
        pessoaDto.setDataEmissaoCtps(parseDate(row, 13));
        pessoaDto.setNumeroRg(getString(row, 14));
        pessoaDto.setDataEmissaoRg(parseDate(row, 15));
        pessoaDto.setUfRg(getString(row, 16));
        pessoaDto.setCpf(getString(row, 17));
        pessoaDto.setPis(getString(row, 18));
        pessoaDto.setDataEmissaoPis(parseDate(row, 19));
        pessoaDto.setTituloEleitor(getString(row, 20));
        pessoaDto.setDataNascimento(parseDate(row, 21));
        pessoaDto.setLocalNascimento(getString(row, 22));
        pessoaDto.setMae(getString(row, 23));
        pessoaDto.setPai(getString(row, 24));
        pessoaDto.setEstadoCivil(getString(row, 25));


        VagaDTO vagaDto = VagaDTO.builder()
                .cliente(getString(row, 26))
                .cidade(getString(row, 27))
                .uf(getString(row, 28))
                .cargo(getString(row, 29))
                .setor(getString(row, 30))
                .salario(parseBigDecimal(getString(row, 31)))
                .dataAdmissao(parseDate(row, 33))
                .dataDemissao(parseDate(row, 34))
                .horarioEntrada(parseHorario(getString(row, 50)))
                .horarioSaida(parseHorario(getString(row, 51)))
                .motivoContratacao(getString(row, 52))
                .contratante(getString(row, 53)) // 1 - 2
                .build();

        String tipoContratoStr = Optional.ofNullable(getString(row, 32))
                .map(String::trim)
                .orElse("6");
        switch (tipoContratoStr) {
            case "1" -> vagaDto.setTipoContrato(TipoContrato.CLT_CE_CJ);
            case "2" -> vagaDto.setTipoContrato(TipoContrato.CLT_CE_SJ);
            case "4" -> vagaDto.setTipoContrato(TipoContrato.CLT_SE_SJ);
            case "5" -> vagaDto.setTipoContrato(TipoContrato.TEMP_CJ);
            default -> vagaDto.setTipoContrato(TipoContrato.INDEFINIDO);
        }

        CarroDTO carroDto = CarroDTO.builder()
                .marca(null)
                .modelo(null)
                .cor(null)
                .chassi(null)
                .placa(null)
                .anoModelo(null)
                .ddd(null)
                .telefone(null)
                .build();

        CelularDTO celularDto = CelularDTO.builder()
                .marca(null)
                .modelo(null)
                .chip(null)
                .imei(null)
                .build();

        ImportacaoDTO importacaoDto = new ImportacaoDTO();
        importacaoDto.setPessoa(pessoaDto);
        importacaoDto.setVaga(vagaDto);
        importacaoDto.setCarro(carroDto);
        importacaoDto.setCelular(celularDto);

        return importacaoDto;
    }

    private void importarPessoa(ImportacaoDTO importacaoDto) {
        Optional<Pessoa> existente = pessoaRepository.findByCpf(importacaoDto.getPessoa().getCpf());

        if (existente.isPresent()) {
            System.out.println("Pessoa já existe: " + importacaoDto.getPessoa().getNome() + ". Atualizando dados...");
        }

        Pessoa pessoa = importacaoDto.getPessoa().toEntity();

        if (importacaoDto.getVaga() != null) {

            Vaga vaga = importacaoDto.getVaga().toEntity(pessoa);
            pessoa.getVagas().add(vaga);
        }

        if (importacaoDto.getCarro() != null) {
            Carro carro = carroRepository.findByPlaca(importacaoDto.getCarro().getPlaca())
                    .orElseGet(() -> {
                        Carro novoCarro = importacaoDto.getCarro().toEntity();
                        return carroRepository.save(novoCarro);
                    });

            RecursoCarro recursoCarro = new RecursoCarro();
            recursoCarro.setCarro(carro);
            recursoCarro.setPessoa(pessoa);
            recursoCarro.setDataEntrega(null);
            recursoCarro.setDataDevolucao(null);

            pessoa.getRecursos().add(recursoCarro);
        }

        if (importacaoDto.getCelular() != null) {
            Celular celular = celularRepository.findByImei(importacaoDto.getCelular().getImei())
                    .orElseGet(() -> {
                        Celular novoCelular = importacaoDto.getCelular().toEntity();
                        return celularRepository.save(novoCelular);
                    });

            RecursoCelular recursoCelular = new RecursoCelular();
            recursoCelular.setCelular(celular);
            recursoCelular.setPessoa(pessoa);
            recursoCelular.setDataEntrega(null);
            recursoCelular.setDataDevolucao(null);

            pessoa.getRecursos().add(recursoCelular);
        }

        pessoaRepository.save(pessoa);
    }

    private String getString(Row row, int index) {
        Cell cell = row.getCell(index);
        return cell != null ? cell.toString().trim() : null;
    }

    private LocalDate parseDate(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) {
            return null;
        }

        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        } else {
            try {
                String valor = cell.toString().trim();

                // Limpa possíveis pontos extras e troca "dez." por "dez"
                valor = valor.replace(".", "");

                if (valor.isEmpty()) {
                    return null;
                }

                // Tenta parse com formato dd/MM/yyyy
                try {
                    return LocalDate.parse(valor, dtf);
                } catch (DateTimeParseException e) {
                    // Se falhar, tenta com abreviação de mês em português
                    return LocalDate.parse(valor, dtfPortuguese);
                }

            } catch (Exception e) {
                return null;
            }
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isBlank())
            return null;
        try {
            return new BigDecimal(value.replace("R$", "").replace(".", "").replace(",", ".").trim());
        } catch (Exception e) {
            return null;
        }
    }

    public LocalTime parseHorario(String horarioStr) {
        if (horarioStr == null || horarioStr.isBlank()) {
            return null;
        }
        return LocalTime.parse(horarioStr.trim(), timeFormatter);
    }

    public boolean hasPessoas() {
        return pessoaRepository.count() > 0;
    }

}
