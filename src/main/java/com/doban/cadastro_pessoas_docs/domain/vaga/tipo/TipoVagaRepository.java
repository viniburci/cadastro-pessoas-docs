package com.doban.cadastro_pessoas_docs.domain.vaga.tipo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TipoVagaRepository extends JpaRepository<TipoVaga, Long> {

    Optional<TipoVaga> findByCodigo(String codigo);

    List<TipoVaga> findByAtivoTrue();

    boolean existsByCodigo(String codigo);

    @Query("SELECT tv FROM TipoVaga tv LEFT JOIN FETCH tv.recursosPermitidos WHERE tv.id = :id")
    Optional<TipoVaga> findByIdWithRecursos(@Param("id") Long id);

    @Query("SELECT tv FROM TipoVaga tv LEFT JOIN FETCH tv.recursosPermitidos WHERE tv.codigo = :codigo")
    Optional<TipoVaga> findByCodigoWithRecursos(@Param("codigo") String codigo);
}
