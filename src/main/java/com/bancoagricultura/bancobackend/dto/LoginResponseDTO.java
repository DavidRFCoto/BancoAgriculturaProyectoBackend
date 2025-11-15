package com.bancoagricultura.bancobackend.dto;

public class LoginResponseDTO {
    private Integer id;
    private String nombre;
    private String rol;
    private Integer rolId;
    private Integer sucursalId;
    private Integer clienteId;

    public LoginResponseDTO() {
    }

    public LoginResponseDTO(Integer id, String nombre, String rol, Integer rolId, Integer sucursalId) {
        this.id = id;
        this.nombre = nombre;
        this.rol = rol;
        this.rolId = rolId;
        this.sucursalId = sucursalId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public Integer getRolId() {
        return rolId;
    }

    public void setRolId(Integer rolId) {
        this.rolId = rolId;
    }

    public Integer getSucursalId() {
        return sucursalId;
    }

    public void setSucursalId(Integer sucursalId) {
        this.sucursalId = sucursalId;
    }

    public Integer getClienteId() {
        return clienteId;
    }

    public void setClienteId(Integer clienteId) {
        this.clienteId = clienteId;
    }
}

