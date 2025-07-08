package org.javadominicano.servicios;

import org.javadominicano.dto.DatosMeteorologicosDTO;
import org.javadominicano.dto.DatosMeteorologicosDTO.SerieDatos;
import org.javadominicano.visualizadorweb.dto.MedicionesRecientesDTO;
import org.javadominicano.entidades.*;
import org.javadominicano.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardDataService {

    @Autowired private DatosVelocidadRepository repoVelocidad;
    @Autowired private DatosHumedadRepository repoHumedad;
    @Autowired private DatosTemperaturaRepository repoTemperatura;

    @Autowired private RepositorioDatosDireccion repoDireccion;
    @Autowired private RepositorioDatosPrecipitacion repoPrecipitacion;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    public DatosMeteorologicosDTO obtenerSeries() {
        List<DatosVelocidad> velocidades = repoVelocidad.findTop30ByOrderByFechaDesc();
        List<DatosHumedad> humedades = repoHumedad.findTop30ByOrderByFechaDesc();
        List<DatosTemperatura> temperaturas = repoTemperatura.findTop30ByOrderByFechaDesc();

        velocidades.sort(Comparator.comparing(DatosVelocidad::getFecha));
        humedades.sort(Comparator.comparing(DatosHumedad::getFecha));
        temperaturas.sort(Comparator.comparing(DatosTemperatura::getFecha));

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

    public MedicionesRecientesDTO obtenerMediciones() {
        MedicionesRecientesDTO dto = new MedicionesRecientesDTO();
        dto.setTemperatura(
            repoTemperatura.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getTemperatura()
        );
        dto.setHumedad(
            repoHumedad.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getHumedad()
        );
        dto.setVelocidadViento(
            repoVelocidad.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getVelocidad()
        );
        dto.setDireccionViento(
            repoDireccion.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getDireccion()
        );
        dto.setPrecipitacion(
            repoPrecipitacion.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getProbabilidad()
        );
        return dto;
    }
}
