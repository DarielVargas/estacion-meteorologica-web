package org.javadominicano.dto;

import java.util.List;
import org.javadominicano.visualizadorweb.dto.MedicionesRecientesDTO;

public class DatosMeteorologicosDTO {

    public static class SerieDatos {
        private List<String> labels;
        private List<Double> values;

        public SerieDatos(List<String> labels, List<Double> values) {
            this.labels = labels;
            this.values = values;
        }

        public List<String> getLabels() {
            return labels;
        }

        public List<Double> getValues() {
            return values;
        }

        public void setLabels(List<String> labels) {
            this.labels = labels;
        }

        public void setValues(List<Double> values) {
            this.values = values;
        }
    }

    private SerieDatos wind;
    private SerieDatos humidity;
    private SerieDatos temperature;
    private MedicionesRecientesDTO mediciones;

    public DatosMeteorologicosDTO(SerieDatos wind, SerieDatos humidity, SerieDatos temperature,
                                  MedicionesRecientesDTO mediciones) {
        this.wind = wind;
        this.humidity = humidity;
        this.temperature = temperature;
        this.mediciones = mediciones;
    }

    public SerieDatos getWind() {
        return wind;
    }

    public SerieDatos getHumidity() {
        return humidity;
    }

    public SerieDatos getTemperature() {
        return temperature;
    }

    public MedicionesRecientesDTO getMediciones() {
        return mediciones;
    }

    public void setWind(SerieDatos wind) {
        this.wind = wind;
    }

    public void setHumidity(SerieDatos humidity) {
        this.humidity = humidity;
    }

    public void setTemperature(SerieDatos temperature) {
        this.temperature = temperature;
    }

    public void setMediciones(MedicionesRecientesDTO mediciones) {
        this.mediciones = mediciones;
    }
}
