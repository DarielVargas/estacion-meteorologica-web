package org.javadominicano.visualizadorweb.entidades;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "datos_presion")
public class DatosPresion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sensor_id")
    private String sensorId;

    @Column(name = "estacion_id")
    private String estacionId;

    @Column(name = "presion")
    private double presion;

    @Column(name = "fecha")
    private Timestamp fecha;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public double getPresion() {
        return presion;
    }

    public void setPresion(double presion) {
        this.presion = presion;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}
