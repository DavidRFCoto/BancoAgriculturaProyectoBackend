package com.bancoagricultura.bancobackend.dto;

import com.bancoagricultura.bancobackend.entity.Cliente;
import java.math.BigDecimal;

public class ClienteDTO {

    private Integer id;
    private String nombre;
    private String dui;
    private BigDecimal salario;

    public ClienteDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nombre = cliente.getNombre();
        this.dui = cliente.getDui();
        this.salario = cliente.getSalario();
    }

    // --- Getters y Setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDui() { return dui; }
    public void setDui(String dui) { this.dui = dui; }
    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }
}