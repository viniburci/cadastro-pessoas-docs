package com.doban.cadastro_pessoas_docs.domain.pessoa;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service para gerenciamento de dados bancários.
 */
@Service
@RequiredArgsConstructor
public class DadosBancariosService {

    private final DadosBancariosRepository dadosBancariosRepository;
    private final PessoaRepository pessoaRepository;

    /**
     * Busca dados bancários por ID da pessoa.
     *
     * @param pessoaId ID da pessoa
     * @return DTO com dados bancários
     * @throws EntityNotFoundException se não encontrar dados bancários
     */
    @Transactional(readOnly = true)
    public DadosBancariosDTO buscarPorPessoaId(Long pessoaId) {
        DadosBancarios dadosBancarios = dadosBancariosRepository.findByPessoaId(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Dados bancários não encontrados para pessoa ID: " + pessoaId));
        return new DadosBancariosDTO(dadosBancarios);
    }

    /**
     * Cria ou atualiza dados bancários de uma pessoa.
     *
     * @param pessoaId ID da pessoa
     * @param dto DTO com dados bancários
     * @return DTO atualizado
     */
    @Transactional
    public DadosBancariosDTO salvar(Long pessoaId, DadosBancariosDTO dto) {
        Pessoa pessoa = pessoaRepository.findById(pessoaId)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com ID: " + pessoaId));

        DadosBancarios dadosBancarios = dadosBancariosRepository.findByPessoaId(pessoaId)
                .orElse(new DadosBancarios());

        dadosBancarios.setPessoa(pessoa);
        dadosBancarios.setBanco(dto.getBanco());
        dadosBancarios.setAgencia(dto.getAgencia());
        dadosBancarios.setConta(dto.getConta());
        dadosBancarios.setTipoConta(dto.getTipoConta());

        DadosBancarios saved = dadosBancariosRepository.save(dadosBancarios);
        return new DadosBancariosDTO(saved);
    }

    /**
     * Deleta dados bancários de uma pessoa.
     *
     * @param pessoaId ID da pessoa
     */
    @Transactional
    public void deletar(Long pessoaId) {
        if (!dadosBancariosRepository.existsByPessoaId(pessoaId)) {
            throw new EntityNotFoundException(
                    "Dados bancários não encontrados para pessoa ID: " + pessoaId);
        }
        dadosBancariosRepository.deleteByPessoaId(pessoaId);
    }

    /**
     * Verifica se existem dados bancários para uma pessoa.
     *
     * @param pessoaId ID da pessoa
     * @return true se existem dados bancários
     */
    @Transactional(readOnly = true)
    public boolean existePorPessoaId(Long pessoaId) {
        return dadosBancariosRepository.existsByPessoaId(pessoaId);
    }
}
