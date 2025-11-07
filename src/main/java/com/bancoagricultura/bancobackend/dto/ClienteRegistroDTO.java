package com.bancoagricultura.bancobackend.dto;

import java.math.BigDecimal;

public class ClienteRegistroDTO {
    private String nombre;
    private String dui;
    private BigDecimal salario;

    // --- Getters y Setters ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDui() { return dui; }
    public void setDui(String dui) { this.dui = dui; }
    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }
}