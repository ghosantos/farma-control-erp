package br.com.gustavo.pharma.swing.controllers;

import br.com.gustavo.pharma.shared.dto.LoteCreateDTO;
import br.com.gustavo.pharma.shared.dto.LoteDTO;
import br.com.gustavo.pharma.shared.dto.LoteUpdateDTO;
import br.com.gustavo.pharma.shared.exceptions.DomainException;
import br.com.gustavo.pharma.shared.interfaces.LoteFacadeRemote;
import br.com.gustavo.pharma.swing.locator.EJBServiceLocator;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class LoteController {

    private final LoteFacadeRemote loteFacade;

    public LoteController() {
        this.loteFacade = EJBServiceLocator.getLoteFacade();
    }

    public Optional<LoteDTO> incluirLote(LoteCreateDTO dto, Component parent){
        try {
            LoteDTO loteDTO = loteFacade.incluirLote(dto);
            JOptionPane.showMessageDialog(parent, "Lote cadastrado com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
            return Optional.of(loteDTO);
        }catch (Exception e){
            tratarErro(parent, "incluir lote", e);
            return Optional.empty();
        }
    }

    public Optional<LoteDTO> buscarPorId(Long id, Component parent){
        try {
            return Optional.ofNullable(loteFacade.buscarPorId(id));
        }catch (Exception e){
            tratarErro(parent, "buscar o produto", e);
            return Optional.empty();
        }
    }

    public List<LoteDTO> buscarTodos(Component parent){
        try {
            return loteFacade.buscarTodos();
        }catch (Exception e){
            tratarErro(parent, "carregar a lista de lotes", e);
            return Collections.emptyList();
        }
    }

    public List<LoteDTO> buscarPorProdutoId(Long produtoId, Component parent) {
        try {
            return loteFacade.buscarPorProdutoId(produtoId);
        } catch (Exception e) {
            tratarErro(parent, "carregar lotes do produto", e);
            return Collections.emptyList();
        }
    }

    public Optional<LoteDTO> atualizarLote(Long id, LoteUpdateDTO dto, Component parent){
        try {
            LoteDTO loteDTO = loteFacade.atualizarLote(id, dto);
            JOptionPane.showMessageDialog(parent, "Lote atualizado com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
            return Optional.of(loteDTO);
        }catch (Exception e){
            tratarErro(parent, "atualizar lote", e);
            return Optional.empty();
        }
    }

    public boolean deletarLote(Long id, Component parent){
        try {
            loteFacade.deletarLote(id);
            JOptionPane.showMessageDialog(parent, "Lote excluído com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }catch (Exception e){
            tratarErro(parent, "deletar lote", e);
            return false;
        }
    }

    public Optional<LoteDTO> darBaixaLote(Long id, Integer quantidade, Component parent){
        try {
            LoteDTO loteDTO = loteFacade.darBaixaLote(id, quantidade);
            JOptionPane.showMessageDialog(parent, "Baixa efetuada com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
            return Optional.of(loteDTO);
        }catch (Exception e){
            tratarErro(parent, "dar baixa no lote", e);
            return Optional.empty();
        }
    }

    private void tratarErro(Component parent, String acao, Exception e){
        Throwable cause = e.getCause();

        if (e instanceof DomainException){
            JOptionPane.showMessageDialog(parent, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        } else if (cause instanceof DomainException) {
            JOptionPane.showMessageDialog(parent, e.getCause(), "Aviso", JOptionPane.WARNING_MESSAGE);
        }else {
            e.printStackTrace();
            JOptionPane.showMessageDialog(parent,
                    "Ocorreu um erro inesperado ao " + acao + ".\nSe o problema persistir, contate o suporte técnico.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
