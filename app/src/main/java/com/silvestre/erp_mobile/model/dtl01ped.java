package com.silvestre.erp_mobile.model;

public class dtl01ped {
    private int IdPedidoLinea;
    private int IdPedido;
    private String Codf;
    private String Codi;
    private String Descr;
    private String Marc;
    private String Umed;
    private double Cant;
    private double Preu;
    private double Tota;
    private double Totn;

    public int getIdPedidoLinea() {
        return IdPedidoLinea;
    }

    public void setIdPedidoLinea(int idPedidoLinea) {
        IdPedidoLinea = idPedidoLinea;
    }

    public int getIdPedido() {
        return IdPedido;
    }

    public void setIdPedido(int idPedido) {
        IdPedido = idPedido;
    }

    public String getCodf() {
        return Codf;
    }

    public void setCodf(String codf) {
        Codf = codf;
    }

    public String getCodi() {
        return Codi;
    }

    public void setCodi(String codi) {
        Codi = codi;
    }

    public String getDescr() {
        return Descr;
    }

    public void setDescr(String desc) {
        Descr = desc;
    }

    public String getMarc() {
        return Marc;
    }

    public void setMarc(String marc) {
        Marc = marc;
    }

    public String getUmed() {
        return Umed;
    }

    public void setUmed(String umed) {
        Umed = umed;
    }

    public double getCant() {
        return Cant;
    }

    public void setCant(double cantidad) {
        Cant = cantidad;
    }

    public double getPreu() {
        return Preu;
    }

    public void setPreu(double preu) {
        Preu = preu;
    }

    public double getTota() {
        return Tota;
    }

    public void setTota(double tota) {
        Tota = tota;
    }

    public double getTotn() {
        return Totn;
    }

    public void setTotn(double totn) {
        Totn = totn;
    }
}
