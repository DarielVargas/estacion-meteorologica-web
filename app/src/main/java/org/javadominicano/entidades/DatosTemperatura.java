package org.javadominicano.visualizadorweb.entidades;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "datos_temperatura")
public class DatosTemperatura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sensor_id")
    private String sensorId;

    @Column(name = "estacion_id")
    private String estacionId;

    @Column(name = "temperatura")
    private double temperatura;

    @Column(name = "fecha")
    private Timestamp fecha;

    // Getters y setters
    public int getId() { return id; }
    public String getSensorId() { return sensorId; }
    public double getTemperatura() { return temperatura; }
    public Timestamp getFecha() { return fecha; }

    public void setId(int id) { this.id = id; }
    public void setSensorId(String sensorId) { this.sensorId = sensorId; }
    public String getEstacionId() { return estacionId; }
    public void setEstacionId(String estacionId) { this.estacionId = estacionId; }
    public void setTemperatura(double temperatura) { this.temperatura = temperatura; }
    public void setFecha(Timestamp fecha) { this.fecha = fecha; }
}