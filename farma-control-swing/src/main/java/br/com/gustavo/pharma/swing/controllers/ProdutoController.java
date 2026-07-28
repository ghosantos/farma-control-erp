package br.com.gustavo.pharma.swing.controllers;

import br.com.gustavo.pharma.shared.dto.ProdutoCreateDTO;
import br.com.gustavo.pharma.shared.dto.ProdutoDTO;
import br.com.gustavo.pharma.shared.dto.ProdutoUpdateDTO;
import br.com.gustavo.pharma.shared.exceptions.DomainException;
import br.com.gustavo.pharma.shared.interfaces.ProdutoFacadeRemote;
import br.com.gustavo.pharma.swing.locator.EJBServiceLocator;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class ProdutoController {
    private final ProdutoFacadeRemote produtoFacade;

    public ProdutoController() {
        // Busca a instância remota através do locator
        this.produtoFacade = EJBServiceLocator.getProdutoFacade();
    }

    public Optional<ProdutoDTO> incluirProduto(ProdutoCreateDTO dto, Component parent){
        try {
            ProdutoDTO produtoDTO = produtoFacade.incluirProduto(dto);
            JOptionPane.showMessageDialog(parent, "Produto cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            return Optional.of(produtoDTO);
        }catch (Exception e){
            tratarErro(parent, "cadastrar o produto", e);
            return Optional.empty();
        }
    }

    public Optional<ProdutoDTO> buscarPorId(Long id, Component parent){
        try {
            return Optional.ofNullable(produtoFacade.buscarPorId(id));
        }catch (Exception e){
            tratarErro(parent, "buscar o produto", e);
            return Optional.empty();
        }
    }

    public List<ProdutoDTO> buscarTodos(Component parent){
        try{
            return produtoFacade.buscarTodos();
        }catch (Exception e){
            tratarErro(parent, "carregar a lista de produtos", e);
            return Collections.emptyList();
        }
    }

    public Optional<ProdutoDTO> atualizarProduto(Long id, ProdutoUpdateDTO dto, Component parent){
        try {
            ProdutoDTO produtoUpdateDTO = produtoFacade.atualizarProduto(id, dto);
            JOptionPane.showMessageDialog(parent, "Produto atualizado com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
            return Optional.of(produtoUpdateDTO);
        }catch (Exception e){
            tratarErro(parent, "atualizar produto", e);
            return Optional.empty();
        }
    }

    public boolean deletarProduto(Long id, Component parent){
        try {
            produtoFacade.deletarProduto(id);
            JOptionPane.showMessageDialog(parent, "Produto excluído com sucesso!", "Sucesso!", JOptionPane.INFORMATION_MESSAGE);
            return true;
        }catch (Exception e){
            tratarErro(parent, "deletar produto", e);
            return false;
        }
    }

    private void tratarErro(Component parent, String acao, Exception e) {
        Throwable cause = e.getCause();

        if (e instanceof DomainException) {
            JOptionPane.showMessageDialog(parent, e.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        } else if (cause instanceof DomainException) {
            JOptionPane.showMessageDialog(parent, cause.getMessage(), "Aviso", JOptionPane.WARNING_MESSAGE);
        } else {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    parent,
                    "Ocorreu um erro inesperado ao " + acao + ".\nSe o problema persistir, contate o suporte técnico.",
                    "Erro",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}
