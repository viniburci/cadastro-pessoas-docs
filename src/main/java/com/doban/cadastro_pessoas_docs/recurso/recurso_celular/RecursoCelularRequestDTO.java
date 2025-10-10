package com.doban.cadastro_pessoas_docs.recurso.recurso_celular;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecursoCelularRequestDTO {

    private Long celularId;
    private Long pessoaId;
    private LocalDate dataEntrega;
    private LocalDate dataDevolucao;
}
