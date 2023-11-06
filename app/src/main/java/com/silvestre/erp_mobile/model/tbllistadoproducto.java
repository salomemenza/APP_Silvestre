package com.silvestre.erp_mobile.model;

public class tbllistadoproducto {
    public String Codi;
    public String Codf;
    public String Marca;
    public String Descripcion;
    public String UMedida;
    public int Stock;
    public double Precio;
    public String Moneda;
    private double Cantidad;
    private double Subtotal;
/*
    public tbllistadoproducto(String Codi,String Codf,String Marca,String Descripcion,String UMedida,int Stock,double Precio,String Moneda)
    {
        this.Codi=Codi;
        this.Codf=Codf;
        this.Marca=Marca;
        this.Descripcion=Descripcion;
        this.UMedida=UMedida;
        this.Stock=Stock;
        this.Precio=Precio;
        this.Moneda=Moneda;
    }
    */
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

    public String getMarca() {
        return Marca;
    }

    public void setMarca(String marca) {
        Marca = marca;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getUMedida() {
        return UMedida;
    }

    public void setUMedida(String UMedida) {
        this.UMedida = UMedida;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }

    public double getPrecio() {
        return Precio;
    }

    public void setPrecio(double precio) {
        Precio = precio;
    }

    public String getMoneda() {
        return Moneda;
    }

    public void setMoneda(String moneda) {
        Moneda = moneda;
    }

    public double getCantidad() {
        return Cantidad;
    }

    public void setCantidad(double cantidad) {
        Cantidad = cantidad;
    }

    public double getSubtotal() {
        return Subtotal;
    }

    public void setSubtotal(double subtotal) {
        Subtotal = subtotal;
    }
}
