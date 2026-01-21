package com.doban.cadastro_pessoas_docs.documentos.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TemplateDocumentoRepository extends JpaRepository<TemplateDocumento, Long> {

    Optional<TemplateDocumento> findByCodigo(String codigo);

    List<TemplateDocumento> findByAtivoTrue();

    boolean existsByCodigo(String codigo);

    @Query("SELECT t FROM TemplateDocumento t WHERE t.ativo = true AND " +
           "(t.tiposVagaPermitidos IS EMPTY OR :tipoVagaId IN (SELECT tv.id FROM t.tiposVagaPermitidos tv))")
    List<TemplateDocumento> findAtivosByTipoVaga(@Param("tipoVagaId") Long tipoVagaId);

    @Query("SELECT t FROM TemplateDocumento t LEFT JOIN FETCH t.tiposVagaPermitidos WHERE t.id = :id")
    Optional<TemplateDocumento> findByIdWithTiposVaga(@Param("id") Long id);

    @Query("SELECT t FROM TemplateDocumento t LEFT JOIN FETCH t.tiposVagaPermitidos WHERE t.codigo = :codigo")
    Optional<TemplateDocumento> findByCodigoWithTiposVaga(@Param("codigo") String codigo);
}
