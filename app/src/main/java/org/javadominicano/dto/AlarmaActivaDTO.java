package org.javadominicano.dto;

import org.javadominicano.entidades.Alarma;

import java.sql.Timestamp;

public class AlarmaActivaDTO {
    private Alarma alarma;
    private double valorActual;
    private Timestamp fecha;

    public Alarma getAlarma() { return alarma; }
    public void setAlarma(Alarma alarma) { this.alarma = alarma; }
    public double getValorActual() { return valorActual; }
    public void setValorActual(double valorActual) { this.valorActual = valorActual; }
    public Timestamp getFecha() { return fecha; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }
}
