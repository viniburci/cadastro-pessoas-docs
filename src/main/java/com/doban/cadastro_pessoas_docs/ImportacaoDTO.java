package com.doban.cadastro_pessoas_docs;

import com.doban.cadastro_pessoas_docs.pessoa.PessoaExcelDTO;
import com.doban.cadastro_pessoas_docs.pessoa.PessoaExcelDTO.CarroDTO;
import com.doban.cadastro_pessoas_docs.pessoa.PessoaExcelDTO.CelularDTO;
import com.doban.cadastro_pessoas_docs.pessoa.PessoaExcelDTO.VagaDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ImportacaoDTO {
    private PessoaExcelDTO pessoa;
    private VagaDTO vaga;
    private CarroDTO carro;
    private CelularDTO celular;
}

