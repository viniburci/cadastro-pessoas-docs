package com.doban.cadastro_pessoas_docs.domain.carro;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CarroService {

    private final CarroRepository carroRepository;

    public CarroService(CarroRepository carroRepository) {
        this.carroRepository = carroRepository;
    }

    public List<Carro> listarTodos() {
        return carroRepository.findAll();
    }

    public Carro buscarPorId(Long id) {
        return carroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Carro não encontrado com id: " + id));
    }

    public Carro salvar(Carro carro) {
        return carroRepository.save(carro);
    }

    public Carro atualizar(Long id, Carro carroAtualizado) {
        Carro carro = buscarPorId(id);

        carro.setMarca(carroAtualizado.getMarca());
        carro.setModelo(carroAtualizado.getModelo());
        carro.setCor(carroAtualizado.getCor());
        carro.setChassi(carroAtualizado.getChassi());
        carro.setPlaca(carroAtualizado.getPlaca());
        carro.setAnoModelo(carroAtualizado.getAnoModelo());

        return carroRepository.save(carro);
    }

    public void deletar(Long id) {
        carroRepository.deleteById(id);
    }
}

