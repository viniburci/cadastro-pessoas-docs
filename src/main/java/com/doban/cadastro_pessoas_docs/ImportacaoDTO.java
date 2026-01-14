package com.doban.cadastro_pessoas_docs;

import com.doban.cadastro_pessoas_docs.domain.pessoa.DadosBancariosDTO;
import com.doban.cadastro_pessoas_docs.domain.pessoa.PessoaExcelDTO;
import com.doban.cadastro_pessoas_docs.domain.vaga.VagaDTO;

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
    private byte[] foto;
    private DadosBancariosDTO dadosBancarios;
}

