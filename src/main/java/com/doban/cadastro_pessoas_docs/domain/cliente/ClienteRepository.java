package com.doban.cadastro_pessoas_docs.domain.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByNome(String nome);

    List<Cliente> findByAtivoTrue();

    boolean existsByNome(String nome);
}
