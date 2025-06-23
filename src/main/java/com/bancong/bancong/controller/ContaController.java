package com.bancong.bancong.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bancong.bancong.dto.ContaDTO;
import com.bancong.bancong.service.ContaService;

@RestController
@RequestMapping("/conta")
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
    public ResponseEntity<ContaDTO> consultar(@RequestParam Integer numero_conta) {
        ContaDTO conta = contaService.consultarConta(numero_conta);
        return ResponseEntity.ok(conta);
    }

    @GetMapping("/listar")
    public ResponseEntity<List<ContaDTO>> listarContas() {
        List<ContaDTO> contas = contaService.listarContas();
        return ResponseEntity.ok(contas);
    }
}
