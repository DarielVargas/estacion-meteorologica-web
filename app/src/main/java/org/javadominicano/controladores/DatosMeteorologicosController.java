package org.javadominicano.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RestController;

import org.javadominicano.dto.DatosMeteorologicosDTO;
import org.javadominicano.dto.DatosMeteorologicosDTO.SerieDatos;
import org.javadominicano.visualizadorweb.dto.MedicionesRecientesDTO;
import org.javadominicano.dto.DatosTablasDTO;

import org.javadominicano.entidades.DatosVelocidad;
import org.javadominicano.visualizadorweb.entidades.DatosHumedad;
import org.javadominicano.visualizadorweb.entidades.DatosTemperatura;

import org.javadominicano.repositorios.DatosVelocidadRepository;
import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.repositorios.RepositorioDatosDireccion;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosTemperatura;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class DatosMeteorologicosController {

    @Autowired
    private DatosVelocidadRepository repoVelocidad;

    @Autowired
    private RepositorioDatosVelocidad repoVelocidadDetalle;

    @Autowired
    private RepositorioDatosHumedad repoHumedad;

    @Autowired
    private RepositorioDatosTemperatura repoTemperatura;

    @Autowired
    private RepositorioDatosDireccion repoDireccion;

    @Autowired
    private RepositorioDatosPrecipitacion repoPrecipitacion;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @GetMapping("/api/datos-meteorologicos")
    public DatosMeteorologicosDTO obtenerDatos() {
        List<DatosVelocidad> velocidades = repoVelocidad.findTop30ByOrderByFechaDesc();
        List<DatosHumedad> humedades = repoHumedad.findTopByOrderByFechaDesc(PageRequest.of(0, 30));
        List<DatosTemperatura> temperaturas = repoTemperatura.findTopByOrderByFechaDesc(PageRequest.of(0, 30));

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

    @GetMapping("/api/mediciones-recientes")
    public MedicionesRecientesDTO obtenerMedicionesRecientes() {
        MedicionesRecientesDTO dto = new MedicionesRecientesDTO();
        dto.setTemperatura(
            repoTemperatura.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getTemperatura()
        );
        dto.setHumedad(
            repoHumedad.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getHumedad()
        );
        dto.setVelocidadViento(
            repoVelocidadDetalle.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getVelocidad()
        );
        dto.setDireccionViento(
            repoDireccion.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getDireccion()
        );
        dto.setPrecipitacion(
            repoPrecipitacion.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getProbabilidad()
        );
        return dto;
    }

    @GetMapping("/api/datos-tablas")
    public DatosTablasDTO obtenerDatosTablas() {
        DatosTablasDTO dto = new DatosTablasDTO();
        dto.setVelocidades(repoVelocidadDetalle.findTopByOrderByFechaDesc(PageRequest.of(0, 10)));
        dto.setDirecciones(repoDireccion.findTopByOrderByFechaDesc(PageRequest.of(0, 10)));
        dto.setPrecipitaciones(repoPrecipitacion.findTopByOrderByFechaDesc(PageRequest.of(0, 10)));
        dto.setHumedades(repoHumedad.findTopByOrderByFechaDesc(PageRequest.of(0, 10)));
        dto.setTemperaturas(repoTemperatura.findTopByOrderByFechaDesc(PageRequest.of(0, 10)));
        return dto;
    }
}
