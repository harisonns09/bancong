package com.bancong.bancong.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.bancong.bancong.dto.ContaDTO;
import com.bancong.bancong.service.ContaService;

public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    public ResponseEntity<ContaDTO> criarConta(@RequestBody ContaDTO contaDTO) {
        ContaDTO novaConta = contaService.criarConta(contaDTO);
        return ResponseEntity.status(201).body(novaConta);
    }

    @GetMapping
    public ResponseEntity<ContaDTO> consultar(@RequestParam("numero_conta") Integer numeroConta) {
        ContaDTO conta = contaService.consultarConta(numeroConta);
        return ResponseEntity.ok(conta);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ContaDTO>> listarContas() {
        List<ContaDTO> contas = contaService.listarContas();
        return ResponseEntity.ok(contas);
    }
}
