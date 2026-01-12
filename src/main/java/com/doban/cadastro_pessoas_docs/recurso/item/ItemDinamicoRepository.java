package com.doban.cadastro_pessoas_docs.recurso.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemDinamicoRepository extends JpaRepository<ItemDinamico, Long> {

    List<ItemDinamico> findByTipoRecursoId(Long tipoRecursoId);

    List<ItemDinamico> findByTipoRecursoCodigo(String codigo);

    List<ItemDinamico> findByTipoRecursoCodigoAndAtivoTrue(String codigo);

    Optional<ItemDinamico> findByTipoRecursoIdAndIdentificador(Long tipoRecursoId, String identificador);

    boolean existsByTipoRecursoIdAndIdentificador(Long tipoRecursoId, String identificador);

    @Query("SELECT i FROM ItemDinamico i WHERE i.tipoRecurso.codigo = :codigo AND i.ativo = true " +
           "AND NOT EXISTS (SELECT r FROM RecursoDinamico r WHERE r.item = i AND r.dataDevolucao IS NULL)")
    List<ItemDinamico> findDisponiveis(@Param("codigo") String tipoRecursoCodigo);

    List<ItemDinamico> findByAtivoTrue();
}
