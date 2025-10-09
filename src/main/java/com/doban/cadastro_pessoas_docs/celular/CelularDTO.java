package com.doban.cadastro_pessoas_docs.celular;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CelularDTO {
    private String marca;
    private String modelo;
    private String chip;
    private String imei;

    public CelularDTO() {
    }

    public CelularDTO(String marca, String modelo, String chip, String imei) {
        this.marca = marca;
        this.modelo = modelo;
        this.chip = chip;
        this.imei = imei;
    }

    public CelularDTO(CelularDTO celular) {
        this.marca = celular.getMarca();
        this.modelo = celular.getModelo();
        this.chip = celular.getChip();
        this.imei = celular.getImei();
    }

    public Celular toEntity() {
        return new Celular(
                null, this.marca, this.modelo, this.chip, this.imei);
    }

}
