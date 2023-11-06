package com.silvestre.erp_mobile.model;

public class letraEmitidaResumen {
    public String idSeguimiento;
    public String idVendedor;
    public String usuario;
    public String latitud;
    public String longitud;
    public String observacion;
    public String fechaRegistro;
    public String fotografia;


    public letraEmitidaResumen() {
    }

    public letraEmitidaResumen(String idSeguimiento, String idVendedor, String usuario, String latitud, String longitud, String observacion, String fechaRegistro, String fotografia) {
        this.idSeguimiento = idSeguimiento;
        this.idVendedor = idVendedor;
        this.usuario = usuario;
        this.latitud = latitud;
        this.longitud = longitud;
        this.observacion = observacion;
        this.fechaRegistro = fechaRegistro;
        this.fotografia = fotografia;
    }

    public String getIdSeguimiento() {
        return idSeguimiento;
    }

    public void setIdSeguimiento(String idSeguimiento) {
        this.idSeguimiento = idSeguimiento;
    }

    public String getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(String idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getFotografia() {
        return fotografia;
    }

    public void setFotografia(String fotografia) {
        this.fotografia = fotografia;
    }
}
