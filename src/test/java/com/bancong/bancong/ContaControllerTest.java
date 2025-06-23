package com.bancong.bancong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import com.bancong.bancong.controller.ContaController;
import com.bancong.bancong.dto.ContaDTO;
import com.bancong.bancong.service.ContaService;

public class ContaControllerTest {

    @Mock
    private ContaService contaService;

    @InjectMocks
    private ContaController contaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCriarConta() {
        ContaDTO dto = new ContaDTO(1, BigDecimal.valueOf(100));
        when(contaService.criarConta(any())).thenReturn(dto);

        ResponseEntity<ContaDTO> response = contaController.criarConta(dto);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals(dto.getNumeroConta(), response.getBody().getNumeroConta());
    }

    @Test
    void testConsultarConta() {
        ContaDTO dto = new ContaDTO(1, BigDecimal.valueOf(100));
        when(contaService.consultarConta(1)).thenReturn(dto);

        ResponseEntity<ContaDTO> response = contaController.consultar(1);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(dto.getSaldo(), response.getBody().getSaldo());
    }

    @Test
    void testListarContas() {
        List<ContaDTO> contas = Arrays.asList(
                new ContaDTO(1, BigDecimal.valueOf(100)),
                new ContaDTO(2, BigDecimal.valueOf(200))
        );
        when(contaService.listarContas()).thenReturn(contas);

        ResponseEntity<List<ContaDTO>> response = contaController.listarContas();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }
}
