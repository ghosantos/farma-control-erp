package br.com.gustavo.pharma.ejb.mappers;

import br.com.gustavo.pharma.ejb.entities.Produto;
import br.com.gustavo.pharma.shared.dto.ProdutoCreateDTO;
import br.com.gustavo.pharma.shared.dto.ProdutoDTO;

public class ProdutoMapper {

    public static ProdutoDTO toDTO(Produto produto) {
        int estoqueTotal = 0;

        if (!produto.getlotes().isEmpty()){
            estoqueTotal = produto.getlotes().stream()
                    .mapToInt(l -> l.getQuantidadeAtual())
                    .sum();
        }

        ProdutoDTO dto = new ProdutoDTO(
                produto.getId(),
                produto.getNome(),
                produto.getCodigoBarras(),
                produto.getFabricante(),
                produto.getPrincipioAtivo()
        );

        dto.setEstoqueTotal(estoqueTotal);
        return dto;
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
