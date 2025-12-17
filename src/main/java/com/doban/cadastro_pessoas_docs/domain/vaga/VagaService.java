package com.doban.cadastro_pessoas_docs.domain.vaga;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.doban.cadastro_pessoas_docs.domain.pessoa.Pessoa;
import com.doban.cadastro_pessoas_docs.domain.pessoa.PessoaService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
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

    @Transactional
    public VagaDTO obterVagaPorId(Long vagaId) {
        Vaga vaga = vagaRepository.findById(vagaId)
            .orElseThrow(() -> new EntityNotFoundException("Não existe vaga com id " + vagaId + '.'));
        VagaDTO vagaDTO = new VagaDTO(vaga);
        return vagaDTO;
    }

    public VagaDTO criarVaga(Long pessoaId, VagaDTO vagaDTO) {
        if (vagaDTO == null) {
            throw new IllegalArgumentException("Dados da vaga não podem ser nulos.");
        }

        if (pessoaId == null) {
            throw new IllegalArgumentException("ID da pessoa não pode ser nulo.");
        }

        Pessoa pessoa = pessoaService.buscarEntidadePessoaPorId(pessoaId);
        if (pessoa == null) {
            throw new EntityNotFoundException("Pessoa com ID " + pessoaId + " não encontrada.");
        }

        Vaga vaga = vagaDTO.toEntity(pessoa);

        Vaga vagaSalva;
        try {
            vagaSalva = vagaRepository.save(vaga);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityViolationException(
                    "Erro ao salvar a vaga: dados inválidos ou violação de integridade.");
        }

        return new VagaDTO(vagaSalva);
    }

    public VagaDTO atualizarVaga(Long vagaId, VagaDTO vagaAtualizada) {
        Vaga vagaExistente = vagaRepository.findById(vagaId)
                .orElseThrow(() -> new EntityNotFoundException("Vaga não encontrada"));

        vagaAtualizada.atualizarEntidade(vagaExistente);
        return new VagaDTO(vagaRepository.save(vagaExistente));
    }

    public void deletarVaga(Long vagaId) {
        if (!vagaRepository.existsById(vagaId)) {
            throw new EntityNotFoundException("Vaga não encontrada");
        }
        vagaRepository.deleteById(vagaId);
    }
}
