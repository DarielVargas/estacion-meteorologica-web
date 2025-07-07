package org.javadominicano.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.PageRequest;

import org.javadominicano.dto.DatosMeteorologicosDTO;
import org.javadominicano.dto.DatosMeteorologicosDTO.SerieDatos;

import org.javadominicano.entidades.DatosVelocidad;
import org.javadominicano.entidades.DatosDireccion;
import org.javadominicano.entidades.DatosPrecipitacion;
import org.javadominicano.visualizadorweb.entidades.DatosHumedad;
import org.javadominicano.visualizadorweb.entidades.DatosTemperatura;

import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.repositorios.RepositorioDatosDireccion;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
import org.javadominicano.visualizadorweb.repositorios.DatosHumedadRepository;
import org.javadominicano.visualizadorweb.repositorios.DatosTemperaturaRepository;

import org.javadominicano.dto.UltimasMedicionesDTO;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class DatosMeteorologicosController {

    @Autowired
    private RepositorioDatosVelocidad repoVelocidad;

    @Autowired
    private DatosHumedadRepository repoHumedad;

    @Autowired
    private DatosTemperaturaRepository repoTemperatura;

    @Autowired
    private RepositorioDatosDireccion repoDireccion;

    @Autowired
    private RepositorioDatosPrecipitacion repoPrecipitacion;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @GetMapping("/api/datos-meteorologicos")
    public DatosMeteorologicosDTO obtenerDatos() {
        List<DatosVelocidad> velocidades = repoVelocidad.findTop30ByOrderByFechaDesc();
        List<DatosHumedad> humedades = repoHumedad.findTop30ByOrderByFechaDesc();
        List<DatosTemperatura> temperaturas = repoTemperatura.findTop30ByOrderByFechaDesc();

        // Ordenar ascendente (más antiguo a más reciente)
        velocidades.sort(Comparator.comparing(DatosVelocidad::getFecha));
        humedades.sort(Comparator.comparing(DatosHumedad::getFecha));
        temperaturas.sort(Comparator.comparing(DatosTemperatura::getFecha));

        // Construir listas para labels y valores (suponemos que fechas coinciden o tomamos la de velocidad)
        List<String> labels = velocidades.stream()
            .map(d -> d.getFecha().toLocalDateTime().format(formatter))
            .collect(Collectors.toList());

        List<Double> windValues = velocidades.stream()
            .map(DatosVelocidad::getVelocidad)
            .collect(Collectors.toList());

        List<Double> humidityValues = humedades.stream()
            .map(DatosHumedad::getHumedad)
            .collect(Collectors.toList());

        List<Double> temperatureValues = temperaturas.stream()
            .map(DatosTemperatura::getTemperatura)
            .collect(Collectors.toList());

        SerieDatos wind = new SerieDatos(labels, windValues);
        SerieDatos humidity = new SerieDatos(labels, humidityValues);
        SerieDatos temperature = new SerieDatos(labels, temperatureValues);

        return new DatosMeteorologicosDTO(wind, humidity, temperature);
    }

    @GetMapping("/api/ultimas-mediciones")
    public UltimasMedicionesDTO obtenerUltimasMediciones() {
        UltimasMedicionesDTO dto = new UltimasMedicionesDTO();
        dto.setVelocidad(repoVelocidad.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0));
        dto.setDireccion(repoDireccion.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0));
        dto.setPrecipitacion(repoPrecipitacion.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0));
        dto.setHumedad(repoHumedad.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0));
        dto.setTemperatura(repoTemperatura.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0));
        return dto;
    }
}
