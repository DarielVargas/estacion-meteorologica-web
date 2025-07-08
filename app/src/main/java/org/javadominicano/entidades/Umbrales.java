package org.javadominicano.visualizadorweb.entidades;

public class Umbrales {
    private double temperatura;
    private double humedad;
    private double velocidadViento;
    private double precipitacion;
    private double presion;
    private double humedadSuelo;

    public double getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(double temperatura) {
        this.temperatura = temperatura;
    }

    public double getHumedad() {
        return humedad;
    }

    public void setHumedad(double humedad) {
        this.humedad = humedad;
    }

    public double getVelocidadViento() {
        return velocidadViento;
    }

    public void setVelocidadViento(double velocidadViento) {
        this.velocidadViento = velocidadViento;
    }

    public double getPrecipitacion() {
        return precipitacion;
    }

    public void setPrecipitacion(double precipitacion) {
        this.precipitacion = precipitacion;
    }

    public double getPresion() {
        return presion;
    }

    public void setPresion(double presion) {
        this.presion = presion;
    }

    public double getHumedadSuelo() {
        return humedadSuelo;
    }

    public void setHumedadSuelo(double humedadSuelo) {
        this.humedadSuelo = humedadSuelo;
    }
}
