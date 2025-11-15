package com.bancoagricultura.bancobackend.dto;

import java.math.BigDecimal;

public class ClienteRegistroDTO {
    private String nombre;
    private String apellidos;
    private String genero;
    private String dui;
    private String direccion;
    private BigDecimal salario;
    private String password;

    // --- Getters y Setters ---
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public String getDui() { return dui; }
    public void setDui(String dui) { this.dui = dui; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}