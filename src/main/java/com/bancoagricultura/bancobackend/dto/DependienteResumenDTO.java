package com.bancoagricultura.bancobackend.dto;

import java.math.BigDecimal;

public class DependienteResumenDTO {

    private Integer id;
    private String nombre;
    private BigDecimal comisionAcumulada;
    private BigDecimal porcentajeComision;

    public DependienteResumenDTO(Integer id, String nombre, BigDecimal comisionAcumulada, BigDecimal porcentajeComision) {
        this.id = id;
        this.nombre = nombre;
        this.comisionAcumulada = comisionAcumulada;
        this.porcentajeComision = porcentajeComision;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public BigDecimal getComisionAcumulada() {
        return comisionAcumulada;
    }

    public BigDecimal getPorcentajeComision() {
        return porcentajeComision;
    }
}

