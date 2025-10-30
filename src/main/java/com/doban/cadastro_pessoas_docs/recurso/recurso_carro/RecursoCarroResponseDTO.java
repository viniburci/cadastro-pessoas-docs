package com.doban.cadastro_pessoas_docs.recurso.recurso_carro;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecursoCarroResponseDTO {

    private Long id;
    private Long pessoaId;
    private Long carroId;
    private String nomePessoa;
    private String modeloCarro;
    private String placa;
    private LocalDate dataEntrega;
    private LocalDate dataDevolucao;

    public RecursoCarroResponseDTO(RecursoCarro recursoCarro) {
        this.id = recursoCarro.getId();
        this.pessoaId = recursoCarro.getPessoa().getId();
        this.carroId = recursoCarro.getCarro().getId();
        this.nomePessoa = recursoCarro.getPessoa().getNome();
        this.modeloCarro = recursoCarro.getCarro().getModelo();
        this.placa = recursoCarro.getCarro().getPlaca();
        this.dataEntrega = recursoCarro.getDataEntrega();
        this.dataDevolucao = recursoCarro.getDataDevolucao();
    }
}

