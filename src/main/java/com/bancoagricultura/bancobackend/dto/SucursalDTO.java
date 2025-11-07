package com.bancoagricultura.bancobackend.dto;

import com.bancoagricultura.bancobackend.entity.Sucursal;

public class SucursalDTO {
    private Integer id;
    private String nombre;
    private String direccion;

    private String gerenteNombre;

    public SucursalDTO(Sucursal sucursal) {
        this.id = sucursal.getId();
        this.nombre = sucursal.getNombre();
        this.direccion = sucursal.getDireccion();

        if (sucursal.getGerente() != null) {
            this.gerenteNombre = sucursal.getGerente().getNombre();
        } else {
            this.gerenteNombre = null;
        }
    }

    // --- Getters y Setters ---
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
    public String getDireccion() {
        return direccion; }
    public void setDireccion(String direccion) {
        this.direccion = direccion; }

    public String getGerenteNombre() {
        return gerenteNombre;
    }

    public void setGerenteNombre(String gerenteNombre) {
        this.gerenteNombre = gerenteNombre;
    }
}