package com.example.bescore.models;

import java.io.Serializable;

public class Mensaje implements Serializable {
    private String idEstado;
    private String remitente;
    private String texto;

    public Mensaje(){

    }

    public Mensaje(String idEstado, String remitente, String texto) {
        this.idEstado = idEstado;
        this.remitente = remitente;
        this.texto = texto;
    }

    public String getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(String idEstado) {
        this.idEstado = idEstado;
    }

    public String getRemitente() {
        return remitente;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
