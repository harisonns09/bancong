package com.bancong.bancong.controller;

import com.bancong.bancong.dto.ContaDTO;
import com.bancong.bancong.dto.TransacaoDTO;
import com.bancong.bancong.service.ContaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/transacao")
public class TransacaoController {

    private final ContaService contaService;

    public TransacaoController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<ContaDTO> realizarPagamento(@RequestBody TransacaoDTO transacaoDTO) {
        ContaDTO conta = contaService.realizarPagamento(transacaoDTO);
        return ResponseEntity.status(201).body(conta);
    }

    @PostMapping("/deposito")
    public ResponseEntity<String> depositar(@RequestBody TransacaoDTO dto) {
        contaService.depositar(dto.getContaDestino(), dto.getValor());
        return ResponseEntity.ok("Depósito realizado com sucesso.");
    }

    @PostMapping("/transferencia")
    public ResponseEntity<String> transferir(@RequestBody TransacaoDTO dto) {
        contaService.transferir(dto.getContaOrigem(), dto.getContaDestino(), dto.getValor());
        return ResponseEntity.ok("Transferência realizada com sucesso.");
    }
}