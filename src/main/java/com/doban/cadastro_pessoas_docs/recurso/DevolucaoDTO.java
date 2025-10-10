package com.doban.cadastro_pessoas_docs.recurso;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DevolucaoDTO {
    private LocalDate dataDevolucao;
}
