package com.doban.cadastro_pessoas_docs.recurso.recurso_rocadeira;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecursoRocadeiraRequestDTO {
    private Long rocadeiraId;
    private Long pessoaId;
    private LocalDate dataEntrega;
    private LocalDate dataDevolucao;
}
