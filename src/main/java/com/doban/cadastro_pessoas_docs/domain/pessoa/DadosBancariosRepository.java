package com.doban.cadastro_pessoas_docs.domain.pessoa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository para acesso aos dados bancários das pessoas.
 */
@Repository
public interface DadosBancariosRepository extends JpaRepository<DadosBancarios, Long> {

    /**
     * Busca dados bancários por ID da pessoa.
     *
     * @param pessoaId ID da pessoa
     * @return Optional com dados bancários se encontrados
     */
    Optional<DadosBancarios> findByPessoaId(Long pessoaId);

    /**
     * Verifica se existem dados bancários cadastrados para uma pessoa.
     *
     * @param pessoaId ID da pessoa
     * @return true se existem dados bancários
     */
    boolean existsByPessoaId(Long pessoaId);

    /**
     * Deleta dados bancários de uma pessoa.
     *
     * @param pessoaId ID da pessoa
     */
    void deleteByPessoaId(Long pessoaId);
}
