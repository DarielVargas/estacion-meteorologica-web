package org.javadominicano.servicios;

import org.javadominicano.entidades.Alerta;
import org.javadominicano.entidades.DatosPrecipitacion;
import org.javadominicano.entidades.DatosVelocidad;
import org.javadominicano.visualizadorweb.entidades.DatosHumedad;
import org.javadominicano.visualizadorweb.entidades.DatosTemperatura;
import org.javadominicano.visualizadorweb.entidades.DatosPresion;
import org.javadominicano.visualizadorweb.entidades.DatosHumedadSuelo;
import org.javadominicano.dto.AlertaActivaDTO;
import org.javadominicano.repositorios.RepositorioAlerta;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosTemperatura;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosPresion;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedadSuelo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class AlertasService {

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
    @Autowired
    private RepositorioDatosPresion repoPresion;
    @Autowired
    private RepositorioDatosHumedadSuelo repoHumedadSuelo;

    public List<AlertaActivaDTO> obtenerAlertasActivas() {
        List<AlertaActivaDTO> lista = new ArrayList<>();

        DatosTemperatura temp = repoTemperatura.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0);
        DatosHumedad hum      = repoHumedad.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0);
        DatosVelocidad vel    = repoVelocidad.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0);
        DatosPrecipitacion pre= repoPrecipitacion.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0);
        DatosPresion pres    = repoPresion.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0);
        DatosHumedadSuelo hs = repoHumedadSuelo.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0);

        agregarAlertaActiva(lista, repoAlerta.findByNombre("Temperatura"), temp.getTemperatura(), temp.getFecha());
        agregarAlertaActiva(lista, repoAlerta.findByNombre("Humedad"), hum.getHumedad(), hum.getFecha());
        agregarAlertaActiva(lista, repoAlerta.findByNombre("VelocidadViento"), vel.getVelocidad(), vel.getFecha());
        agregarAlertaActiva(lista, repoAlerta.findByNombre("Precipitacion"), pre.getProbabilidad(), pre.getFecha());
        agregarAlertaActiva(lista, repoAlerta.findByNombre("Presion"), pres.getPresion(), pres.getFecha());
        agregarAlertaActiva(lista, repoAlerta.findByNombre("HumedadSuelo"), hs.getHumedad(), hs.getFecha());

        return lista;
    }

    public List<String> obtenerAlertasActivasTexto() {
        List<String> textos = new ArrayList<>();
        for (AlertaActivaDTO dto : obtenerAlertasActivas()) {
            textos.add(formatoAlerta(dto.getAlerta()));
        }
        return textos;
    }

    private void agregarAlertaActiva(List<AlertaActivaDTO> lista, Alerta alerta, double valor, Timestamp fecha) {
        if (chequearAlerta(alerta, valor)) {
            AlertaActivaDTO dto = new AlertaActivaDTO();
            dto.setAlerta(alerta);
            dto.setValorActual(valor);
            dto.setFecha(fecha);
            lista.add(dto);
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
        if (a == null) {
            return "Umbral superado";
        }
        switch (a.getNombre()) {
            case "Temperatura":
                return "Umbral de temperatura superado";
            case "Humedad":
                return "Umbral de humedad superado";
            case "VelocidadViento":
                return "Umbral de velocidad de viento superado";
            case "Precipitacion":
                return "Umbral de precipitación superado";
            case "Presion":
                return "Umbral de presión superado";
            case "HumedadSuelo":
                return "Umbral de humedad del suelo superado";
            default:
                return "Umbral superado";
        }
    }}