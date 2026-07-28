package br.com.gustavo.pharma.shared.interfaces;

import br.com.gustavo.pharma.shared.dto.*;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface LoteFacadeRemote {
    LoteDTO incluirLote(LoteCreateDTO dto);
    LoteDTO buscarPorId(Long id);
    List<LoteDTO> buscarTodos();
    List<LoteDTO> buscarPorProdutoId(Long produtoId);
    LoteDTO atualizarLote(Long id, LoteUpdateDTO loteUpdateDTO);
    void deletarLote(Long id);
    LoteDTO darBaixaLote(Long loteId, Integer quantidade);
}
