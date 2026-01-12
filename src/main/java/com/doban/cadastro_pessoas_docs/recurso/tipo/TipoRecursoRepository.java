package com.doban.cadastro_pessoas_docs.recurso.tipo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoRecursoRepository extends JpaRepository<TipoRecurso, Long> {

    Optional<TipoRecurso> findByCodigo(String codigo);

    List<TipoRecurso> findByAtivoTrue();

    boolean existsByCodigo(String codigo);
}
