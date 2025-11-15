package com.bancoagricultura.bancobackend.dto;

import com.bancoagricultura.bancobackend.entity.CuentaBancaria;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CuentaDTO {

    private Integer id;
    private String numeroCuenta;
    private String tipoCuenta;
    private BigDecimal saldo;
    private LocalDateTime fechaApertura;
    private Integer clienteId;

    public CuentaDTO(CuentaBancaria cuenta) {
        this.id = cuenta.getId();
        this.numeroCuenta = cuenta.getNumeroCuenta();
        this.tipoCuenta = cuenta.getTipoCuenta();
        this.saldo = cuenta.getSaldo();
        this.fechaApertura = cuenta.getFechaApertura();
        this.clienteId = cuenta.getCliente().getId();
    }

    // --- Getters y Setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public void setNumeroCuenta(String numeroCuenta) { this.numeroCuenta = numeroCuenta; }

    public String getTipoCuenta() { return tipoCuenta; }
    public void setTipoCuenta(String tipoCuenta) { this.tipoCuenta = tipoCuenta; }
    public BigDecimal getSaldo() { return saldo; }
    public void setSaldo(BigDecimal saldo) { this.saldo = saldo; }
    public LocalDateTime getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(LocalDateTime fechaApertura) { this.fechaApertura = fechaApertura; }
    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }
}