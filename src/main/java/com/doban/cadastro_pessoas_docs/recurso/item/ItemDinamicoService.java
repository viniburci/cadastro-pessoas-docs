package com.doban.cadastro_pessoas_docs.recurso.item;

import com.doban.cadastro_pessoas_docs.recurso.tipo.TipoRecurso;
import com.doban.cadastro_pessoas_docs.recurso.tipo.TipoRecursoService;
import com.doban.cadastro_pessoas_docs.shared.validation.SchemaValidatorService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemDinamicoService {

    private final ItemDinamicoRepository itemDinamicoRepository;
    private final TipoRecursoService tipoRecursoService;
    private final SchemaValidatorService schemaValidatorService;

    @Transactional(readOnly = true)
    public List<ItemDinamicoDTO> listarTodos() {
        return itemDinamicoRepository.findByAtivoTrue().stream()
                .map(ItemDinamicoDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItemDinamicoDTO> listarPorTipo(String tipoRecursoCodigo) {
        return itemDinamicoRepository.findByTipoRecursoCodigoAndAtivoTrue(tipoRecursoCodigo).stream()
                .map(ItemDinamicoDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItemDinamicoDTO> listarDisponiveis(String tipoRecursoCodigo) {
        return itemDinamicoRepository.findDisponiveis(tipoRecursoCodigo).stream()
                .map(ItemDinamicoDTO::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ItemDinamicoDTO buscarPorId(Long id) {
        ItemDinamico item = itemDinamicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado com id: " + id));
        return new ItemDinamicoDTO(item);
    }

    @Transactional(readOnly = true)
    public ItemDinamico buscarEntidadePorId(Long id) {
        return itemDinamicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado com id: " + id));
    }

    @Transactional
    public ItemDinamicoDTO criar(ItemDinamicoCreateDTO dto) {
        TipoRecurso tipoRecurso = tipoRecursoService.buscarEntidadePorCodigo(dto.getTipoRecursoCodigo());

        // Validar se já existe item com mesmo identificador para este tipo
        if (itemDinamicoRepository.existsByTipoRecursoIdAndIdentificador(
                tipoRecurso.getId(), dto.getIdentificador())) {
            throw new DataIntegrityViolationException(
                    "Já existe um item do tipo " + tipoRecurso.getNome() +
                    " com o identificador: " + dto.getIdentificador());
        }

        // Validar atributos contra o schema do tipo
        if (tipoRecurso.getSchema() != null) {
            schemaValidatorService.validarOuLancarExcecao(dto.getAtributos(), tipoRecurso.getSchema());
        }

        ItemDinamico item = ItemDinamico.builder()
                .tipoRecurso(tipoRecurso)
                .identificador(dto.getIdentificador())
                .atributos(dto.getAtributos())
                .ativo(true)
                .build();

        item = itemDinamicoRepository.save(item);
        return new ItemDinamicoDTO(item);
    }

    @Transactional
    public ItemDinamicoDTO atualizar(Long id, ItemDinamicoUpdateDTO dto) {
        ItemDinamico item = itemDinamicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado com id: " + id));

        // Validar atributos contra o schema se foram alterados
        if (dto.getAtributos() != null && item.getTipoRecurso().getSchema() != null) {
            schemaValidatorService.validarOuLancarExcecao(dto.getAtributos(), item.getTipoRecurso().getSchema());
            item.setAtributos(dto.getAtributos());
        }

        if (dto.getIdentificador() != null) {
            // Verificar se novo identificador já existe
            if (!dto.getIdentificador().equals(item.getIdentificador()) &&
                itemDinamicoRepository.existsByTipoRecursoIdAndIdentificador(
                        item.getTipoRecurso().getId(), dto.getIdentificador())) {
                throw new DataIntegrityViolationException(
                        "Já existe um item do tipo " + item.getTipoRecurso().getNome() +
                        " com o identificador: " + dto.getIdentificador());
            }
            item.setIdentificador(dto.getIdentificador());
        }

        if (dto.getAtivo() != null) {
            item.setAtivo(dto.getAtivo());
        }

        item = itemDinamicoRepository.save(item);
        return new ItemDinamicoDTO(item);
    }

    @Transactional
    public void desativar(Long id) {
        ItemDinamico item = itemDinamicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Item não encontrado com id: " + id));
        item.setAtivo(false);
        itemDinamicoRepository.save(item);
    }

    @Transactional
    public void deletar(Long id) {
        if (!itemDinamicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Item não encontrado com id: " + id);
        }
        itemDinamicoRepository.deleteById(id);
    }
}
