package com.bancong.bancong;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import com.bancong.bancong.dto.ContaDTO;
import com.bancong.bancong.dto.TransacaoDTO;

public class DTOsTests {

    @Test
    void testGettersAndSetters() {
        ContaDTO dto = new ContaDTO();
        assertEquals(BigDecimal.ZERO, dto.getSaldo());

        dto.setNumeroConta(123);
        dto.setSaldo(BigDecimal.valueOf(500));

        assertEquals(123, dto.getNumeroConta());
        assertEquals(BigDecimal.valueOf(500), dto.getSaldo());

        // Testa valor nulo no saldo (deve virar zero)
        dto.setSaldo(null);
        assertEquals(BigDecimal.ZERO, dto.getSaldo());


        TransacaoDTO dtoTransacao = new TransacaoDTO();
        assertEquals(BigDecimal.ZERO, dtoTransacao.getValor());

        dtoTransacao.setFormaPagamento("D");
        dtoTransacao.setContaDebitada(1);
        dtoTransacao.setContaCreditada(2);
        dtoTransacao.setValor(BigDecimal.valueOf(300));

        assertEquals("D", dtoTransacao.getFormaPagamento());
        assertEquals(1, dtoTransacao.getContaDebitada());
        assertEquals(2, dtoTransacao.getContaCreditada());
        assertEquals(BigDecimal.valueOf(300), dtoTransacao.getValor());

        // Testa valor nulo no valor (deve virar zero)
        dtoTransacao.setValor(null);
        assertEquals(BigDecimal.ZERO, dtoTransacao.getValor());

    }

    @Test
    void testConstructorWithArgs() {
        ContaDTO dto = new ContaDTO(10, BigDecimal.valueOf(1000));
        assertEquals(10, dto.getNumeroConta());
        assertEquals(BigDecimal.valueOf(1000), dto.getSaldo());

        ContaDTO dtoNullSaldo = new ContaDTO(11, null);
        assertEquals(11, dtoNullSaldo.getNumeroConta());
        assertEquals(BigDecimal.ZERO, dtoNullSaldo.getSaldo());


        TransacaoDTO dtoTransacao = new TransacaoDTO("C", 10, 20, BigDecimal.valueOf(500));
        assertEquals("C", dtoTransacao.getFormaPagamento());
        assertEquals(10, dtoTransacao.getContaDebitada());
        assertEquals(20, dtoTransacao.getContaCreditada());
        assertEquals(BigDecimal.valueOf(500), dtoTransacao.getValor());

        TransacaoDTO dtoNullValor = new TransacaoDTO("P", 11, 21, null);
        assertEquals(BigDecimal.ZERO, dtoNullValor.getValor());
    }

}
