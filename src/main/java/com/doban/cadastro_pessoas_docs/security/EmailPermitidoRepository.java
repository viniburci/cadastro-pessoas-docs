package com.doban.cadastro_pessoas_docs.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmailPermitidoRepository extends JpaRepository<EmailPermitido, Long> {
    Optional<EmailPermitido> findByEmail(String email);
    boolean existsByEmail(String email);
}
