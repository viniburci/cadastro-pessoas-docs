package com.doban.cadastro_pessoas_docs.domain.pessoa;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final DadosBancariosService dadosBancariosService;

    PessoaService(PessoaRepository pessoaRepository, DadosBancariosService dadosBancariosService) {
        this.pessoaRepository = pessoaRepository;
        this.dadosBancariosService = dadosBancariosService;
    }

    public List<PessoaDTO> buscarTodasPessoas() {
        return pessoaRepository.findAll().stream().map(PessoaDTO::new).toList();
    }

    public Pessoa buscarEntidadePessoaPorId(Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada"));
    }

    public PessoaDTO buscarPessoaPorId(Long id) {
        Pessoa pessoa = buscarEntidadePessoaPorId(id);
        return new PessoaDTO(pessoa);
    }

    public PessoaDTO salvarPessoa(PessoaDTO pessoaDTO) {
        try {
            Pessoa pessoa = pessoaDTO.toEntity();
            Pessoa pessoaSalva = pessoaRepository.save(pessoa);
            if (pessoaDTO.getDadosBancarios() != null && !pessoaDTO.getDadosBancarios().isEmpty()) {
                dadosBancariosService.salvar(pessoaSalva.getId(), pessoaDTO.getDadosBancarios());
                pessoaSalva = pessoaRepository.findById(pessoaSalva.getId()).orElse(pessoaSalva);
            }
            return new PessoaDTO(pessoaSalva);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("Violação de integridade: dados inválidos ou duplicados.");
        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar pessoa: " + e.getMessage(), e);
        }
    }

    public PessoaDTO atualizarPessoa(Long id, PessoaDTO dto) {
        if (dto.getId() != null && !dto.getId().equals(id)) {
            throw new IllegalArgumentException("ID do DTO não corresponde ao ID do parâmetro");
        }

        Pessoa pessoa = pessoaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pessoa não encontrada com id: " + id));

        dto.atualizarEntidade(pessoa);
        pessoa = pessoaRepository.save(pessoa);

        if (dto.getDadosBancarios() != null && !dto.getDadosBancarios().isEmpty()) {
            dadosBancariosService.salvar(id, dto.getDadosBancarios());
            pessoa = pessoaRepository.findById(id).orElse(pessoa);
        }

        return new PessoaDTO(pessoa);
    }

    public void deletarPessoa(Long id) {
        pessoaRepository.deleteById(id);
    }

    public List<PessoaDTO> buscarPessoasAtivas() {
        LocalDate hoje = LocalDate.now();
        return pessoaRepository.findAll().stream()
                .filter(pessoa -> pessoa.getVagas().stream()
                        .anyMatch(vaga -> vaga.getDataDemissao() == null || vaga.getDataDemissao().isAfter(hoje)))
                .map(PessoaDTO::new)
                .toList();
    }

    public List<PessoaDTO> buscarPessoasInativas() {
        LocalDate hoje = LocalDate.now();
        return pessoaRepository.findAll().stream()
                .filter(pessoa -> pessoa.getVagas().isEmpty() ||
                        pessoa.getVagas().stream()
                                .allMatch(vaga -> vaga.getDataDemissao() != null && !vaga.getDataDemissao().isAfter(hoje)))
                .map(PessoaDTO::new)
                .toList();
    }

    public void salvarFoto(Long pessoaId, byte[] foto) {
        if (foto == null || foto.length == 0) {
            throw new IllegalArgumentException("Foto não pode ser vazia");
        }

        // Limite de 5MB para fotos
        if (foto.length > 5_000_000) {
            throw new IllegalArgumentException("Foto muito grande. Tamanho máximo: 5MB");
        }

        // Converte para JPEG para garantir compatibilidade com geração de PDF
        byte[] fotoJpeg = converterParaJpeg(foto);

        Pessoa pessoa = buscarEntidadePessoaPorId(pessoaId);
        pessoa.setFoto(fotoJpeg);
        pessoaRepository.save(pessoa);
    }

    private byte[] converterParaJpeg(byte[] imagemOriginal) {
        try {
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imagemOriginal));
            if (image == null) {
                throw new IllegalArgumentException(
                    "Formato de imagem não suportado. Envie JPEG, PNG, BMP ou GIF.");
            }

            // Remove canal alpha (transparência) para compatibilidade com JPEG
            BufferedImage rgbImage = new BufferedImage(
                    image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            rgbImage.createGraphics().drawImage(image, 0, 0, java.awt.Color.WHITE, null);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(rgbImage, "jpg", bos);
            return bos.toByteArray();
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalArgumentException(
                "Erro ao processar imagem. Envie JPEG, PNG, BMP ou GIF.", e);
        }
    }

    public byte[] buscarFoto(Long pessoaId) {
        Pessoa pessoa = buscarEntidadePessoaPorId(pessoaId);
        byte[] foto = pessoa.getFoto();

        if (foto == null || foto.length == 0) {
            throw new EntityNotFoundException("Foto não encontrada para a pessoa com id: " + pessoaId);
        }

        return foto;
    }
}
