package com.bancong.bancong.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransacaoDTO {
    private String formaPagamento; // D, C ou P
    private Integer contaDestino;
    private Integer contaOrigem;
    private BigDecimal valor;
}
