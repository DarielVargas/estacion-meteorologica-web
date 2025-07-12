package org.javadominicano.entidades;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;

@Entity
@Table(name = "alertas_mediciones")
public class AlertaMedicion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String estacion;
    private String parametro;

    @Column(name = "valor_actual")
    private double valorActual;

    @Column(name = "valor_umbral")
    private double valorUmbral;

    @Column(columnDefinition = "TEXT")
    private String mensaje;

    @CreationTimestamp
    @Column(updatable = false)
    private Timestamp fecha;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEstacion() {
        return estacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public String getParametro() {
        return parametro;
    }

    public void setParametro(String parametro) {
        this.parametro = parametro;
    }

    public double getValorActual() {
        return valorActual;
    }

    public void setValorActual(double valorActual) {
        this.valorActual = valorActual;
    }

    public double getValorUmbral() {
        return valorUmbral;
    }

    public void setValorUmbral(double valorUmbral) {
        this.valorUmbral = valorUmbral;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }
}
