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
    private Long pessoaId;
    private Long celularId;
    private String nomePessoa;
    private String modeloCelular;
    private String imei;
    private LocalDate dataEntrega;
    private LocalDate dataDevolucao;

    public RecursoCelularResponseDTO(RecursoCelular recursoCelular) {
        this.id = recursoCelular.getId();
        this.pessoaId = recursoCelular.getPessoa().getId();
        this.celularId = recursoCelular.getCelular().getId();
        this.nomePessoa = recursoCelular.getPessoa().getNome();
        this.modeloCelular = recursoCelular.getCelular().getModelo();
        this.imei = recursoCelular.getCelular().getImei();
        this.dataEntrega = recursoCelular.getDataEntrega();
        this.dataDevolucao = recursoCelular.getDataDevolucao();
    }
}

