package com.bancoagricultura.bancobackend.dto;

import java.math.BigDecimal;

// DTO para registrar un nuevo empleado (Gerente -> Cajero)
public class EmpleadoRegistroDTO {

    private String nombre;
    private String puesto;
    private BigDecimal salario;
    private Integer rolId; // El ID del Rol

    // --- Getters y Setters ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getPuesto() { return puesto; }
    public void setPuesto(String puesto) { this.puesto = puesto; }

    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }

    public Integer getRolId() { return rolId; }
    public void setRolId(Integer rolId) { this.rolId = rolId; }
}