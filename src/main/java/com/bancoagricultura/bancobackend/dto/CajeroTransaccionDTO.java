package com.bancoagricultura.bancobackend.dto;

import java.math.BigDecimal;

// DTO para recibir el JSON del Cajero con validacion de DUI
public class CajeroTransaccionDTO {

    private String numeroCuenta;
    private String duiCliente; // El DUI para validar
    private BigDecimal monto;

    // --- Getters y Setters ---
    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(String numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getDuiCliente() {
        return duiCliente;
    }

    public void setDuiCliente(String duiCliente) {
        this.duiCliente = duiCliente;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }
}