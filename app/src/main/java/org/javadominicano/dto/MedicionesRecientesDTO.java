package org.javadominicano.visualizadorweb.dto;

public class MedicionesRecientesDTO {
    private Double temperatura;
    private Double humedad;
    private Double velocidadViento;
    private String direccionViento;
    private Double precipitacion;

    // Getters y setters
    public Double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(Double temperatura) {
        this.temperatura = temperatura;
    }

    public Double getHumedad() {
        return humedad;
    }

    public void setHumedad(Double humedad) {
        this.humedad = humedad;
    }

    public Double getVelocidadViento() {
        return velocidadViento;
    }

    public void setVelocidadViento(Double velocidadViento) {
        this.velocidadViento = velocidadViento;
    }

    public String getDireccionViento() {
        return direccionViento;
    }

    public void setDireccionViento(String direccionViento) {
        this.direccionViento = direccionViento;
    }

    public Double getPrecipitacion() {
        return precipitacion;
    }

    public void setPrecipitacion(Double precipitacion) {
        this.precipitacion = precipitacion;
    }
}
