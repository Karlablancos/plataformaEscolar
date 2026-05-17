package com.colegio.usuario.dto;

public class LoginRequest {

    private String username;
    private String password;
    private Integer idEstablecimiento;

    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public Integer getIdEstablecimiento() {
        return idEstablecimiento;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setIdEstablecimiento(Integer id) {
        this.idEstablecimiento = id;
    }
}