package com.doban.cadastro_pessoas_docs.carro;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CarroDTO {
    private String marca;
        private String modelo;
        private String cor;
        private String chassi;
        private String placa;
        private String anoModelo;

        public CarroDTO() {
        }

        public CarroDTO(String marca, String modelo, String cor, String chassi, String placa, String anoModelo) {
            this.marca = marca;
            this.modelo = modelo;
            this.cor = cor;
            this.chassi = chassi;
            this.placa = placa;
            this.anoModelo = anoModelo;
        }

        public CarroDTO(CarroDTO carro) {
            this.marca = carro.getMarca();
            this.modelo = carro.getModelo();
            this.cor = carro.getCor();
            this.chassi = carro.getChassi();
            this.placa = carro.getPlaca();
            this.anoModelo = carro.getAnoModelo();
        }

        public Carro toEntity() {
            if (placa == null || placa.trim().isEmpty() || placa.equalsIgnoreCase("null") || placa.isBlank()
                    || placa.length() < 7) {
                return null;
            }
            return new Carro(
                    null, this.marca, this.modelo, this.cor, this.chassi,
                    this.placa, this.anoModelo);
        }
}
