package com.bancong.bancong.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(ContaNaoEncontradaException.class)
    public ResponseEntity<String> handleContaNaoEncontradaException(ContaNaoEncontradaException ex) {
        return ResponseEntity.status(404).build();
    }

    @ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity<String> handleSaldoInsuficienteException(SaldoInsuficienteException ex) {
        return ResponseEntity.status(404).build();
    }

    @ExceptionHandler(ContaJaExisteException.class)
    public ResponseEntity<String> handleContaJaExisteException(ContaJaExisteException ex) {
        return ResponseEntity.status(409).build();
    }

    


    
}
