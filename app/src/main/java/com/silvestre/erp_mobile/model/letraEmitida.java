package com.silvestre.erp_mobile.model;

public class letraEmitida {
    public Integer id;
    public Integer idSeguimiento;
    public Integer idletra;
    public String nroLetra;
    public Integer nroCuota;
    public Integer idFinanciamiento;
    public String idAceptante;
    public String importe;
    public String emision;
    public String vencimiento;
    public boolean estado;
    public boolean flagRecepcionado;
    public String fechaRegistro;
    public String fechaAceptacion;

    public letraEmitida() {
    }

    public letraEmitida(Integer idSeguimiento, Integer idletra, String nroLetra, Integer nroCuota, Integer idFinanciamiento, String idAceptante, String importe, String emision, String vencimiento, boolean estado, boolean flagRecepcionado, String fechaRegistro, String fechaAceptacion) {

        this.idSeguimiento = idSeguimiento;
        this.idletra = idletra;
        this.nroLetra = nroLetra;
        this.nroCuota = nroCuota;
        this.idFinanciamiento = idFinanciamiento;
        this.idAceptante = idAceptante;
        this.importe = importe;
        this.emision = emision;
        this.vencimiento = vencimiento;
        this.estado = estado;
        this.flagRecepcionado = flagRecepcionado;
        this.fechaRegistro = fechaRegistro;
        this.fechaAceptacion = fechaAceptacion;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdSeguimiento() {
        return idSeguimiento;
    }

    public void setIdSeguimiento(Integer idSeguimiento) {
        this.idSeguimiento = idSeguimiento;
    }

    public Integer getIdletra() {
        return idletra;
    }

    public void setIdletra(Integer idletra) {
        this.idletra = idletra;
    }

    public String getNroLetra() {
        return nroLetra;
    }

    public void setNroLetra(String nroLetra) {
        this.nroLetra = nroLetra;
    }
    public Integer getNroCuota() {
        return nroCuota;
    }

    public void setNroCuota(Integer nroCuota) {
        this.nroCuota = nroCuota;
    }

    public Integer getIdFinanciamiento() {
        return idFinanciamiento;
    }

    public void setIdFinanciamiento(Integer idFinanciamiento) {
        this.idFinanciamiento = idFinanciamiento;
    }

    public String getIdAceptante() {
        return idAceptante;
    }

    public void setIdAceptante(String idAceptante) {
        this.idAceptante = idAceptante;
    }

    public String getImporte() {
        return importe;
    }

    public void setImporte(String importe) {
        this.importe = importe;
    }

    public String getEmision() {
        return emision;
    }

    public void setEmision(String emision) {
        this.emision = emision;
    }

    public String getVencimiento() {
        return vencimiento;
    }

    public void setVencimiento(String vencimiento) {
        this.vencimiento = vencimiento;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public boolean isFlagRecepcionado() {
        return flagRecepcionado;
    }

    public void setFlagRecepcionado(boolean flagRecepcionado) {
        this.flagRecepcionado = flagRecepcionado;
    }

    public String getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(String fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getFechaAceptacion() {
        return fechaAceptacion;
    }

    public void setFechaAceptacion(String fechaAceptacion) {
        this.fechaAceptacion = fechaAceptacion;
    }
}
