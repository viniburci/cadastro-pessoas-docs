package com.doban.cadastro_pessoas_docs;

import java.io.FileInputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import com.doban.cadastro_pessoas_docs.carro.Carro;
import com.doban.cadastro_pessoas_docs.celular.Celular;
import com.doban.cadastro_pessoas_docs.pessoa.Pessoa;
import com.doban.cadastro_pessoas_docs.pessoa.PessoaRepository;
import com.doban.cadastro_pessoas_docs.vaga.Regime;
import com.doban.cadastro_pessoas_docs.vaga.Vaga;

import jakarta.transaction.Transactional;

@Service
public class ExcelImportService {

    private final PessoaRepository pessoaRepository;

    public ExcelImportService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
    
    @Transactional
    public void importarExcel(String caminhoArquivo) throws Exception {
        List<Pessoa> pessoas = new ArrayList<>();

        try (FileInputStream fis = new FileInputStream(caminhoArquivo);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(4);

            // Pular cabeçalho (linha 0)
            for (int i = 12; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Pessoa pessoa = new Pessoa();

                // Colunas fixas (baseado no layout que você mandou)
                pessoa.setNome(getString(row, 3));
                pessoa.setEndereco(getString(row, 3));
                pessoa.setBairro(getString(row, 4));
                pessoa.setCidade(getString(row, 5));
                pessoa.setUf(getString(row, 6));
                pessoa.setCep(getString(row, 7));
                pessoa.setFone(getString(row, 9));
                pessoa.setCpf(getString(row, 17));
                pessoa.setNumeroRg(getString(row, 13));
                pessoa.setDataNascimento(parseDate(getString(row, 20)));
                pessoa.setLocalNascimento(getString(row, 21));
                pessoa.setMae(getString(row, 22));
                pessoa.setPai(getString(row, 23));

                // CTPS
                pessoa.setNumeroCtps(getString(row, 11));
                pessoa.setSerieCtps(getString(row, 12));
                pessoa.setDataEmissaoCtps(parseDate(getString(row, 13)));

                // PIS
                pessoa.setPis(getString(row, 18));
                pessoa.setDataPis(parseDate(getString(row, 19)));

                // Título Eleitor
                pessoa.setTituloEleitor(getString(row, 20));

                // Criar Vaga associada
                Vaga vaga = new Vaga();
                vaga.setCliente(getString(row, 25));
                vaga.setCidade(getString(row, 26));
                vaga.setUf(getString(row, 27));
                vaga.setCargo(getString(row, 28));
                vaga.setSetor(getString(row, 29));
                vaga.setSalario(parseSalario(getString(row, 30)));
                vaga.setTipo(getString(row, 31));
                vaga.setDataAdmissao(parseDate(getString(row, 32)));
                vaga.setDataDemissao(parseDate(getString(row, 33)));
                vaga.setRegime(getString(row, 35).equalsIgnoreCase("X") ? Regime.TEMPORARIO : Regime.CLT);
                
                String horario = getString(row, 55);
                String[] horarios = horario.split("-");
                vaga.setHorarioEntrada(LocalTime.parse(horarios[0].trim(), formatter));
                vaga.setHorarioSaida(LocalTime.parse(horarios[1].trim(), formatter));
                
                vaga.setMotivoContratacao(getString(row, 57));
                vaga.setContratante(getString(row, 58));
                vaga.setMatricula(getString(row, 66));

                vaga.setPessoa(pessoa);
                pessoa.getVagas().add(vaga);

                // Criar Carro (se houver dados)
                Carro carro = new Carro();
                carro.setMarca(getString(row, 59));
                carro.setCor(getString(row, 60));
                carro.setChassi(getString(row, 61));
                carro.setPlaca(getString(row, 62));
                carro.setModelo(getString(row, 63));
                carro.setDdd(getString(row, 64));
                carro.setTelefone(getString(row, 65));
                carro.setAnoModelo(getString(row, 66));
                carro.getVagas().add(vaga);
                vaga.setCarro(carro);

                // Criar Celular
                Celular celular = new Celular();
                celular.setMarca(getString(row, 67));
                celular.setModelo(getString(row, 68));
                celular.setChip(getString(row, 69));
                celular.setImei(getString(row, 70));
                celular.setPessoa(pessoa);
                pessoa.setCelular(celular);

                pessoas.add(pessoa);
            }
        }

        // Salvar todas no banco
        pessoaRepository.saveAll(pessoas);
    }

    // Métodos auxiliares
    private String getString(Row row, int index) {
        if (row.getCell(index) == null) return "";
        return row.getCell(index).toString().trim();
    }

    private LocalDate parseDate(String value) {
        try {
            if (value == null || value.isBlank()) return null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy"); // ajuste ao formato do Excel
            return LocalDate.parse(value, formatter);
        } catch (Exception e) {
            return null;
        }
    }

    private java.math.BigDecimal parseSalario(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return new java.math.BigDecimal(value.replace("R$", "").replace(",", ".").trim());
        } catch (Exception e) {
            return null;
        }
    }
}