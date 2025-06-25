package org.javadominicano.dto;

import java.time.LocalDate;

public class ReporteGenerado {
    private int id;
    private String titulo;
    private String estacion;
    private LocalDate fecha;
    private String tipo;

    public ReporteGenerado() {
    }

    public ReporteGenerado(int id, String titulo, String estacion, LocalDate fecha, String tipo) {
        this.id = id;
        this.titulo = titulo;
        this.estacion = estacion;
        this.fecha = fecha;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}