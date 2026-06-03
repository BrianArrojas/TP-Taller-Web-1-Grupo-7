package com.tallerwebi.presentacion.dto;

public class DatosRegistroDTO {
    private String nombre;
    private String apellido;
    private String telefono;
    private String mail;
    private String password;
    private String confirmarPassword;

    public DatosRegistroDTO() {}

    public DatosRegistroDTO(String mail, String password, String confirmarPassword) {
        this.mail = mail;
        this.password = password;
        this.confirmarPassword = confirmarPassword;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getConfirmarPassword() {
        return confirmarPassword;
    }

    public void setConfirmarPassword(String confirmarPassword) {
        this.confirmarPassword = confirmarPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }
}
