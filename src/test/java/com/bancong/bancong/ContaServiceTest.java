package com.bancong.bancong;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bancong.bancong.dto.ContaDTO;
import com.bancong.bancong.dto.TransacaoDTO;
import com.bancong.bancong.exception.ContaNaoEncontradaException;
import com.bancong.bancong.exception.SaldoInsuficienteException;
import com.bancong.bancong.model.Conta;
import com.bancong.bancong.repository.ContaRepository;
import com.bancong.bancong.service.ContaService;

public class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @InjectMocks
    private ContaService contaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void criarConta_deveSalvarContaERetornarDTO() {
        Conta conta = new Conta();
        conta.setConta(1);
        conta.setSaldo(BigDecimal.valueOf(1000));

        ContaDTO contaDTO = new ContaDTO(1, BigDecimal.valueOf(1000));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        ContaDTO resultado = contaService.criarConta(contaDTO);

        assertEquals(conta.getConta(), resultado.getNumeroConta());
        assertEquals(conta.getSaldo(), resultado.getSaldo());
        verify(contaRepository, times(1)).save(any(Conta.class));
    }

    @Test
    void consultarConta_quandoContaExiste_deveRetornarDTO() {
        Conta conta = new Conta();
        conta.setConta(1);
        conta.setSaldo(BigDecimal.valueOf(500));
        when(contaRepository.findById(1)).thenReturn(Optional.of(conta));

        ContaDTO dto = contaService.consultarConta(1);

        assertEquals(1, dto.getNumeroConta());
        assertEquals(BigDecimal.valueOf(500), dto.getSaldo());
    }

    @Test
    void consultarConta_quandoContaNaoExiste_deveLancarException() {
        when(contaRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> contaService.consultarConta(1));
        assertEquals("Conta não encontrada", exception.getMessage());
    }

    @Test
    void listarContas_deveRetornarListaDeDTO() {
        Conta conta1 = new Conta();
        conta1.setConta(1);
        conta1.setSaldo(BigDecimal.valueOf(100));

        Conta conta2 = new Conta();
        conta2.setConta(2);
        conta2.setSaldo(BigDecimal.valueOf(200));

        when(contaRepository.findAll()).thenReturn(Arrays.asList(conta1, conta2));

        List<ContaDTO> contas = contaService.listarContas();

        assertEquals(2, contas.size());
        assertEquals(1, contas.get(0).getNumeroConta());
        assertEquals(BigDecimal.valueOf(100), contas.get(0).getSaldo());
        assertEquals(2, contas.get(1).getNumeroConta());
        assertEquals(BigDecimal.valueOf(200), contas.get(1).getSaldo());
    }

    @Test
    void realizarPagamento_comSaldoSuficiente_deveDebitarESalvar() {
        Conta conta = new Conta();
        conta.setConta(1);
        conta.setSaldo(BigDecimal.valueOf(200));

        TransacaoDTO dto = new TransacaoDTO("D", 1, null, BigDecimal.valueOf(100));

        when(contaRepository.findById(1)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ContaDTO resultado = contaService.realizarPagamento(dto);

        BigDecimal taxa = BigDecimal.valueOf(100).multiply(BigDecimal.valueOf(0.03));
        BigDecimal esperado = BigDecimal.valueOf(200).subtract(BigDecimal.valueOf(100).add(taxa));

        assertEquals(esperado, resultado.getSaldo());
        verify(contaRepository).save(conta);
    }

    @Test
    void realizarPagamento_comSaldoInsuficiente_deveLancarSaldoInsuficienteException() {
        Conta conta = new Conta();
        conta.setConta(1);
        conta.setSaldo(BigDecimal.valueOf(50));

        TransacaoDTO dto = new TransacaoDTO("D", 1, null, BigDecimal.valueOf(100));

        when(contaRepository.findById(1)).thenReturn(Optional.of(conta));

        assertThrows(SaldoInsuficienteException.class, () -> contaService.realizarPagamento(dto));
    }

    @Test
    void realizarPagamento_comContaNaoEncontrada_deveLancarException() {
        TransacaoDTO dto = new TransacaoDTO("D", 1, null, BigDecimal.valueOf(100));
        when(contaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ContaNaoEncontradaException.class, () -> contaService.realizarPagamento(dto));
    }

    @Test
    void depositar_valorPositivo_deveSomarSaldo() {
        Conta conta = new Conta();
        conta.setConta(1);
        conta.setSaldo(BigDecimal.valueOf(100));

        when(contaRepository.findById(1)).thenReturn(Optional.of(conta));
        when(contaRepository.save(any(Conta.class))).thenReturn(conta);

        contaService.depositar(1, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), conta.getSaldo());
        verify(contaRepository).save(conta);
    }

    @Test
    void depositar_valorNegativoOuZero_deveLancarException() {
        assertThrows(RuntimeException.class, () -> contaService.depositar(1, BigDecimal.ZERO));
        assertThrows(RuntimeException.class, () -> contaService.depositar(1, BigDecimal.valueOf(-10)));
    }

    @Test
    void depositar_contaNaoEncontrada_deveLancarException() {
        when(contaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> contaService.depositar(1, BigDecimal.valueOf(50)));
    }

    @Test
    void transferir_valorPositivoESaldoSuficiente_deveAtualizarSaldos() {
        Conta origem = new Conta();
        origem.setConta(1);
        origem.setSaldo(BigDecimal.valueOf(200));

        Conta destino = new Conta();
        destino.setConta(2);
        destino.setSaldo(BigDecimal.valueOf(100));

        when(contaRepository.findById(1)).thenReturn(Optional.of(origem));
        when(contaRepository.findById(2)).thenReturn(Optional.of(destino));
        when(contaRepository.save(any(Conta.class))).thenAnswer(invocation -> invocation.getArgument(0));

        contaService.transferir(1, 2, BigDecimal.valueOf(50));

        assertEquals(BigDecimal.valueOf(150), origem.getSaldo());
        assertEquals(BigDecimal.valueOf(150), destino.getSaldo());
        verify(contaRepository, times(2)).save(any(Conta.class));
    }

    @Test
    void transferir_valorNegativoOuZero_deveLancarException() {
        assertThrows(RuntimeException.class, () -> contaService.transferir(1, 2, BigDecimal.ZERO));
        assertThrows(RuntimeException.class, () -> contaService.transferir(1, 2, BigDecimal.valueOf(-10)));
    }

    @Test
    void transferir_contaOrigemNaoEncontrada_deveLancarException() {
        when(contaRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> contaService.transferir(1, 2, BigDecimal.valueOf(50)));
    }

    @Test
    void transferir_contaDestinoNaoEncontrada_deveLancarException() {
        Conta origem = new Conta();
        origem.setConta(1);
        origem.setSaldo(BigDecimal.valueOf(200));

        when(contaRepository.findById(1)).thenReturn(Optional.of(origem));
        when(contaRepository.findById(2)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> contaService.transferir(1, 2, BigDecimal.valueOf(50)));
    }

    @Test
    void transferir_saldoInsuficiente_deveLancarSaldoInsuficienteException() {
        Conta origem = new Conta();
        origem.setConta(1);
        origem.setSaldo(BigDecimal.valueOf(30));

        Conta destino = new Conta();
        destino.setConta(2);
        destino.setSaldo(BigDecimal.valueOf(100));

        when(contaRepository.findById(1)).thenReturn(Optional.of(origem));
        when(contaRepository.findById(2)).thenReturn(Optional.of(destino));

        assertThrows(SaldoInsuficienteException.class, () -> contaService.transferir(1, 2, BigDecimal.valueOf(50)));
    }

    @Test
    void calcularTaxa_deveCalcularCorretamente() throws Exception {
        // Usa reflexão para testar método privado
        var method = ContaService.class.getDeclaredMethod("calcularTaxa", String.class, BigDecimal.class);
        method.setAccessible(true);

        BigDecimal valor = BigDecimal.valueOf(100);

        BigDecimal taxaD = (BigDecimal) method.invoke(contaService, "D", valor);
        BigDecimal taxaC = (BigDecimal) method.invoke(contaService, "C", valor);
        BigDecimal taxaP = (BigDecimal) method.invoke(contaService, "P", valor);

        assertEquals(BigDecimal.valueOf(3.0).setScale(1), taxaD.setScale(1));
        assertEquals(BigDecimal.valueOf(5.0).setScale(1), taxaC.setScale(1));
        assertEquals(BigDecimal.ZERO.setScale(1), taxaP.setScale(1));

        // Testa formato inválido
        assertThrows(IllegalArgumentException.class, () -> method.invoke(contaService, "X", valor));
    }
}
