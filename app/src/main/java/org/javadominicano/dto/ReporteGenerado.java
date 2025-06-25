package org.javadominicano.dto;

public class ReporteGenerado {
    private String titulo;
    private String estacion;

    public ReporteGenerado() {
    }

    public ReporteGenerado(String titulo, String estacion) {
        this.titulo = titulo;
        this.estacion = estacion;
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
}
