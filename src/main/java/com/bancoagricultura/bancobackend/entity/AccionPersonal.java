package com.bancoagricultura.bancobackend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

// Esta entidad almacena la solicitud de un gerente para contratar
//un nuevo empleado. El Gerente General la aprueba o rechaza
@Entity
@Table(name = "accionespersonal")
public class AccionPersonal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_accion")
    private Integer id;

    @Column(name = "tipo_accion", nullable = false, length = 100)
    private String tipoAccion;

    // "pendiente", "aprobado", "rechazado")
    @Column(name = "estado", nullable = false, length = 50)
    private String estado;

    //  Guardamos los datos del empleado a crear)
    @Column(name = "nombre_nuevo_empleado", nullable = false, length = 200)
    private String nombreNuevoEmpleado;

    @Column(name = "puesto_nuevo_empleado", nullable = false, length = 100)
    private String puestoNuevoEmpleado;

    @Column(name = "salario_nuevo_empleado", precision = 15, scale = 2)
    private BigDecimal salarioNuevoEmpleado;

    //  Relacion con el Rol que tendra el nuevo empleado)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rol_nuevo_empleado", nullable = false)
    private Rol rolNuevoEmpleado;

    // Relacion con el Gerente que solicita la accion)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_gerente_solicitante", nullable = false)
    private Empleado gerenteSolicitante;

    // Timestamps de auditoria
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public AccionPersonal() {
    }

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        this.updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // --- Getters y Setters ---

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipoAccion() {
        return tipoAccion;
    }

    public void setTipoAccion(String tipoAccion) {
        this.tipoAccion = tipoAccion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNombreNuevoEmpleado() {
        return nombreNuevoEmpleado;
    }

    public void setNombreNuevoEmpleado(String nombreNuevoEmpleado) {
        this.nombreNuevoEmpleado = nombreNuevoEmpleado;
    }

    public String getPuestoNuevoEmpleado() {
        return puestoNuevoEmpleado;
    }

    public void setPuestoNuevoEmpleado(String puestoNuevoEmpleado) {
        this.puestoNuevoEmpleado = puestoNuevoEmpleado;
    }

    public BigDecimal getSalarioNuevoEmpleado() {
        return salarioNuevoEmpleado;
    }

    public void setSalarioNuevoEmpleado(BigDecimal salarioNuevoEmpleado) {
        this.salarioNuevoEmpleado = salarioNuevoEmpleado;
    }

    public Rol getRolNuevoEmpleado() {
        return rolNuevoEmpleado;
    }

    public void setRolNuevoEmpleado(Rol rolNuevoEmpleado) {
        this.rolNuevoEmpleado = rolNuevoEmpleado;
    }

    public Empleado getGerenteSolicitante() {
        return gerenteSolicitante;
    }

    public void setGerenteSolicitante(Empleado gerenteSolicitante) {
        this.gerenteSolicitante = gerenteSolicitante;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}