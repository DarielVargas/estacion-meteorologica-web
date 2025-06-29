package org.javadominicano.servicio;

import org.javadominicano.entidades.Alerta;
import org.javadominicano.entidades.DatosPrecipitacion;
import org.javadominicano.entidades.DatosVelocidad;
import org.javadominicano.visualizadorweb.entidades.DatosHumedad;
import org.javadominicano.visualizadorweb.entidades.DatosTemperatura;
import org.javadominicano.repositorios.RepositorioAlerta;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosTemperatura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ServicioAlertas {

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

    public List<String> obtenerAlertasActivasDescripcion() {
        List<String> lista = new ArrayList<>();

        DatosTemperatura temp = repoTemperatura.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0);
        DatosHumedad hum = repoHumedad.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0);
        DatosVelocidad vel = repoVelocidad.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0);
        DatosPrecipitacion pre = repoPrecipitacion.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0);

        agregarAlerta(lista, repoAlerta.findByNombre("Temperatura"), temp.getTemperatura());
        agregarAlerta(lista, repoAlerta.findByNombre("Humedad"), hum.getHumedad());
        agregarAlerta(lista, repoAlerta.findByNombre("VelocidadViento"), vel.getVelocidad());
        agregarAlerta(lista, repoAlerta.findByNombre("Precipitacion"), pre.getProbabilidad());

        return lista;
    }

    private void agregarAlerta(List<String> lista, Alerta alerta, Double valor) {
        if (chequearAlerta(alerta, valor)) {
            lista.add(descripcion(alerta));
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

    public String descripcion(Alerta alerta) {
        if (alerta == null) return "";
        return switch (alerta.getNombre()) {
            case "Temperatura" -> "Umbral de temperatura superado";
            case "Humedad" -> "Umbral de humedad superado";
            case "VelocidadViento" -> "Umbral de velocidad de viento superado";
            case "Precipitacion" -> "Umbral de precipitación superado";
            default -> "Alerta activa";
        };
    }
}
