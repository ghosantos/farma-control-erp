package br.com.gustavo.pharma.ejb.mappers;

import br.com.gustavo.pharma.ejb.entities.Lote;
import br.com.gustavo.pharma.shared.dto.LoteDTO;

public class LoteMapper {

    public static LoteDTO toDTO(Lote lote){
        return new LoteDTO(lote.getNumeroLote(), lote.getQuantidadeAtual(),
                lote.getDataFabricacao(), lote.getDataValidade());
    }

    public static Lote toEntity(LoteDTO loteDTO){
        return new Lote(loteDTO.getNumeroLote(), loteDTO.getQuantidadeAtual(),
                loteDTO.getDataFabricacao(), loteDTO.getDataValidade());
    }
}
