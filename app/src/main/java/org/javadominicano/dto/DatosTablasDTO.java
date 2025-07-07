package org.javadominicano.dto;

import java.util.List;

import org.javadominicano.entidades.DatosDireccion;
import org.javadominicano.entidades.DatosPrecipitacion;
import org.javadominicano.entidades.DatosVelocidad;
import org.javadominicano.visualizadorweb.entidades.DatosHumedad;
import org.javadominicano.visualizadorweb.entidades.DatosTemperatura;

public class DatosTablasDTO {
    private List<DatosVelocidad> velocidades;
    private List<DatosDireccion> direcciones;
    private List<DatosPrecipitacion> precipitaciones;
    private List<DatosHumedad> humedades;
    private List<DatosTemperatura> temperaturas;

    public List<DatosVelocidad> getVelocidades() { return velocidades; }
    public void setVelocidades(List<DatosVelocidad> velocidades) { this.velocidades = velocidades; }

    public List<DatosDireccion> getDirecciones() { return direcciones; }
    public void setDirecciones(List<DatosDireccion> direcciones) { this.direcciones = direcciones; }

    public List<DatosPrecipitacion> getPrecipitaciones() { return precipitaciones; }
    public void setPrecipitaciones(List<DatosPrecipitacion> precipitaciones) { this.precipitaciones = precipitaciones; }

    public List<DatosHumedad> getHumedades() { return humedades; }
    public void setHumedades(List<DatosHumedad> humedades) { this.humedades = humedades; }

    public List<DatosTemperatura> getTemperaturas() { return temperaturas; }
    public void setTemperaturas(List<DatosTemperatura> temperaturas) { this.temperaturas = temperaturas; }
}

