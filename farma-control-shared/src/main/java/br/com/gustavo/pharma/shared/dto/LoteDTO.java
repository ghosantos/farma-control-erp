package br.com.gustavo.pharma.shared.dto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

public class LoteDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String numeroLote;
    private Integer quantidadeAtual;
    private LocalDate dataFabricacao;
    private LocalDate dataValidade;

    public LoteDTO(){}

    public LoteDTO(Long id, String numeroLote, Integer quantidadeAtual, LocalDate dataFabricacao, LocalDate dataValidade) {
        this.id = id;
        this.numeroLote = numeroLote;
        this.quantidadeAtual = quantidadeAtual;
        this.dataFabricacao = dataFabricacao;
        this.dataValidade = dataValidade;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    public void setDataValidade(LocalDate dataValidade) {
        this.dataValidade = dataValidade;
    }

    public LocalDate getDataFabricacao() {
        return dataFabricacao;
    }

    public void setDataFabricacao(LocalDate dataFabricacao) {
        this.dataFabricacao = dataFabricacao;
    }

    public Integer getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void setQuantidadeAtual(Integer quantidadeAtual) {
        this.quantidadeAtual = quantidadeAtual;
    }

    public String getNumeroLote() {
        return numeroLote;
    }

    public void setNumeroLote(String numeroLote) {
        this.numeroLote = numeroLote;
    }
}
