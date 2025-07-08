package org.javadominicano.dto;

public class DashboardUpdateDTO {
    private DatosMeteorologicosDTO datos;
    private MedicionesRecientesDTO mediciones;
    private int totalEstaciones;
    private int estacionesActivas;
    private int estacionesInactivas;

    public DashboardUpdateDTO(DatosMeteorologicosDTO datos,
                              MedicionesRecientesDTO mediciones,
                              int totalEstaciones,
                              int estacionesActivas,
                              int estacionesInactivas) {
        this.datos = datos;
        this.mediciones = mediciones;
        this.totalEstaciones = totalEstaciones;
        this.estacionesActivas = estacionesActivas;
        this.estacionesInactivas = estacionesInactivas;
    }

    public DatosMeteorologicosDTO getDatos() { return datos; }
    public MedicionesRecientesDTO getMediciones() { return mediciones; }
    public int getTotalEstaciones() { return totalEstaciones; }
    public int getEstacionesActivas() { return estacionesActivas; }
    public int getEstacionesInactivas() { return estacionesInactivas; }

    public void setDatos(DatosMeteorologicosDTO datos) { this.datos = datos; }
    public void setMediciones(MedicionesRecientesDTO mediciones) { this.mediciones = mediciones; }
    public void setTotalEstaciones(int totalEstaciones) { this.totalEstaciones = totalEstaciones; }
    public void setEstacionesActivas(int estacionesActivas) { this.estacionesActivas = estacionesActivas; }
    public void setEstacionesInactivas(int estacionesInactivas) { this.estacionesInactivas = estacionesInactivas; }
}
