package com.doban.cadastro_pessoas_docs.recurso.recurso_carro;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecursoCarroRepository extends JpaRepository<RecursoCarro, Long> {
    List<RecursoCarro> findByPessoaId(Long pessoaId);
}
