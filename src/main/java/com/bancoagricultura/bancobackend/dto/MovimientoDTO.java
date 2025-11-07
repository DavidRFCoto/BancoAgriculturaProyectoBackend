package com.bancoagricultura.bancobackend.dto;

import com.bancoagricultura.bancobackend.entity.Movimiento;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoDTO {

    private Integer id;
    private String tipoMovimiento;
    private BigDecimal monto;
    private LocalDateTime fecha;
    private String descripcion;

    // Datos de la cuenta y cliente (aplanados)
    private String numeroCuenta;
    private String clienteDui;
    private String clienteNombre;

    public MovimientoDTO(Movimiento movimiento) {
        this.id = movimiento.getId();
        this.tipoMovimiento = movimiento.getTipoMovimiento();
        this.monto = movimiento.getMonto();
        this.fecha = movimiento.getFecha();
        this.descripcion = movimiento.getDescripcion();


        this.numeroCuenta = movimiento.getCuenta().getNumeroCuenta();
        this.clienteDui = movimiento.getCuenta().getCliente().getDui();
        this.clienteNombre = movimiento.getCuenta().getCliente().getNombre();
    }

    // --- Getters y Setters ---
    public Integer getId() { return id; }
    public String getTipoMovimiento() { return tipoMovimiento; }
    public BigDecimal getMonto() { return monto; }
    public LocalDateTime getFecha() { return fecha; }
    public String getDescripcion() { return descripcion; }
    public String getNumeroCuenta() { return numeroCuenta; }
    public String getClienteDui() { return clienteDui; }
    public String getClienteNombre() { return clienteNombre; }
}