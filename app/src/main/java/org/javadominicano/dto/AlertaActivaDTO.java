package org.javadominicano.dto;

import java.util.Date;

public class AlertaActivaDTO {
    private Long id;
    private String nombre;
    private double umbral;
    private boolean activa;
    private String operador;
    private String prioridad;
    private Double valorActual;
    private Date fecha;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public double getUmbral() { return umbral; }
    public void setUmbral(double umbral) { this.umbral = umbral; }

    public boolean isActiva() { return activa; }
    public void setActiva(boolean activa) { this.activa = activa; }

    public String getOperador() { return operador; }
    public void setOperador(String operador) { this.operador = operador; }

    public String getPrioridad() { return prioridad; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }

    public Double getValorActual() { return valorActual; }
    public void setValorActual(Double valorActual) { this.valorActual = valorActual; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }
}
