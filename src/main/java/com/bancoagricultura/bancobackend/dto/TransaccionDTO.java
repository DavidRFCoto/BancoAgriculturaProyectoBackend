package com.bancoagricultura.bancobackend.dto;

import java.math.BigDecimal;

public class TransaccionDTO {
    private String numeroCuenta;
    private BigDecimal monto;
    private Integer dependienteId;

    // --- Getters y Setters ---
    public String getNumeroCuenta() {
        return numeroCuenta;
    }
    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }
    public BigDecimal getMonto() {
        return monto;
    }
    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
    public Integer getDependienteId() {
        return dependienteId;
    }
    public void setDependienteId(Integer dependienteId) {
        this.dependienteId = dependienteId;
    }
}