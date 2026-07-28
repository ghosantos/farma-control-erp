package br.com.gustavo.pharma.shared.dto;

import java.io.Serial;
import java.io.Serializable;

public class ProdutoCreateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String nome;
    private String codigoBarras;
    private String fabricante;
    private String principioAtivo;

    // Atributo opcional: se vier preenchido, salva o produto com lote; se for null, salva só o produto
    private LoteCreateDTO loteInicial;

    public ProdutoCreateDTO(){}

    public ProdutoCreateDTO(String nome, String codigoBarras, String fabricante, String principioAtivo) {
        this.nome = nome;
        this.codigoBarras = codigoBarras;
        this.fabricante = fabricante;
        this.principioAtivo = principioAtivo;
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

    public LoteCreateDTO getLoteInicial() {
        return loteInicial;
    }

    public void setLoteInicial(LoteCreateDTO loteInicial) {
        this.loteInicial = loteInicial;
    }
}
