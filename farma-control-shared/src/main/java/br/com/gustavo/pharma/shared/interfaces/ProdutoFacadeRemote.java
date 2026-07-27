package br.com.gustavo.pharma.shared.interfaces;

import br.com.gustavo.pharma.shared.dto.ProdutoCreateDTO;
import br.com.gustavo.pharma.shared.dto.ProdutoDTO;
import br.com.gustavo.pharma.shared.dto.ProdutoUpdateDTO;
import jakarta.ejb.Remote;

import java.util.List;

@Remote
public interface ProdutoFacadeRemote {
    ProdutoDTO incluirProduto(ProdutoCreateDTO dto);
    ProdutoDTO buscarPorId(Long id);
    List<ProdutoDTO> buscarTodos();
    ProdutoDTO atualizarProduto(Long id, ProdutoUpdateDTO produtoUpdateDTO);
    void deletarProduto(Long id);
}
