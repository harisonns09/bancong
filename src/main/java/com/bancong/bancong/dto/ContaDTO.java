package com.bancong.bancong.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContaDTO {
    private Integer numeroConta;
    private Double saldo;
}
