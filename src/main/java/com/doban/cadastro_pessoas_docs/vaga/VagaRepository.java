package com.doban.cadastro_pessoas_docs.vaga;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long>{

    Optional<List<Vaga>> findByPessoaId(Long pessoaId);

    @Query("SELECT v FROM Vaga v WHERE v.pessoa.id = :pessoaId ORDER BY v.dataAdmissao DESC")
    Optional<Vaga> findTopByPessoaIdOrderByDataAdmissaoDesc(Long pessoaId);

    Optional<Vaga> findFirstByPessoaIdOrderByDataAdmissaoDesc(Long pessoaId);    

}
