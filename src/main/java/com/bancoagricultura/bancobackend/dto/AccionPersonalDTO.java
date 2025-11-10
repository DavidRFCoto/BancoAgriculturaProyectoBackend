package com.bancoagricultura.bancobackend.dto;

import com.bancoagricultura.bancobackend.entity.AccionPersonal;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AccionPersonalDTO {

    private Integer id;
    private String tipoAccion;
    private String estado;
    private String nombreNuevoEmpleado;
    private String puestoNuevoEmpleado;
    private BigDecimal salarioNuevoEmpleado;
    private String rolNombre;
    private String gerenteSolicitanteNombre;
    private LocalDateTime createdAt;

    public AccionPersonalDTO(AccionPersonal ap) {
        this.id = ap.getId();
        this.tipoAccion = ap.getTipoAccion();
        this.estado = ap.getEstado();
        this.nombreNuevoEmpleado = ap.getNombreNuevoEmpleado();
        this.puestoNuevoEmpleado = ap.getPuestoNuevoEmpleado();
        this.salarioNuevoEmpleado = ap.getSalarioNuevoEmpleado();

        this.rolNombre = ap.getRolNuevoEmpleado().getNombre();
        this.gerenteSolicitanteNombre = ap.getGerenteSolicitante().getNombre();
        this.createdAt = ap.getCreatedAt();
    }

    // --- Getters y Setters ---
    public Integer getId() {
        return id;
    }
    public String getTipoAccion() {
        return tipoAccion;
    }
    public String getEstado() {
        return estado;
    }
    public String getNombreNuevoEmpleado() {
        return nombreNuevoEmpleado;
    }
    public String getPuestoNuevoEmpleado() {
        return puestoNuevoEmpleado;
    }
    public BigDecimal getSalarioNuevoEmpleado() {
        return salarioNuevoEmpleado;
    }
    public String getRolNombre() {
        return rolNombre;
    }
    public String getGerenteSolicitanteNombre() {
        return gerenteSolicitanteNombre;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}