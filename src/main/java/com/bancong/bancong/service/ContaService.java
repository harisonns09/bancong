package com.bancong.bancong.service;

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

        // if (contaRepository.existsById(dto.getNumeroConta())) {
        // throw new ContaJaExisteException();
        // }

        Conta conta = new Conta();
        conta.setConta(contaDTO.getNumeroConta());
        conta.setSaldo(contaDTO.getSaldo());

        Conta contaSalva = contaRepository.save(conta);

        // Cria e retorna DTO atualizado
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

    public ContaDTO realizarTransacao(TransacaoDTO transacaoDTO) {
        Conta conta = contaRepository.findById(transacaoDTO.getContaDestino())
                .orElseThrow(ContaNaoEncontradaException::new);

        double taxa = calcularTaxa(transacaoDTO.getFormaPagamento(), transacaoDTO.getValor());
        double totalDebito = transacaoDTO.getValor() + taxa;

        if (conta.getSaldo() < totalDebito) {
            throw new SaldoInsuficienteException();
        }

        conta.setSaldo(conta.getSaldo() - totalDebito);
        Conta contaAtualizada = contaRepository.save(conta);

        return toDTO(contaAtualizada);
    }

    private double calcularTaxa(String formaPagamento, double valor) {
        switch (formaPagamento.toUpperCase()) {
            case "D":
                return valor * 0.03;
            case "C":
                return valor * 0.05;
            case "P":
                return 0.0;
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
