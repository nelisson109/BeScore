package com.example.bescore.models;

public class Usuario {
    private String nombre;
    private String apellidos;
    private String usuario;
    private String idTablon;
    private String descripcion;


    public Usuario() {

    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getIdTablon() {
        return idTablon;
    }

    public void setIdTablon(String idTablon) {
        this.idTablon = idTablon;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
