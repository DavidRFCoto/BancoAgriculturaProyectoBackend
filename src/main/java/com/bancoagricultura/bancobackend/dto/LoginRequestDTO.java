package com.bancoagricultura.bancobackend.dto;

public class LoginRequestDTO {
    private String usuario;
    private String password;

    public LoginRequestDTO() {
    }

    public LoginRequestDTO(String usuario, String password) {
        this.usuario = usuario;
        this.password = password;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

