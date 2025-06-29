package org.javadominicano.dto;

import java.util.Date;

public class AlertaActivaDTO {
    private Long id;
    private String nombre;
    private double valorActual;
    private double umbral;
    private String operador;
    private String prioridad;
    private boolean activa;
    private Date fechaActivacion;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getValorActual() { return valorActual; }
    public void setValorActual(double valorActual) { this.valorActual = valorActual; }

    public double getUmbral() { return umbral; }
    public void setUmbral(double umbral) { this.umbral = umbral; }

    public String getOperador() { return operador; }
    public void setOperador(String operador) { this.operador = operador; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    public Date getFechaActivacion() { return fechaActivacion; }
    public void setFechaActivacion(Date fechaActivacion) { this.fechaActivacion = fechaActivacion; }
}
