package com.bancong.bancong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.bancong.bancong.controller.TransacaoController;
import com.bancong.bancong.dto.ContaDTO;
import com.bancong.bancong.dto.TransacaoDTO;
import com.bancong.bancong.service.ContaService;

public class TransacaoControllerTest {

    @Mock
    private ContaService contaService;

    @InjectMocks
    private TransacaoController transacaoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRealizarPagamento() {
        TransacaoDTO dto = new TransacaoDTO("D", 1, null, BigDecimal.valueOf(100));
        ContaDTO contaDTO = new ContaDTO(1, BigDecimal.valueOf(900));
        when(contaService.realizarPagamento(dto)).thenReturn(contaDTO);

        ResponseEntity<ContaDTO> response = transacaoController.realizarPagamento(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(contaDTO.getNumeroConta(), response.getBody().getNumeroConta());
        verify(contaService).realizarPagamento(dto);
    }

    @Test
    void testDepositar() {
        TransacaoDTO dto = new TransacaoDTO(null, 1, null, BigDecimal.valueOf(200));

        ResponseEntity<String> response = transacaoController.depositar(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Depósito realizado com sucesso.", response.getBody());
        verify(contaService).depositar(dto.getContaDebitada(), dto.getValor());
    }

    @Test
    void testTransferir() {
        TransacaoDTO dto = new TransacaoDTO(null, 1, 2, BigDecimal.valueOf(150));

        ResponseEntity<String> response = transacaoController.transferir(dto);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Transferência realizada com sucesso.", response.getBody());
        verify(contaService).transferir(dto.getContaDebitada(), dto.getContaCreditada(), dto.getValor());
    }
}