package com.silvestre.erp_mobile.model;

public class tblusuario {
    private int CodUsuario;
    private String NomUsuario;
    private String LoginUsuario;
    private String Contrasena;
    private int Activo;
    private String Email;
    private String Ruc;

    public int getCodUsuario() {
        return CodUsuario;
    }

    public void setCodUsuario(int codUsuario) {
        CodUsuario = codUsuario;
    }

    public String getNomUsuario() {
        return NomUsuario;
    }

    public void setNomUsuario(String nomUsuario) {
        NomUsuario = nomUsuario;
    }

    public String getLoginUsuario() {
        return LoginUsuario;
    }

    public void setLoginUsuario(String loginUsuario) {
        LoginUsuario = loginUsuario;
    }

    public String getContrasena() {
        return Contrasena;
    }

    public void setContrasena(String contrasena) {
        Contrasena = contrasena;
    }

    public int getActivo() {
        return Activo;
    }

    public void setActivo(int activo) {
        Activo = activo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getRuc() {
        return Ruc;
    }

    public void setRuc(String ruc) {
        Ruc = ruc;
    }
}
