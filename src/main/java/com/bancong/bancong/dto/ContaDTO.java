package com.bancong.bancong.dto;

import java.math.BigDecimal;

public class ContaDTO {

    private Integer numeroConta;
    private BigDecimal saldo;

    public ContaDTO() {
        this.saldo = BigDecimal.ZERO;
    }

    public ContaDTO(Integer numeroConta, BigDecimal saldo) {
        this.numeroConta = numeroConta;
        this.saldo = saldo != null ? saldo : BigDecimal.ZERO;
    }

    public Integer getNumeroConta() {
        return numeroConta;
    }

    public void setNumeroConta(Integer numeroConta) {
        this.numeroConta = numeroConta;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public void setSaldo(BigDecimal saldo) {
        this.saldo = saldo != null ? saldo : BigDecimal.ZERO;
    }
}
