package com.doban.cadastro_pessoas_docs.recurso.recurso_rocadeira;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecursoRocadeiraRepository extends JpaRepository<RecursoRocadeira, Long> {
    List<RecursoRocadeira> findByPessoaId(Long pessoaId);
}
