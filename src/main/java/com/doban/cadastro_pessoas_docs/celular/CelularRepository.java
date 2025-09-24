package com.doban.cadastro_pessoas_docs.celular;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CelularRepository extends JpaRepository<Celular, Long>{

}
