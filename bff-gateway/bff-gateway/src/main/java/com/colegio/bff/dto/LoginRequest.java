package com.colegio.bff.dto;

public class LoginRequest {

    private String rbd;
    private String username;
    private String password;

    public String getRbd() { return rbd; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public void setRbd(String rbd) { this.rbd = rbd; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
}