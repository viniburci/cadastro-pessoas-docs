package com.doban.cadastro_pessoas_docs.pessoa;

import com.doban.cadastro_pessoas_docs.carro.Carro;
import com.doban.cadastro_pessoas_docs.celular.Celular;
import com.doban.cadastro_pessoas_docs.vaga.Vaga;

public class PessoaMapper {

    public static PessoaExcelDTO toDTO(Pessoa pessoa) {
        PessoaExcelDTO dto = new PessoaExcelDTO();

        dto.setNome(pessoa.getNome());
        dto.setEndereco(pessoa.getEndereco());
        dto.setBairro(pessoa.getBairro());
        dto.setCidade(pessoa.getCidade());
        dto.setUf(pessoa.getUf());
        dto.setCep(pessoa.getCep());
        dto.setFone(pessoa.getFone());
        dto.setCpf(pessoa.getCpf());
        dto.setNumeroRg(pessoa.getNumeroRg());
        dto.setDataNascimento(pessoa.getDataNascimento());
        dto.setLocalNascimento(pessoa.getLocalNascimento());
        dto.setMae(pessoa.getMae());
        dto.setPai(pessoa.getPai());
        dto.setNumeroCtps(pessoa.getNumeroCtps());
        dto.setSerieCtps(pessoa.getSerieCtps());
        dto.setDataEmissaoCtps(pessoa.getDataEmissaoCtps());
        dto.setPis(pessoa.getPis());
        dto.setDataPis(pessoa.getDataPis());
        dto.setTituloEleitor(pessoa.getTituloEleitor());

        if (!pessoa.getVagas().isEmpty()) {
            Vaga vaga = pessoa.getVagas().get(0);
            dto.setCliente(vaga.getCliente());
            dto.setCidadeVaga(vaga.getCidade());
            dto.setUfVaga(vaga.getUf());
            dto.setCargo(vaga.getCargo());
            dto.setSetor(vaga.getSetor());
            dto.setSalario(vaga.getSalario());
            dto.setTipoContrato(vaga.getTipo());
            dto.setDataAdmissao(vaga.getDataAdmissao());
            dto.setDataDemissao(vaga.getDataDemissao());
            dto.setRegime(vaga.getRegime());
            dto.setHorarioEntrada(vaga.getHorarioEntrada());
            dto.setHorarioSaida(vaga.getHorarioSaida());
            dto.setMotivoContratacao(vaga.getMotivoContratacao());
            dto.setContratante(vaga.getContratante());
            dto.setMatricula(vaga.getMatricula());

            if (vaga.getCarro() != null) {
                Carro carro = vaga.getCarro();
                dto.setCarroMarca(carro.getMarca());
                dto.setCarroCor(carro.getCor());
                dto.setCarroChassi(carro.getChassi());
                dto.setCarroPlaca(carro.getPlaca());
                dto.setCarroModelo(carro.getModelo());
                dto.setCarroDdd(carro.getDdd());
                dto.setCarroTelefone(carro.getTelefone());
                dto.setCarroAnoModelo(carro.getAnoModelo());
            }
        }

        if (pessoa.getCelular() != null) {
            Celular celular = pessoa.getCelular();
            dto.setCelularMarca(celular.getMarca());
            dto.setCelularModelo(celular.getModelo());
            dto.setCelularChip(celular.getChip());
            dto.setCelularImei(celular.getImei());
        }

        return dto;
    }

    public static Pessoa toEntity(PessoaExcelDTO dto) {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(dto.getNome());
        pessoa.setEndereco(dto.getEndereco());
        pessoa.setBairro(dto.getBairro());
        pessoa.setCidade(dto.getCidade());
        pessoa.setUf(dto.getUf());
        pessoa.setCep(dto.getCep());
        pessoa.setFone(dto.getFone());
        pessoa.setCpf(dto.getCpf());
        pessoa.setNumeroRg(dto.getNumeroRg());
        pessoa.setDataNascimento(dto.getDataNascimento());
        pessoa.setLocalNascimento(dto.getLocalNascimento());
        pessoa.setMae(dto.getMae());
        pessoa.setPai(dto.getPai());
        pessoa.setNumeroCtps(dto.getNumeroCtps());
        pessoa.setSerieCtps(dto.getSerieCtps());
        pessoa.setDataEmissaoCtps(dto.getDataEmissaoCtps());
        pessoa.setPis(dto.getPis());
        pessoa.setDataPis(dto.getDataPis());
        pessoa.setTituloEleitor(dto.getTituloEleitor());

        // Vaga
        Vaga vaga = new Vaga();
        vaga.setCliente(dto.getCliente());
        vaga.setCidade(dto.getCidadeVaga());
        vaga.setUf(dto.getUfVaga());
        vaga.setCargo(dto.getCargo());
        vaga.setSetor(dto.getSetor());
        vaga.setSalario(dto.getSalario());
        vaga.setTipo(dto.getTipoContrato());
        vaga.setDataAdmissao(dto.getDataAdmissao());
        vaga.setDataDemissao(dto.getDataDemissao());
        vaga.setRegime(dto.getRegime());
        vaga.setHorarioEntrada(dto.getHorarioEntrada());
        vaga.setHorarioSaida(dto.getHorarioSaida());
        vaga.setMotivoContratacao(dto.getMotivoContratacao());
        vaga.setContratante(dto.getContratante());
        vaga.setMatricula(dto.getMatricula());
        vaga.setPessoa(pessoa);
        pessoa.getVagas().add(vaga);

        // Carro
        if (dto.getCarroMarca() != null || dto.getCarroPlaca() != null) {
            Carro carro = new Carro();
            carro.setMarca(dto.getCarroMarca());
            carro.setCor(dto.getCarroCor());
            carro.setChassi(dto.getCarroChassi());
            carro.setPlaca(dto.getCarroPlaca());
            carro.setModelo(dto.getCarroModelo());
            carro.setDdd(dto.getCarroDdd());
            carro.setTelefone(dto.getCarroTelefone());
            carro.setAnoModelo(dto.getCarroAnoModelo());
            carro.getVagas().add(vaga);
            vaga.setCarro(carro);
        }

        // Celular
        if (dto.getCelularMarca() != null || dto.getCelularImei() != null) {
            Celular celular = new Celular();
            celular.setMarca(dto.getCelularMarca());
            celular.setModelo(dto.getCelularModelo());
            celular.setChip(dto.getCelularChip());
            celular.setImei(dto.getCelularImei());
            celular.setPessoa(pessoa);
            pessoa.setCelular(celular);
        }

        return pessoa;
    }
}
