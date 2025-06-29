package org.javadominicano.servicios;

import org.javadominicano.entidades.Alerta;
import org.javadominicano.entidades.DatosHumedad;
import org.javadominicano.entidades.DatosPrecipitacion;
import org.javadominicano.entidades.DatosTemperatura;
import org.javadominicano.entidades.DatosVelocidad;
import org.javadominicano.repositorios.RepositorioAlerta;
import org.javadominicano.repositorios.RepositorioDatosHumedad;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
import org.javadominicano.repositorios.RepositorioDatosTemperatura;
import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AlertaNotificacionService {
    @Autowired
    private RepositorioAlerta repoAlerta;
    @Autowired
    private RepositorioDatosVelocidad repoVelocidad;
    @Autowired
    private RepositorioDatosHumedad repoHumedad;
    @Autowired
    private RepositorioDatosTemperatura repoTemperatura;
    @Autowired
    private RepositorioDatosPrecipitacion repoPrecipitacion;

    public List<String> obtenerAlertasActivas() {
        List<String> alertas = new ArrayList<>();

        DatosTemperatura temp = repoTemperatura.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0);
        DatosHumedad hum = repoHumedad.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0);
        DatosVelocidad vel = repoVelocidad.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0);
        DatosPrecipitacion pre = repoPrecipitacion.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0);

        agregarAlerta(alertas, repoAlerta.findByNombre("Temperatura"), temp.getTemperatura());
        agregarAlerta(alertas, repoAlerta.findByNombre("Humedad"), hum.getHumedad());
        agregarAlerta(alertas, repoAlerta.findByNombre("VelocidadViento"), vel.getVelocidad());
        agregarAlerta(alertas, repoAlerta.findByNombre("Precipitacion"), pre.getProbabilidad());

        return alertas;
    }

    private void agregarAlerta(List<String> lista, Alerta alerta, Double valor) {
        if (chequearAlerta(alerta, valor)) {
            lista.add(formatoAlerta(alerta));
        }
    }

    private boolean chequearAlerta(Alerta alerta, Double valor) {
        if (alerta == null || valor == null || !alerta.isActiva()) {
            return false;
        }
        if (">".equals(alerta.getOperador())) {
            return valor > alerta.getUmbral();
        } else {
            return valor < alerta.getUmbral();
        }
    }

    private String formatoAlerta(Alerta a) {
        return a.getNombre() + " " + a.getOperador() + " " + a.getUmbral();
    }
}
