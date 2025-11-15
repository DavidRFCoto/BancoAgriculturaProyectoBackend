package com.bancoagricultura.bancobackend.dto;

import com.bancoagricultura.bancobackend.entity.Empleado;
import java.math.BigDecimal;

// DTO para devolver un Empleado como JSON
public class EmpleadoDTO {

    private Integer id;
    private String nombre;
    private String puesto;
    private BigDecimal salario;
    private String estado;
    private String rolNombre; // Solo el nombre del rol

    public EmpleadoDTO(Empleado empleado) {
        this.id = empleado.getId();
        this.nombre = empleado.getNombre();
        this.puesto = empleado.getPuesto();
        this.salario = empleado.getSalario();
        this.estado = empleado.getEstado();
        this.rolNombre = empleado.getRol().getNombre(); // Evita el objeto Rol completo
    }

    // --- Getters y Setters ---
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRolNombre() {
        return rolNombre;
    }

    public void setRolNombre(String rolNombre) {
        this.rolNombre = rolNombre;
    }
}