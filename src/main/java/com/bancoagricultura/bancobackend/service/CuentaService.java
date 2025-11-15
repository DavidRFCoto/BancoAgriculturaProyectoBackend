package com.bancoagricultura.bancobackend.service;

import com.bancoagricultura.bancobackend.entity.CuentaBancaria;
import java.math.BigDecimal;
import java.util.List;

public interface CuentaService {

    CuentaBancaria createCuenta(Integer clienteId, String tipoCuenta);

    List<CuentaBancaria> findCuentasByDui(String dui);

    void abonarEfectivo(String numeroCuenta, BigDecimal monto, Integer dependienteId);

    void retirarEfectivo(String numeroCuenta, BigDecimal monto, Integer dependienteId);

}