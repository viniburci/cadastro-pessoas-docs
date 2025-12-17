package com.doban.cadastro_pessoas_docs.domain.carro;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarroRepository extends JpaRepository<Carro, Long>{

    Optional<Carro> findByPlaca(String placa);

}
