package com.bancoagricultura.bancobackend.repository;

import com.bancoagricultura.bancobackend.entity.CuentaBancaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CuentaRepository extends JpaRepository<CuentaBancaria, Integer> {
    @Query("SELECT c FROM CuentaBancaria c JOIN FETCH c.cliente WHERE c.numeroCuenta = :numeroCuenta")
    Optional<CuentaBancaria> findByNumeroCuenta(@Param("numeroCuenta") String numeroCuenta);

    List<CuentaBancaria> findByClienteId(Integer clienteId);
}
