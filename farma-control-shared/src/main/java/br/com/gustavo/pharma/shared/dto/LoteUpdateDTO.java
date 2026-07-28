package br.com.gustavo.pharma.shared.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class LoteUpdateDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String numeroLote;
    private Integer quantidadeAtual;
    private LocalDate dataFabricacao;
    private LocalDate dataValidade;

    public LoteUpdateDTO() {}

    public LoteUpdateDTO(String numeroLote, Integer quantidadeAtual, LocalDate dataFabricacao, LocalDate dataValidade) {
        this.numeroLote = numeroLote;
        this.quantidadeAtual = quantidadeAtual;
        this.dataFabricacao = dataFabricacao;
        this.dataValidade = dataValidade;
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
}
