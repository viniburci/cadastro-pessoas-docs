package com.doban.cadastro_pessoas_docs.recurso.recurso_celular;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecursoCelularResponseDTO {

    private Long id;
    private String nomePessoa;
    private String modeloCelular;
    private LocalDate dataEntrega;
    private LocalDate dataDevolucao;

    public RecursoCelularResponseDTO(RecursoCelular recursoCelular) {
        this.id = recursoCelular.getId();
        this.nomePessoa = recursoCelular.getPessoa().getNome();
        this.modeloCelular = recursoCelular.getCelular().getModelo();
        this.dataEntrega = recursoCelular.getDataEntrega();
        this.dataDevolucao = recursoCelular.getDataDevolucao();
    }
}

