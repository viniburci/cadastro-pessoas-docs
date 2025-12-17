package com.doban.cadastro_pessoas_docs.domain.pessoa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;


@Repository
public interface PessoaRepository extends JpaRepository<Pessoa, Long>{
	
	Optional<Pessoa> findByCpf(String cpf);

	boolean existsById(@NonNull Long id);

}
