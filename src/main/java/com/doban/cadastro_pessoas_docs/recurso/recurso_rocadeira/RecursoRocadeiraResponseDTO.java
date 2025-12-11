package com.doban.cadastro_pessoas_docs.recurso.recurso_rocadeira;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecursoRocadeiraResponseDTO {
    
    private Long id;
    private Long pessoaId;
    private Long rocadeiraId;
    private String nomePessoa;
    
    private String marca;
    private String numeroSerie;

    private LocalDate dataEntrega;
    private LocalDate dataDevolucao;

    public RecursoRocadeiraResponseDTO(RecursoRocadeira recursoRocadeira) {
        this.id = recursoRocadeira.getId();
        this.pessoaId = recursoRocadeira.getPessoa().getId();
        this.rocadeiraId = recursoRocadeira.getRocadeira().getId();
        this.nomePessoa = recursoRocadeira.getPessoa().getNome();
        this.marca = recursoRocadeira.getRocadeira().getMarca();
        this.numeroSerie = recursoRocadeira.getRocadeira().getNumeroSerie();
        this.dataEntrega = recursoRocadeira.getDataEntrega();
        this.dataDevolucao = recursoRocadeira.getDataDevolucao();
    }
}
