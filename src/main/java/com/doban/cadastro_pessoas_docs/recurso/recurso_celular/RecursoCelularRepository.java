package com.doban.cadastro_pessoas_docs.recurso.recurso_celular;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecursoCelularRepository extends JpaRepository<RecursoCelular, Long> {
    List<RecursoCelular> findByPessoaId(Long pessoaId);
}
