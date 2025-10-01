package com.doban.cadastro_pessoas_docs.celular;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CelularRepository extends JpaRepository<Celular, Long>{

    Optional<Celular> findByImei(String imei);

}
