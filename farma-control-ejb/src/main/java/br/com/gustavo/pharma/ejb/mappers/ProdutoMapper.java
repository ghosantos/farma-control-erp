package br.com.gustavo.pharma.ejb.mappers;

import br.com.gustavo.pharma.ejb.entities.Produto;
import br.com.gustavo.pharma.shared.dto.ProdutoCreateDTO;
import br.com.gustavo.pharma.shared.dto.ProdutoDTO;

public class ProdutoMapper {

    public static ProdutoDTO toDTO(Produto produto) {
        return new ProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getCodigoBarras(),
                produto.getFabricante(),
                produto.getPrincipioAtivo()
        );
    }

    public static Produto toEntity(ProdutoCreateDTO dto) {
        return new Produto(
                dto.getNome(),
                dto.getCodigoBarras(),
                dto.getFabricante(),
                dto.getPrincipioAtivo()
        );
    }

}
