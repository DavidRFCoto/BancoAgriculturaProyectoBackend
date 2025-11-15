package com.bancoagricultura.bancobackend.dto;

import com.bancoagricultura.bancobackend.entity.Dependiente;
import java.math.BigDecimal;

public class DependienteDTO {

    private Integer id;
    private String nombre;
    private BigDecimal comision;
    private String estado;
    private String rolNombre;

    public DependienteDTO(Dependiente dependiente) {
        this.id = dependiente.getId();
        this.nombre = dependiente.getNombre();
        this.comision = dependiente.getComision();
        this.estado = dependiente.getEstado();
        this.rolNombre = dependiente.getRol().getNombre(); // Evita el objeto Rol completo
    }

    // --- Getters y Setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getComision() { return comision; }
    public void setComision(BigDecimal comision) { this.comision = comision; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public String getRolNombre() { return rolNombre; }
    public void setRolNombre(String rolNombre) { this.rolNombre = rolNombre; }
}