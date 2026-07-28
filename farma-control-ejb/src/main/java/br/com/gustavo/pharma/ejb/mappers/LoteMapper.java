package br.com.gustavo.pharma.ejb.mappers;

import br.com.gustavo.pharma.ejb.entities.Lote;
import br.com.gustavo.pharma.shared.dto.LoteCreateDTO;
import br.com.gustavo.pharma.shared.dto.LoteDTO;
import br.com.gustavo.pharma.shared.dto.ProdutoDTO;

public class LoteMapper {

    public static LoteDTO toDTO(Lote lote){
        return new LoteDTO(
                lote.getId(),
                lote.getNumeroLote(),
                lote.getQuantidadeAtual(),
                lote.getDataFabricacao(),
                lote.getDataValidade()
        );
    }

    public static Lote toEntity(LoteCreateDTO loteDTO){
        return new Lote(
                loteDTO.getNumeroLote(),
                loteDTO.getQuantidadeAtual(),
                loteDTO.getDataFabricacao(),
                loteDTO.getDataValidade()
        );
    }
}
