package com.doban.cadastro_pessoas_docs.recurso.recurso_carro;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecursoCarroRequestDTO {
    private Long carroId;
    private Long pessoaId;
    private LocalDate dataEntrega;
    private LocalDate dataDevolucao;
}
