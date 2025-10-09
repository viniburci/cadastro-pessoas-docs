package com.doban.cadastro_pessoas_docs.vaga;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.doban.cadastro_pessoas_docs.pessoa.Pessoa;
import com.doban.cadastro_pessoas_docs.pessoa.PessoaService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VagaService {

    private final VagaRepository vagaRepository;
    private final PessoaService pessoaService;

    public List<VagaDTO> obterVagasPorPessoa(Long pessoaId) {
        List<Vaga> vagas = vagaRepository.findByPessoaId(pessoaId).orElse(Collections.emptyList());
        return vagas.stream().map(vaga -> {
            VagaDTO vagaDTO = new VagaDTO(vaga);
            return vagaDTO;
        }).toList(); 
    }

    public VagaDTO obterVagaMaisRecentePorPessoa(Long pessoaId) {
        Vaga vaga = vagaRepository.findFirstByPessoaIdOrderByDataAdmissaoDesc(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Essa pessoa não tem vagas cadastradas."));
        VagaDTO vagaDTO = new VagaDTO(vaga);
        return vagaDTO;
    }

    public VagaDTO criarVaga(Vaga vaga, Long pessoaId) {
        Pessoa pessoa = pessoaService.buscarEntidadePessoaPorId(pessoaId);
        vaga.setPessoa(pessoa);
        VagaDTO vagaDTO = new VagaDTO(vagaRepository.save(vaga));
        return vagaDTO;
    }

    public VagaDTO atualizarVaga(Long vagaId, Vaga vagaAtualizada) {
        Vaga vagaExistente = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));

        vagaExistente.setCliente(vagaAtualizada.getCliente());
        vagaExistente.setCidade(vagaAtualizada.getCidade());
        vagaExistente.setUf(vagaAtualizada.getUf());
        vagaExistente.setCargo(vagaAtualizada.getCargo());
        vagaExistente.setSetor(vagaAtualizada.getSetor());
        vagaExistente.setSalario(vagaAtualizada.getSalario());
        vagaExistente.setTipoContrato(vagaAtualizada.getTipoContrato());
        vagaExistente.setDataAdmissao(vagaAtualizada.getDataAdmissao());
        vagaExistente.setDataDemissao(vagaAtualizada.getDataDemissao());
        vagaExistente.setAcrescimoOuSubstituicao(vagaAtualizada.getAcrescimoOuSubstituicao());
        vagaExistente.setAso(vagaAtualizada.getAso());
        vagaExistente.setOptanteVT(vagaAtualizada.getOptanteVT());
        vagaExistente.setHorarioEntrada(vagaAtualizada.getHorarioEntrada());
        vagaExistente.setHorarioSaida(vagaAtualizada.getHorarioSaida());
    
        return new VagaDTO(vagaRepository.save(vagaExistente));
    }

    public void deletarVaga(Long vagaId) {
        if (!vagaRepository.existsById(vagaId)) {
            throw new EntityNotFoundException("Vaga não encontrada");
        }
        vagaRepository.deleteById(vagaId);
    }
}
