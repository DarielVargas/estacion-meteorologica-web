package org.javadominicano.servicios;

import org.javadominicano.dto.AlarmaActivaDTO;
import org.javadominicano.entidades.Alarma;
import org.javadominicano.entidades.TipoSensor;
import org.javadominicano.repositorios.*;
import org.javadominicano.visualizadorweb.entidades.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlarmasService {
    @Autowired
    private RepositorioAlarma repoAlarma;
    @Autowired private RepositorioDatosTemperatura repoTemp;
    @Autowired private RepositorioDatosHumedad repoHum;
    @Autowired private RepositorioDatosVelocidad repoVel;
    @Autowired private RepositorioDatosPrecipitacion repoPre;
    @Autowired private RepositorioDatosPresion repoPres;
    @Autowired private RepositorioDatosHumedadSuelo repoHumSu;

    public List<AlarmaActivaDTO> obtenerAlarmasActivas() {
        List<AlarmaActivaDTO> lista = new ArrayList<>();
        for (Alarma a : repoAlarma.findByActivaTrue()) {
            ValorActual val = obtenerValor(a.getEstacionId(), a.getSensor());
            if (val == null) continue;
            if (comparar(a.getTipo(), val.valor, a.getUmbral())) {
                AlarmaActivaDTO dto = new AlarmaActivaDTO();
                dto.setAlarma(a);
                dto.setValorActual(val.valor);
                dto.setFecha(val.fecha);
                lista.add(dto);
            }
        }
        return lista;
    }

    private boolean comparar(String tipo, double actual, double umbral) {
        return ">".equals(tipo) ? actual > umbral : actual < umbral;
    }

    private record ValorActual(double valor, Timestamp fecha) {}

    private ValorActual obtenerValor(String estacion, String sensor) {
        TipoSensor ts;
        try {
            ts = TipoSensor.valueOf(sensor);
        } catch (Exception e) {
            return null;
        }
        switch (ts) {
            case TEMPERATURA -> {
                var d = repoTemp.findFirstByEstacionIdOrderByFechaDesc(estacion);
                if (d != null) return new ValorActual(d.getTemperatura(), d.getFecha());
            }
            case HUMEDAD -> {
                var d = repoHum.findFirstByEstacionIdOrderByFechaDesc(estacion);
                if (d != null) return new ValorActual(d.getHumedad(), d.getFecha());
            }
            case VELOCIDAD_VIENTO -> {
                var d = repoVel.findFirstByEstacionIdOrderByFechaDesc(estacion);
                if (d != null) return new ValorActual(d.getVelocidad(), d.getFecha());
            }
            case PRECIPITACION -> {
                var d = repoPre.findFirstByEstacionIdOrderByFechaDesc(estacion);
                if (d != null) return new ValorActual(d.getProbabilidad(), d.getFecha());
            }
            case PRESION -> {
                var d = repoPres.findFirstByEstacionIdOrderByFechaDesc(estacion);
                if (d != null) return new ValorActual(d.getPresion(), d.getFecha());
            }
            case HUMEDAD_SUELO -> {
                var d = repoHumSu.findFirstByEstacionIdOrderByFechaDesc(estacion);
                if (d != null) return new ValorActual(d.getHumedad(), d.getFecha());
            }
        }
        return null;
    }
}
