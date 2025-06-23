package com.bancong.bancong.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bancong.bancong.model.Conta;


public interface ContaRepository extends JpaRepository<Conta, Integer> {
    Optional<Conta> findByConta(Integer conta);
}
