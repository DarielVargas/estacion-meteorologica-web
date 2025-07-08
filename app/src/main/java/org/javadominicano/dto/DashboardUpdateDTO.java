package org.javadominicano.dto;

import org.javadominicano.dto.DatosMeteorologicosDTO;
import org.javadominicano.visualizadorweb.dto.MedicionesRecientesDTO;

public class DashboardUpdateDTO {
    private DatosMeteorologicosDTO datos;
    private MedicionesRecientesDTO mediciones;

    public DashboardUpdateDTO() {}

    public DashboardUpdateDTO(DatosMeteorologicosDTO datos, MedicionesRecientesDTO mediciones) {
        this.datos = datos;
        this.mediciones = mediciones;
    }

    public DatosMeteorologicosDTO getDatos() {
        return datos;
    }

    public void setDatos(DatosMeteorologicosDTO datos) {
        this.datos = datos;
    }

    public MedicionesRecientesDTO getMediciones() {
        return mediciones;
    }

    public void setMediciones(MedicionesRecientesDTO mediciones) {
        this.mediciones = mediciones;
    }
}
