package org.javadominicano.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "estaciones")
public class EstacionMeteorologica {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "ubicacion")
    private String ubicacion;

    @Column(name = "activa", columnDefinition = "TINYINT(1) DEFAULT 1")
    private boolean activa = true;

    // Constructor vacío (necesario para Thymeleaf)
    public EstacionMeteorologica() {
    }

    // Constructor con parámetros
    public EstacionMeteorologica(String id, String nombre, String ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
    }

    // Getters y setters
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUbicacion() {
        return ubicacion;
    }
    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;}
