package org.javadominicano.servicios;

import org.javadominicano.dto.DashboardUpdateDTO;
import org.javadominicano.dto.DatosMeteorologicosDTO;
import org.javadominicano.dto.MedicionesRecientesDTO;
import org.javadominicano.entidades.*;
import org.javadominicano.repositorios.*;
import org.javadominicano.visualizadorweb.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardWebSocketService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private DatosVelocidadRepository repoVelocidad;
    @Autowired
    private DatosHumedadRepository repoHumedad;
    @Autowired
    private DatosTemperaturaRepository repoTemperatura;
    @Autowired
    private RepositorioDatosDireccion repoDireccion;
    @Autowired
    private RepositorioDatosPrecipitacion repoPrecipitacion;
    @Autowired
    private RepositorioEstacionMeteorologica repoEstacion;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @Scheduled(fixedRate = 10000)
    public void sendDashboardData() {
        DatosMeteorologicosDTO datos = obtenerDatosMeteorologicos();
        MedicionesRecientesDTO mediciones = obtenerMedicionesRecientes();

        List<EstacionMeteorologica> estaciones = repoEstacion.findAll();
        int total = estaciones.size();
        int activas = 0;
        int inactivas = 0;
        Date limite = new Date(System.currentTimeMillis() - 30000L);
        for (EstacionMeteorologica e : estaciones) {
            Date ultima = obtenerUltimaFechaEstacion(e.getId());
            if (ultima != null && ultima.after(limite)) {
                activas++;
            } else {
                inactivas++;
            }
        }

        DashboardUpdateDTO dto = new DashboardUpdateDTO(datos, mediciones, total, activas, inactivas);
        messagingTemplate.convertAndSend("/topic/dashboard", dto);
    }

    private DatosMeteorologicosDTO obtenerDatosMeteorologicos() {
        List<DatosVelocidad> velocidades = repoVelocidad.findTop30ByOrderByFechaDesc();
        List<DatosHumedad> humedades = repoHumedad.findTop30ByOrderByFechaDesc();
        List<DatosTemperatura> temperaturas = repoTemperatura.findTop30ByOrderByFechaDesc();

        velocidades.sort(Comparator.comparing(DatosVelocidad::getFecha));
        humedades.sort(Comparator.comparing(DatosHumedad::getFecha));
        temperaturas.sort(Comparator.comparing(DatosTemperatura::getFecha));

        List<String> labels = velocidades.stream()
                .map(d -> d.getFecha().toLocalDateTime().format(formatter))
                .collect(Collectors.toList());
        List<Double> windValues = velocidades.stream().map(DatosVelocidad::getVelocidad).collect(Collectors.toList());
        List<Double> humidityValues = humedades.stream().map(DatosHumedad::getHumedad).collect(Collectors.toList());
        List<Double> temperatureValues = temperaturas.stream().map(DatosTemperatura::getTemperatura).collect(Collectors.toList());

        DatosMeteorologicosDTO.SerieDatos wind = new DatosMeteorologicosDTO.SerieDatos(labels, windValues);
        DatosMeteorologicosDTO.SerieDatos humidity = new DatosMeteorologicosDTO.SerieDatos(labels, humidityValues);
        DatosMeteorologicosDTO.SerieDatos temperature = new DatosMeteorologicosDTO.SerieDatos(labels, temperatureValues);
        return new DatosMeteorologicosDTO(wind, humidity, temperature);
    }

    private MedicionesRecientesDTO obtenerMedicionesRecientes() {
        MedicionesRecientesDTO mediciones = new MedicionesRecientesDTO();
        mediciones.setTemperatura(repoTemperatura.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getTemperatura());
        mediciones.setHumedad(repoHumedad.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getHumedad());
        mediciones.setVelocidadViento(repoVelocidad.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getVelocidad());
        mediciones.setDireccionViento(repoDireccion.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getDireccion());
        mediciones.setPrecipitacion(repoPrecipitacion.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getProbabilidad());
        return mediciones;
    }

    private Date obtenerUltimaFechaEstacion(String estacionId) {
        Date ultima = null;
        Timestamp t;
        t = repoTemperatura.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) ultima = t;
        t = repoHumedad.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) ultima = t;
        t = repoVelocidad.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) ultima = t;
        t = repoDireccion.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) ultima = t;
        t = repoPrecipitacion.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) ultima = t;
        return ultima;
    }
}
