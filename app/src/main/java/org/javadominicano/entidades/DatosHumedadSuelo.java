package org.javadominicano.visualizadorweb.entidades;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "datos_humedad_suelo")
public class DatosHumedadSuelo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sensor_id")
    private String sensorId;

    @Column(name = "estacion_id")
    private String estacionId;

    @Column(name = "humedad_suelo")
    private double humedadSuelo;

    @Column(name = "fecha")
    private Timestamp fecha;

    public int getId() { return id; }
    public String getSensorId() { return sensorId; }
    public String getEstacionId() { return estacionId; }
    public double getHumedadSuelo() { return humedadSuelo; }
    public Timestamp getFecha() { return fecha; }

    public void setId(int id) { this.id = id; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }
    public void setEstacionId(String estacionId) { this.estacionId = estacionId; }
    public void setHumedadSuelo(double humedadSuelo) { this.humedadSuelo = humedadSuelo; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }
}
