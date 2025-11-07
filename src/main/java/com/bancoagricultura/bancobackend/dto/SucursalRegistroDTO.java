package com.bancoagricultura.bancobackend.dto;

public class SucursalRegistroDTO {
    private String nombre;
    private String direccion;

    // --- Getters y Setters ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}