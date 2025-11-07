package com.bancoagricultura.bancobackend.repository;

import com.bancoagricultura.bancobackend.entity.CuentaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CuentaRepository extends JpaRepository<CuentaBancaria, Integer> {
    Optional<CuentaBancaria> findByNumeroCuenta(String numeroCuenta);
    List<CuentaBancaria> findByClienteId(Integer clienteId);
}
