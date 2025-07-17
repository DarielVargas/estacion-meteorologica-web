package org.javadominicano.entidades;

public enum TipoSensor {
    TEMPERATURA("Temperatura"),
    HUMEDAD("Humedad"),
    VELOCIDAD_VIENTO("VelocidadViento"),
    PRECIPITACION("Precipitacion"),
    PRESION("Presion"),
    HUMEDAD_SUELO("HumedadSuelo");

    private final String nombre;

    TipoSensor(String nombre) { this.nombre = nombre; }

    public String getNombre() { return nombre; }
}
