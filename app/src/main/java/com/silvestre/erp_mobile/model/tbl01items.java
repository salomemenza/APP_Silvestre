package com.silvestre.erp_mobile.model;

public class tbl01items {
    private String Codi;
    private String Codf;
    private String Descripcion;
    private Integer IdCategoria;
    private String Nombre;
    private String Marca;
    private Double Stoc;
    private Double Precio;
    private String UrlImagen;

    public String getCodi() {
        return Codi;
    }

    public void setCodi(String codi) {
        Codi = codi;
    }

    public String getCodf() {
        return Codf;
    }

    public void setCodf(String codf) {
        Codf = codf;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public Integer getIdCategoria() {
        return IdCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        IdCategoria = idCategoria;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }

    public Double getStoc() {
        return Stoc;
    }

    public void setStoc(Double stoc) {
        Stoc = stoc;
    }

    public Double getPrecio() {
        return Precio;
    }

    public void setPrecio(Double precio) {
        Precio = precio;
    }

    public String getUrlImagen() {
        return UrlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        UrlImagen = urlImagen;
    }
}
