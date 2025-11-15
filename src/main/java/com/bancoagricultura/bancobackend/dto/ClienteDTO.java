package com.bancoagricultura.bancobackend.dto;

import com.bancoagricultura.bancobackend.entity.Cliente;
import java.math.BigDecimal;

public class ClienteDTO {

    private Integer id;
    private String nombre;
    private String apellidos;
    private String dui;
    private String genero;
    private BigDecimal salario;

    public ClienteDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nombre = cliente.getNombre();
        this.apellidos = cliente.getApellidos();
        this.dui = cliente.getDui();
        this.genero = cliente.getGenero();
        this.salario = cliente.getSalario();
    }

    // --- Getters y Setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getDui() { return dui; }
    public void setDui(String dui) { this.dui = dui; }
    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }
    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }
}