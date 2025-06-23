package com.bancong.bancong.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransacaoDTO {
    private String formaPagamento; // D, C ou P
    private Integer contaDestino;
    private Integer contaOrigem;
    private Double valor;
}
