package br.com.gustavo.pharma.shared.dto;

import java.io.Serial;
import java.io.Serializable;

public class ProdutoDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String nome;
    private String codigoBarras;
    private String fabricante;
    private String principioAtivo;

    private Integer estoqueTotal;

    public ProdutoDTO(){}

    public ProdutoDTO(Long id, String nome, String codigoBarras, String fabricante, String principioAtivo) {
        this.id = id;
        this.nome = nome;
        this.codigoBarras = codigoBarras;
        this.fabricante = fabricante;
        this.principioAtivo = principioAtivo;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCodigoBarras() {
        return codigoBarras;
    }

    public void setCodigoBarras(String codigoBarras) {
        this.codigoBarras = codigoBarras;
    }

    public String getFabricante() {
        return fabricante;
    }

    public void setFabricante(String fabricante) {
        this.fabricante = fabricante;
    }

    public String getPrincipioAtivo() {
        return principioAtivo;
    }

    public void setPrincipioAtivo(String principioAtivo) {
        this.principioAtivo = principioAtivo;
    }

    public Integer getEstoqueTotal() {
        return estoqueTotal;
    }

    public void setEstoqueTotal(Integer estoqueTotal) {
        this.estoqueTotal = estoqueTotal;
    }
}
