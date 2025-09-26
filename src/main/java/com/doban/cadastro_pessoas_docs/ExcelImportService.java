package com.doban.cadastro_pessoas_docs;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
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
import com.doban.cadastro_pessoas_docs.celular.Celular;
import com.doban.cadastro_pessoas_docs.pessoa.Pessoa;
import com.doban.cadastro_pessoas_docs.pessoa.PessoaExcelDTO;
import com.doban.cadastro_pessoas_docs.pessoa.PessoaRepository;
import com.doban.cadastro_pessoas_docs.recurso.Recurso;
import com.doban.cadastro_pessoas_docs.vaga.TipoContrato;
import com.doban.cadastro_pessoas_docs.vaga.Vaga;

import jakarta.transaction.Transactional;

@Service
public class ExcelImportService {

    private final PessoaRepository pessoaRepository;

    private final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public ExcelImportService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional
    public void importar(String caminhoArquivo) throws IOException {
        try (FileInputStream fis = new FileInputStream(caminhoArquivo);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(4);
            Set<String> cpfsImportados = new HashSet<>();

            for (int i = 12; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                PessoaExcelDTO dto = lerLinhaDTO(row);

                if (dto.getCpf() == null || cpfsImportados.contains(dto.getCpf())) {
                    System.out.println("Ignorando duplicado: " + dto.getNome());
                    continue;
                }

                importarPessoa(dto);
                cpfsImportados.add(dto.getCpf());
            }
        }
    }

    private PessoaExcelDTO lerLinhaDTO(Row row) {
        PessoaExcelDTO dto = new PessoaExcelDTO();

        // ===== Pessoa =====

//0  -> id + nome  
//1  -> id  
//2  -> nome  
//3  -> endereço  
//4  -> bairro  
//5  -> cidade  
//6  -> estado  
//7  -> cep  
//8  -> ddd  
//9  -> telefone  
//10 -> email  
//11 -> numero ctps  
//12 -> serie ctps  
//13 -> data emissao ctps  
//14 -> rg  
//15 -> data emissao rg  
//16 -> uf  
//17 -> cpf  
//18 -> pis  
//19 -> data emissao pis  
//20 -> titulo eleitor  
//21 -> data nascimento  
//22 -> local nascimento  
//23 -> nome mae  
//24 -> nome pai  
//25 -> estado civil  

        dto.setNome(getString(row, 2));
        dto.setEndereco(getString(row, 3));
        dto.setBairro(getString(row, 4));
        dto.setCidade(getString(row, 5));
        dto.setUf(getString(row, 6));
        dto.setCep(getString(row, 7));
        dto.setFone(getString(row, 9));
        dto.setCpf(getString(row, 17));
        dto.setNumeroRg(getString(row, 13));
        dto.setDataNascimento(parseDate(row, 20));
        dto.setLocalNascimento(getString(row, 21));
        dto.setMae(getString(row, 22));
        dto.setPai(getString(row, 23));
        dto.setNumeroCtps(getString(row, 11));
        dto.setSerieCtps(getString(row, 12));
        dto.setDataEmissaoCtps(parseDate(row, 13));
        dto.setPis(getString(row, 18));
        dto.setDataPis(parseDate(row, 19));
        dto.setTituloEleitor(getString(row, 20));


        // ===== Vaga =====
//26 -> cliente  
//27 -> cidade (cliente)  
//28 -> estado (cliente)  
//29 -> cargo  
//30 -> setor  
//31 -> salario  
//32 -> tipo contrato  
//33 -> data admissao  
//34 -> data demissao  
//35 -> clt  
//36 -> temp  
//37 -> acresc  
//38 -> subst  
//39 -> admissional  
//40 -> demissional  
//41 -> retorno  
//42 -> post  
//43 -> ruido  
//44 -> outro  
//45 -> numero pep  
//46 -> numero lotação  
//47 -> sim (vale transporte)  
//48 -> nao (vale transporte)  
//49 -> nada (vale transporte)  
//50 -> horario entrada  
//51 -> horario saída  
//52 -> motivo contratação  
//53 -> contratante (1, 2)  

//54 -> marca (carro)  
//55 -> cor  
//56 -> chassi  
//57 -> placa  
//58 -> modelo (carro)  
//59 -> ddd (carro)  
//60 -> telefone (carro)  
//61 -> ano/modelo  

//62 -> marca (celular)  
//63 -> modelo (celular)  
//64 -> chip  
//65 -> imei  

//66 -> categoria cnh  
//67 -> validade cnh  
//68 -> matrícula  



        dto.setCliente(getString(row, 25));
        dto.setCidadeVaga(getString(row, 26));
        dto.setUfVaga(getString(row, 27));
        dto.setCargo(getString(row, 28));
        dto.setSetor(getString(row, 29));
        dto.setSalario(parseBigDecimal(getString(row, 30)));
        dto.setDataAdmissao(parseDate(row, 32));
        dto.setDataDemissao(parseDate(row, 33));


        String tipoContratoStr = Optional.ofNullable(getString(row, 35))
        		.map(String::trim)
        		.orElse("6");
        switch (tipoContratoStr) {
            case "1" -> dto.setTipoContrato(TipoContrato.CLT_CE_CJ);
            case "2" -> dto.setTipoContrato(TipoContrato.CLT_CE_SJ);
            case "4" -> dto.setTipoContrato(TipoContrato.CLT_SE_SJ);
            case "5" -> dto.setTipoContrato(TipoContrato.TEMP_CJ);
            default  -> dto.setTipoContrato(TipoContrato.INDEFINIDO);
        }

        // Horários
        String horario = getString(row, 55);
        if (horario != null && horario.contains("-")) {
            String[] partes = horario.split("-");
            dto.setHorarioEntrada(LocalTime.parse(partes[0].trim(), timeFormatter));
            dto.setHorarioSaida(LocalTime.parse(partes[1].trim(), timeFormatter));
        }

        dto.setMotivoContratacao(getString(row, 57));
        dto.setContratante(getString(row, 58));
        dto.setMatricula(getString(row, 66));

        // ===== Carro =====
        dto.setCarroMarca(getString(row, 59));
        dto.setCarroCor(getString(row, 60));
        dto.setCarroChassi(getString(row, 61));
        dto.setCarroPlaca(getString(row, 62));
        dto.setCarroModelo(getString(row, 63));
        dto.setCarroDdd(getString(row, 64));
        dto.setCarroTelefone(getString(row, 65));
        dto.setCarroAnoModelo(getString(row, 66));

        // ===== Celular =====
        dto.setCelularMarca(getString(row, 67));
        dto.setCelularModelo(getString(row, 68));
        dto.setCelularChip(getString(row, 69));
        dto.setCelularImei(getString(row, 70));

        return dto;
    }

    private void importarPessoa(PessoaExcelDTO dto) {
        Optional<Pessoa> existente = pessoaRepository.findByCpf(dto.getCpf());

        if (existente.isPresent()) {
            System.out.println("Pessoa já existe: " + dto.getNome());
            // Aqui você poderia atualizar dados da pessoa ou criar nova Vaga/Recurso
        } else {
            // Criar Pessoa
            Pessoa pessoa = dto.toEntity();

            // Criar Vaga
            Vaga vaga = Vaga.builder()
                    .pessoa(pessoa)
                    .contratante(dto.getContratante())
                    .cliente(dto.getCliente())
                    .setor(dto.getSetor())
                    .cargo(dto.getCargo())
                    .cidade(dto.getCidadeVaga())
                    .uf(dto.getUfVaga())
                    .salario(dto.getSalario())
                    .dataAdmissao(dto.getDataAdmissao())
                    .dataDemissao(dto.getDataDemissao())
                    .tipoContrato(dto.getTipoContrato())
                    .horarioEntrada(dto.getHorarioEntrada())
                    .horarioSaida(dto.getHorarioSaida())
                    .motivoContratacao(dto.getMotivoContratacao())
                    .matricula(dto.getMatricula())
                    .build();
            pessoa.getVagas().add(vaga);

            // Criar Carro e Recurso se houver dados
            if (dto.getCarroMarca() != null || dto.getCarroModelo() != null) {
                Carro carro = Carro.builder()
                        .pessoa(pessoa)
                        .marca(dto.getCarroMarca())
                        .modelo(dto.getCarroModelo())
                        .cor(dto.getCarroCor())
                        .chassi(dto.getCarroChassi())
                        .placa(dto.getCarroPlaca())
                        .anoModelo(dto.getCarroAnoModelo())
                        .ddd(dto.getCarroDdd())
                        .telefone(dto.getCarroTelefone())
                        .build();

                Recurso recursoCarro = Recurso.builder()
                        .vaga(vaga)
                        .carro(carro)
                        .build();

                vaga.getRecursos().add(recursoCarro);
                pessoa.getCarros().add(carro);
            }

            // Criar Celular e Recurso se houver dados
            if (dto.getCelularMarca() != null || dto.getCelularModelo() != null) {
                Celular celular = Celular.builder()
                        .pessoa(pessoa)
                        .marca(dto.getCelularMarca())
                        .modelo(dto.getCelularModelo())
                        .chip(dto.getCelularChip())
                        .imei(dto.getCelularImei())
                        .build();

                Recurso recursoCelular = Recurso.builder()
                        .vaga(vaga)
                        .celular(celular)
                        .build();

                vaga.getRecursos().add(recursoCelular);
                pessoa.getCelulares().add(celular);
            }

            // Salvar no banco
            pessoaRepository.save(pessoa);
        }
    }

    private String getString(Row row, int index) {
        Cell cell = row.getCell(index);
        return cell != null ? cell.toString().trim() : null;
    }

    private LocalDate parseDate(Row row, int index) {
        Cell cell = row.getCell(index);
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(cell)) {
            return cell.getLocalDateTimeCellValue().toLocalDate();
        } else {
            try {
                String valor = cell.toString().trim();
                if (valor.isEmpty()) return null;
                return LocalDate.parse(valor, dtf);
            } catch (Exception e) {
                return null;
            }
        }
    }

    private BigDecimal parseBigDecimal(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return new BigDecimal(value.replace("R$", "").replace(".", "").replace(",", ".").trim());
        } catch (Exception e) {
            return null;
        }
    }
    
    public boolean hasPessoas() {
    	return pessoaRepository.count() > 0;
    }

}
