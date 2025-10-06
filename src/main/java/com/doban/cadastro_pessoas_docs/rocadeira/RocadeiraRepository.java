package com.doban.cadastro_pessoas_docs.rocadeira;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RocadeiraRepository extends JpaRepository<Rocadeira, Long> {
    
}
