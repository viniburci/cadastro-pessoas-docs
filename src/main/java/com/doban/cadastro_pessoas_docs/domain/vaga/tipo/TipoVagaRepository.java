package com.doban.cadastro_pessoas_docs.domain.vaga.tipo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoVagaRepository extends JpaRepository<TipoVaga, Long> {

    Optional<TipoVaga> findByCodigo(String codigo);

    Optional<TipoVaga> findByNome(String nome);

    List<TipoVaga> findByAtivoTrueOrderByIdAsc();

    boolean existsByCodigo(String codigo);
}
