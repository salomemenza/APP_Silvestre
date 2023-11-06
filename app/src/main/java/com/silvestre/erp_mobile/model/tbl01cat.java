package com.silvestre.erp_mobile.model;

public class tbl01cat {
    private int IdCategoria;
    private String NomCategoria;
    private int Activo;
    private byte[] Imagen;
    private String Ruc;
    private String UrlImagen;

    public int getIdCategoria() {
        return IdCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        IdCategoria = idCategoria;
    }

    public String getNomCategoria() {
        return NomCategoria;
    }

    public void setNomCategoria(String nomCategoria) {
        NomCategoria = nomCategoria;
    }

    public int getActivo() {
        return Activo;
    }

    public void setActivo(int activo) {
        Activo = activo;
    }

    public byte[] getImagen() {
        return Imagen;
    }

    public void setImagen(byte[] imagen) {
        Imagen = imagen;
    }

    public String getRuc() {
        return Ruc;
    }

    public void setRuc(String ruc) {
        Ruc = ruc;
    }

    public String getUrlImagen() {
        return UrlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        UrlImagen = urlImagen;
    }
}
