package com.bancoagricultura.bancobackend.dto;

import java.math.BigDecimal;

public class AccionPersonalRegistroDTO {

    private String nombre;
    private String puesto;
    private BigDecimal salario;
    private Integer rolId;
    private Integer gerenteSolicitanteId; // ID del gerente que hace la solicitud)

    // --- Getters y Setters ---
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getPuesto() {
        return puesto;
    }
    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }
    public BigDecimal getSalario() {
        return salario;
    }
    public void setSalario(BigDecimal salario) {
        this.salario = salario;
    }
    public Integer getRolId() {
        return rolId;
    }
    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }
    public Integer getGerenteSolicitanteId() {
        return gerenteSolicitanteId;
    }
    public void setGerenteSolicitanteId(Integer gerenteSolicitanteId) {
        this.gerenteSolicitanteId = gerenteSolicitanteId;
    }
}