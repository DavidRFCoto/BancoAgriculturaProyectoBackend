package com.bancoagricultura.bancobackend.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes", uniqueConstraints = {
        @UniqueConstraint(name = "ux_clientes_dui", columnNames = {"dui"}),
        @UniqueConstraint(name = "ux_clientes_usuario", columnNames = {"usuario"})
})
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    private Integer id;

    @Column(name = "nombre", nullable = false, length = 200)
    private String nombre;

    @Column(name = "apellidos", length = 200)
    private String apellidos;

    @Column(name = "dui", nullable = false, length = 20)
    private String dui;

    @Column(name = "usuario", unique = true, length = 50)
    private String usuario;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "direccion", length = 255)
    private String direccion;

    @Column(name = "correo", length = 150)
    private String correo;

    @Column(name = "genero", length = 20)
    private String genero;

    @Column(name = "salario", precision = 15, scale = 2)
    private BigDecimal salario = BigDecimal.ZERO;

    @Column(name = "estado_civil", length = 50)
    private String estadoCivil;

    @Column(name = "fecha_nacimiento")
    private LocalDate fechaNacimiento;

    @Column(name = "activo", nullable = false)
    private Boolean activo = Boolean.TRUE;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "cliente", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = false, fetch = FetchType.LAZY)
    private List<CuentaBancaria> cuentas = new ArrayList<>();

    public Cliente() {}

    public Cliente(String nombre, String dui) {
        this.nombre = nombre;
        this.dui = dui;
    }

    // --- Lifecycle callbacks para timestamps ---
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
    // --- fin callbacks ---

    // Getters / Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getDui() { return dui; }
    public void setDui(String dui) { this.dui = dui; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public BigDecimal getSalario() { return salario; }
    public void setSalario(BigDecimal salario) { this.salario = salario; }

    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }

    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public Boolean getActivo() { return activo; }
    public void setActivo(Boolean activo) { this.activo = activo; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public List<CuentaBancaria> getCuentas() { return cuentas; }
    public void setCuentas(List<CuentaBancaria> cuentas) { this.cuentas = cuentas; }

    // helpers para relación bidireccional
    public void addCuenta(CuentaBancaria cuenta) {
        cuentas.add(cuenta);
        cuenta.setCliente(this);
    }

    public void removeCuenta(CuentaBancaria cuenta) {
        cuentas.remove(cuenta);
        cuenta.setCliente(null);
    }
}
