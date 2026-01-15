package com.doban.cadastro_pessoas_docs.recurso.dinamico;

import com.doban.cadastro_pessoas_docs.domain.pessoa.Pessoa;
import com.doban.cadastro_pessoas_docs.domain.pessoa.PessoaRepository;
import com.doban.cadastro_pessoas_docs.recurso.item.ItemDinamico;
import com.doban.cadastro_pessoas_docs.recurso.item.ItemDinamicoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecursoDinamicoService {

    private final RecursoDinamicoRepository recursoDinamicoRepository;
    private final ItemDinamicoService itemDinamicoService;
    private final PessoaRepository pessoaRepository;

    @Transactional(readOnly = true)
    public List<RecursoDinamicoDTO> listarTodos() {
        return recursoDinamicoRepository.findAll().stream()
                .map(RecursoDinamicoDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RecursoDinamicoDTO> listarPorPessoa(Long pessoaId) {
        return recursoDinamicoRepository.findByPessoaId(pessoaId).stream()
                .map(RecursoDinamicoDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RecursoDinamicoDTO> listarAtivosParaPessoa(Long pessoaId) {
        return recursoDinamicoRepository.findAtivosParaPessoa(pessoaId).stream()
                .map(RecursoDinamicoDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<RecursoDinamicoDTO> listarPorTipoRecurso(String tipoRecursoCodigo) {
        return recursoDinamicoRepository.findByTipoRecurso(tipoRecursoCodigo).stream()
                .map(RecursoDinamicoDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public RecursoDinamicoDTO buscarPorId(Long id) {
        RecursoDinamico recurso = recursoDinamicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empréstimo não encontrado com id: " + id));
        return new RecursoDinamicoDTO(recurso);
    }

    @Transactional(readOnly = true)
    public RecursoDinamicoDTO buscarAtivoParaItem(Long itemId) {
        RecursoDinamico recurso = recursoDinamicoRepository.findAtivoParaItem(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Item não está emprestado atualmente"));
        return new RecursoDinamicoDTO(recurso);
    }

    @Transactional
    public RecursoDinamicoDTO emprestar(RecursoDinamicoCreateDTO dto) {
        // Verificar se a pessoa existe
        Pessoa pessoa = pessoaRepository.findById(dto.getPessoaId())
                .orElseThrow(() -> new EntityNotFoundException("Pessoa não encontrada com id: " + dto.getPessoaId()));

        // Verificar se o item existe
        ItemDinamico item = itemDinamicoService.buscarEntidadePorId(dto.getItemId());

        // Verificar se o item já está emprestado
        if (recursoDinamicoRepository.existsByItemIdAndDataDevolucaoIsNull(dto.getItemId())) {
            throw new IllegalArgumentException("Este item já está emprestado para outra pessoa");
        }

        // Verificar se o item está ativo
        if (!Boolean.TRUE.equals(item.getAtivo())) {
            throw new IllegalArgumentException("Este item está inativo e não pode ser emprestado");
        }

        RecursoDinamico recurso = RecursoDinamico.builder()
                .pessoa(pessoa)
                .item(item)
                .dataEntrega(dto.getDataEntrega())
                .atributosSnapshot(item.getAtributos())
                .build();

        recurso = recursoDinamicoRepository.save(recurso);
        return new RecursoDinamicoDTO(recurso);
    }

    @Transactional
    public RecursoDinamicoDTO registrarDevolucao(Long id, DevolucaoDTO dto) {
        RecursoDinamico recurso = recursoDinamicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Empréstimo não encontrado com id: " + id));

        if (recurso.getDataDevolucao() != null) {
            throw new IllegalArgumentException("Este empréstimo já foi devolvido em: " + recurso.getDataDevolucao());
        }

        if (dto.getDataDevolucao().isBefore(recurso.getDataEntrega())) {
            throw new IllegalArgumentException("Data de devolução não pode ser anterior à data de entrega");
        }

        recurso.setDataDevolucao(dto.getDataDevolucao());
        recurso = recursoDinamicoRepository.save(recurso);
        return new RecursoDinamicoDTO(recurso);
    }

    @Transactional
    public void deletar(Long id) {
        if (!recursoDinamicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Empréstimo não encontrado com id: " + id);
        }
        recursoDinamicoRepository.deleteById(id);
    }
}
