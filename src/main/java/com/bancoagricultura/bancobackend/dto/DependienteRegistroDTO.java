package com.bancoagricultura.bancobackend.dto;

public class DependienteRegistroDTO {

    private String nombre;
    private Integer rolId; // El ID del Rol "Dependiente"

    // --- Getters y Setters ---
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }
}