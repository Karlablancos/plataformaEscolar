package com.colegio.bff.dto;

public class LoginResponse {

    private String token;
    private String username;
    private String rol;
    private String nombreColegio;
    private String rbd;
    private Integer idEstablecimiento;

    public LoginResponse(String token, String username,
                         String rol, String nombreColegio,
                         String rbd, Integer idEstablecimiento) {
        this.token = token;
        this.username = username;
        this.rol = rol;
        this.nombreColegio = nombreColegio;
        this.rbd = rbd;
        this.idEstablecimiento = idEstablecimiento;
    }

    public String getToken() { return token; }
    public String getUsername() { return username; }
    public String getRol() { return rol; }
    public String getNombreColegio() { return nombreColegio; }
    public String getRbd() { return rbd; }
    public Integer getIdEstablecimiento() { return idEstablecimiento; }
}
