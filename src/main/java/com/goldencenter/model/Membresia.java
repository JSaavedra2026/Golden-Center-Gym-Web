package com.goldencenter.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Membresia {

    private int idMembresia;
    private String tipoMembresia;
    private int duracion;
    private BigDecimal costo;
    private LocalDate fechaInicio;
    private LocalDate fechaVencimiento;
    private String estado;
    private Cliente cliente;

    public Membresia() {
    }

    public Membresia(int idMembresia, String tipoMembresia, int duracion,
                     BigDecimal costo, LocalDate fechaInicio,
                     LocalDate fechaVencimiento, String estado, Cliente cliente) {
        this.idMembresia = idMembresia;
        this.tipoMembresia = tipoMembresia;
        this.duracion = duracion;
        this.costo = costo;
        this.fechaInicio = fechaInicio;
        this.fechaVencimiento = fechaVencimiento;
        this.estado = estado;
        this.cliente = cliente;
    }

    public int getIdMembresia() {
        return idMembresia;
    }

    public void setIdMembresia(int idMembresia) {
        this.idMembresia = idMembresia;
    }

    public String getTipoMembresia() {
        return tipoMembresia;
    }

    public void setTipoMembresia(String tipoMembresia) {
        this.tipoMembresia = tipoMembresia;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public BigDecimal getCosto() {
        return costo;
    }

    public void setCosto(BigDecimal costo) {
        this.costo = costo;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    @Override
    public String toString() {
        return "Membresia{" +
                "idMembresia=" + idMembresia +
                ", tipoMembresia='" + tipoMembresia + '\'' +
                ", duracion=" + duracion +
                ", costo=" + costo +
                ", fechaInicio=" + fechaInicio +
                ", fechaVencimiento=" + fechaVencimiento +
                ", estado='" + estado + '\'' +
                ", cliente=" + cliente +
                '}';
    }
}