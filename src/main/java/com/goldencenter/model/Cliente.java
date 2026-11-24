package com.goldencenter.model;

public class Cliente {

    private int idCliente;
    private String nombreCompleto;
    private String documento;
    private String telefono;
    private String correo;
    private String estado;

    public Cliente() {
    }

    public Cliente(int idCliente, String nombreCompleto, String documento,
                   String telefono, String correo, String estado) {
        this.idCliente = idCliente;
        this.nombreCompleto = nombreCompleto;
        this.documento = documento;
        this.telefono = telefono;
        this.correo = correo;
        this.estado = estado;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Cliente{" +
                "idCliente=" + idCliente +
                ", nombreCompleto='" + nombreCompleto + '\'' +
                ", documento='" + documento + '\'' +
                ", telefono='" + telefono + '\'' +
                ", correo='" + correo + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}