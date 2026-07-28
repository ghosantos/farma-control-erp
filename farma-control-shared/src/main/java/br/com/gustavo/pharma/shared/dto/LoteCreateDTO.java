package br.com.gustavo.pharma.shared.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class LoteCreateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String numeroLote;
    private Integer quantidadeAtual;
    private LocalDate dataFabricacao;
    private LocalDate dataValidade;
    private Long produtoId;

    public LoteCreateDTO(){}

    public LoteCreateDTO(String numeroLote, Integer quantidadeAtual, LocalDate dataFabricacao, LocalDate dataValidade, Long produtoId) {
        this.numeroLote = numeroLote;
        this.quantidadeAtual = quantidadeAtual;
        this.dataFabricacao = dataFabricacao;
        this.dataValidade = dataValidade;
        this.produtoId = produtoId;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }

    public Integer getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(Integer quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public LocalDate getDataFabricacao() {
        return dataFabricacao;
    }

    public void setDataFabricacao(LocalDate dataFabricacao) {
        this.dataFabricacao = dataFabricacao;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }
}
