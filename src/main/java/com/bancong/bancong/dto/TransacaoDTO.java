package com.bancong.bancong.dto;

import java.math.BigDecimal;

public class TransacaoDTO {

    private String formaPagamento; // D, C ou P
    private Integer contaDebitada;
    private Integer contaCreditada;
    private BigDecimal valor;

    public TransacaoDTO() {
        this.valor = BigDecimal.ZERO;
    }

    public TransacaoDTO(String formaPagamento, Integer contaDebitada, Integer contaCreditada, BigDecimal valor) {
        this.formaPagamento = formaPagamento;
        this.contaDebitada = contaDebitada;
        this.contaCreditada = contaCreditada;
        this.valor = valor != null ? valor : BigDecimal.ZERO;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public Integer getContaDebitada() {
        return contaDebitada;
    }

    public void setContaDebitada(Integer contaDebitada) {
        this.contaDebitada = contaDebitada;
    }

    public Integer getContaCreditada() {
        return contaCreditada;
    }

    public void setContaCreditada(Integer contaCreditada) {
        this.contaCreditada = contaCreditada;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor != null ? valor : BigDecimal.ZERO;
    }
}

