package com.bancoagricultura.bancobackend.dto;

import com.bancoagricultura.bancobackend.entity.Prestamo;
import java.math.BigDecimal;
import java.time.LocalDate;

// DTO para enviar la respuesta JSON de un prestamo
public class PrestamoDTO {

    private Integer id;
    private BigDecimal montoTotal;
    private LocalDate fechaAprobacion;
    private BigDecimal cuotaMensual;
    private Integer plazoEnMeses;

    private Integer criterioId;
    private String estadoNombre;
    private Integer clienteId;

    public BigDecimal getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(BigDecimal montoTotal) {
        this.montoTotal = montoTotal;
    }

    public LocalDate getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(LocalDate fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public BigDecimal getCuotaMensual() {
        return cuotaMensual;
    }

    public void setCuotaMensual(BigDecimal cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }

    public Integer getPlazoEnMeses() {
        return plazoEnMeses;
    }

    public void setPlazoEnMeses(Integer plazoEnMeses) {
        this.plazoEnMeses = plazoEnMeses;
    }

    public Integer getCriterioId() {
        return criterioId;
    }

    public void setCriterioId(Integer criterioId) {
        this.criterioId = criterioId;
    }

    public String getEstadoNombre() {
        return estadoNombre;
    }

    public void setEstadoNombre(String estadoNombre) {
        this.estadoNombre = estadoNombre;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }

    // Constructor que mapea la Entidad al DTO
    public PrestamoDTO(Prestamo prestamo) {
        this.id = prestamo.getId();
        this.montoTotal = prestamo.getMontoTotal();
        this.fechaAprobacion = prestamo.getFechaAprobacion();
        this.cuotaMensual = prestamo.getCuotaMensual();
        this.plazoEnMeses = prestamo.getPlazoEnMeses();

        this.criterioId = prestamo.getCriterio().getId();
        this.estadoNombre = prestamo.getEstado().getNombreEstado();
        this.clienteId = prestamo.getCliente().getId();
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
}