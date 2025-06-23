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
        ContaDTO conta = contaService.realizarTransacao(transacaoDTO);
        return ResponseEntity.status(201).body(conta);
    }

    // @PostMapping
    // public ResponseEntity<ContaDTO> depositar(@RequestBody ContaDTO contaDTO) {
    //     ContaDTO novaConta = contaService.criarConta(contaDTO);
    //     return ResponseEntity.status(201).body(novaConta);
    // }

}
