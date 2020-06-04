package com.example.bescore.models;

import java.io.Serializable;

public class Estado implements Serializable {
    private String idEstado;
    private String estado;
    private String remitente;
    //private Date fecha;

    public Estado(){

    }

    public Estado(String idEstado, String estado, String remitente) {
        this.idEstado = idEstado;
        this.estado = estado;
        this.remitente = remitente;
        //this.fecha = fecha;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(String idEstado) {
        this.idEstado = idEstado;
    }

}
