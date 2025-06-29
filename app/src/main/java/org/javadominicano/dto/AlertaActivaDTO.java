package org.javadominicano.dto;

import org.javadominicano.entidades.Alerta;

import java.sql.Timestamp;

public class AlertaActivaDTO {
    private Alerta alerta;
    private double valorActual;
    private Timestamp fecha;

    public Alerta getAlerta() {
        return alerta;
    }

    public void setAlerta(Alerta alerta) {
        this.alerta = alerta;
    }

    public double getValorActual() {
        return valorActual;
    }

    public void setValorActual(double valorActual) {
        this.valorActual = valorActual;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}
