package com.bancoagricultura.bancobackend.dto;

import java.math.BigDecimal;

// DTO para recibir el JSON cuando el cajero crea un prestamo
public class PrestamoSolicitudDTO {

    private Integer clienteId;
    private Integer cajeroId; // El ID del empleado que esta logueado
    private BigDecimal monto;

    // --- Getters y Setters ---
    public Integer getClienteId() { return clienteId; }
    public void setClienteId(Integer clienteId) { this.clienteId = clienteId; }
    public Integer getCajeroId() { return cajeroId; }
    public void setCajeroId(Integer cajeroId) { this.cajeroId = cajeroId; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
}