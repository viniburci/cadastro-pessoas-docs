package com.doban.cadastro_pessoas_docs.recurso.dinamico;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecursoDinamicoRepository extends JpaRepository<RecursoDinamico, Long> {

    List<RecursoDinamico> findByPessoaId(Long pessoaId);

    List<RecursoDinamico> findByItemId(Long itemId);

    @Query("SELECT r FROM RecursoDinamico r WHERE r.pessoa.id = :pessoaId AND r.dataDevolucao IS NULL")
    List<RecursoDinamico> findAtivosParaPessoa(@Param("pessoaId") Long pessoaId);

    @Query("SELECT r FROM RecursoDinamico r WHERE r.item.id = :itemId AND r.dataDevolucao IS NULL")
    Optional<RecursoDinamico> findAtivoParaItem(@Param("itemId") Long itemId);

    @Query("SELECT r FROM RecursoDinamico r WHERE r.item.tipoRecurso.codigo = :tipoRecursoCodigo")
    List<RecursoDinamico> findByTipoRecurso(@Param("tipoRecursoCodigo") String tipoRecursoCodigo);

    boolean existsByItemIdAndDataDevolucaoIsNull(Long itemId);
}
