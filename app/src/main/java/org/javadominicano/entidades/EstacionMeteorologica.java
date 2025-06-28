package org.javadominicano.entidades;

public class EstacionMeteorologica {
    private String id;
    private String nombre;
    private String ubicacion;
    private boolean activa;
    private java.util.Date ultimaActualizacion;

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
        this.activa = activa;
    }

    public java.util.Date getUltimaActualizacion() {
        return ultimaActualizacion;
    }

    public void setUltimaActualizacion(java.util.Date ultimaActualizacion) {
        this.ultimaActualizacion = ultimaActualizacion;
    }
}
