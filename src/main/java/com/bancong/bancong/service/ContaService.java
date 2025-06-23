package com.bancong.bancong.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bancong.bancong.dto.ContaDTO;
import com.bancong.bancong.dto.TransacaoDTO;
import com.bancong.bancong.exception.ContaNaoEncontradaException;
import com.bancong.bancong.exception.SaldoInsuficienteException;
import com.bancong.bancong.model.Conta;
import com.bancong.bancong.repository.ContaRepository;

@Service
public class ContaService {

    private final ContaRepository contaRepository;

    public ContaService(ContaRepository contaRepository) {
        this.contaRepository = contaRepository;
    }

    public ContaDTO criarConta(ContaDTO contaDTO) {
        Conta conta = new Conta();
        conta.setSaldo(contaDTO.getSaldo());
        Conta contaSalva = contaRepository.save(conta);
        return new ContaDTO(contaSalva.getConta(), contaSalva.getSaldo());
    }

    public ContaDTO consultarConta(Integer numeroConta) {
        Conta conta = contaRepository.findById(numeroConta)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        return ContaDTO.builder()
                .numeroConta(conta.getConta())
                .saldo(conta.getSaldo())
                .build();
    }

    public List<ContaDTO> listarContas() {
        List<ContaDTO> contaDTOs = new ArrayList<>();
        List<Conta> contas = contaRepository.findAll();
        for (Conta conta : contas) {
            contaDTOs.add(toDTO(conta));
        }
        return contaDTOs;
    }

    public ContaDTO realizarPagamento(TransacaoDTO transacaoDTO) {
        Conta conta = contaRepository.findById(transacaoDTO.getContaDebitada())
                .orElseThrow(ContaNaoEncontradaException::new);

        BigDecimal taxa = calcularTaxa(transacaoDTO.getFormaPagamento(), transacaoDTO.getValor());
        BigDecimal totalDebito = transacaoDTO.getValor().add(taxa);

        if (conta.getSaldo().compareTo(totalDebito) < 0) {
            throw new SaldoInsuficienteException();
        }

        conta.setSaldo(conta.getSaldo().subtract(totalDebito));
        Conta contaAtualizada = contaRepository.save(conta);

        return toDTO(contaAtualizada);
    }

    public void depositar(Integer numeroConta, BigDecimal valor) {
        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor do depósito deve ser positivo");
        }

        Conta conta = contaRepository.findByConta(numeroConta)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        conta.setSaldo(conta.getSaldo().add(valor));
        contaRepository.save(conta);
    }

    public void transferir(Integer contaOrigem, Integer contaDestino, BigDecimal valor) {
        if (contaOrigem.equals(contaDestino)) {
            throw new RuntimeException("Contas devem ser diferentes");
        }

        Conta origem = contaRepository.findByConta(contaOrigem)
                .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada"));
        Conta destino = contaRepository.findByConta(contaDestino)
                .orElseThrow(() -> new RuntimeException("Conta de destino não encontrada"));

        if (valor.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Valor da transferência deve ser positivo");
        }

        if (origem.getSaldo().compareTo(valor) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }

        origem.setSaldo(origem.getSaldo().subtract(valor));
        destino.setSaldo(destino.getSaldo().add(valor));

        contaRepository.save(origem);
        contaRepository.save(destino);
    }

    private BigDecimal calcularTaxa(String formaPagamento, BigDecimal valor) {
        switch (formaPagamento.toUpperCase()) {
            case "D":
                return valor.multiply(new BigDecimal("0.03"));
            case "C":
                return valor.multiply(new BigDecimal("0.05"));
            case "P":
                return BigDecimal.ZERO;
            default:
                throw new IllegalArgumentException("Forma de pagamento inválida: " + formaPagamento);
        }
    }

    private ContaDTO toDTO(Conta conta) {
        return ContaDTO.builder()
                .numeroConta(conta.getConta())
                .saldo(conta.getSaldo())
                .build();
    }
}

