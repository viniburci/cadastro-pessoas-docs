package com.doban.cadastro_pessoas_docs.recurso.dinamico;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItensExtrasUpdateDTO {
    private List<Map<String, Object>> itensExtras;
}
