package org.javadominicano.entidades;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "datos_precipitacion")
public class DatosPrecipitacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sensorId;

    @Column(name = "estacion_id")
    private String estacionId;

    private double probabilidad;

    private Timestamp fecha;

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSensorId() {
        return sensorId;
    }

    public void setSensorId(String sensorId) {
        this.sensorId = sensorId;
    }

    public String getEstacionId() {
        return estacionId;
    }

    public void setEstacionId(String estacionId) {
        this.estacionId = estacionId;
    }

    public double getProbabilidad() {
        return probabilidad;
    }

    public void setProbabilidad(double probabilidad) {
        this.probabilidad = probabilidad;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}
