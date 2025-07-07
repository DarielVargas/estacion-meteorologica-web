package org.javadominicano.dto;

import org.javadominicano.entidades.DatosDireccion;
import org.javadominicano.entidades.DatosPrecipitacion;
import org.javadominicano.entidades.DatosVelocidad;
import org.javadominicano.visualizadorweb.entidades.DatosHumedad;
import org.javadominicano.visualizadorweb.entidades.DatosTemperatura;

public class UltimasMedicionesDTO {
    private DatosVelocidad velocidad;
    private DatosDireccion direccion;
    private DatosPrecipitacion precipitacion;
    private DatosHumedad humedad;
    private DatosTemperatura temperatura;

    public DatosVelocidad getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(DatosVelocidad velocidad) {
        this.velocidad = velocidad;
    }

    public DatosDireccion getDireccion() {
        return direccion;
    }

    public void setDireccion(DatosDireccion direccion) {
        this.direccion = direccion;
    }

    public DatosPrecipitacion getPrecipitacion() {
        return precipitacion;
    }

    public void setPrecipitacion(DatosPrecipitacion precipitacion) {
        this.precipitacion = precipitacion;
    }

    public DatosHumedad getHumedad() {
        return humedad;
    }

    public void setHumedad(DatosHumedad humedad) {
        this.humedad = humedad;
    }

    public DatosTemperatura getTemperatura() {
        return temperatura;
    }

    public void setTemperatura(DatosTemperatura temperatura) {
        this.temperatura = temperatura;
    }
}
