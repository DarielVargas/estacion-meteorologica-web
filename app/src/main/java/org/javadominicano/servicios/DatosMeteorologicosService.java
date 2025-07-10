package org.javadominicano.servicios;

import org.javadominicano.dto.DatosMeteorologicosDTO;
import org.javadominicano.dto.DatosMeteorologicosDTO.SerieDatos;
import org.javadominicano.entidades.DatosVelocidad;
import org.javadominicano.visualizadorweb.entidades.DatosHumedad;
import org.javadominicano.visualizadorweb.entidades.DatosTemperatura;
import org.javadominicano.repositorios.DatosVelocidadRepository;
import org.javadominicano.visualizadorweb.repositorios.DatosHumedadRepository;
import org.javadominicano.visualizadorweb.repositorios.DatosTemperaturaRepository;
import org.javadominicano.visualizadorweb.dto.MedicionesRecientesDTO;
import org.javadominicano.repositorios.RepositorioDatosDireccion;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosTemperatura;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosPresion;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedadSuelo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.PageRequest;

@Service
public class DatosMeteorologicosService {

    @Autowired
    private DatosVelocidadRepository repoVelocidad;
    @Autowired
    private DatosHumedadRepository repoHumedad;
    @Autowired
    private DatosTemperaturaRepository repoTemperatura;

    @Autowired
    private RepositorioDatosVelocidad repoVelocidadUltimo;
    @Autowired
    private RepositorioDatosDireccion repoDireccionUltimo;
    @Autowired
    private RepositorioDatosPrecipitacion repoPrecipitacionUltimo;
    @Autowired
    private RepositorioDatosHumedad repoHumedadUltimo;
    @Autowired
    private RepositorioDatosTemperatura repoTemperaturaUltimo;
    @Autowired
    private RepositorioDatosPresion repoPresionUltimo;
    @Autowired
    private RepositorioDatosHumedadSuelo repoHumedadSueloUltimo;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    public DatosMeteorologicosDTO obtenerDatos() {
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

        MedicionesRecientesDTO mediciones = new MedicionesRecientesDTO();
        mediciones.setTemperatura(
            repoTemperaturaUltimo.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0).getTemperatura());
        mediciones.setHumedad(
            repoHumedadUltimo.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0).getHumedad());
        mediciones.setVelocidadViento(
            repoVelocidadUltimo.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0).getVelocidad());
        mediciones.setDireccionViento(
            repoDireccionUltimo.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0).getDireccion());
        mediciones.setPrecipitacion(
            repoPrecipitacionUltimo.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0).getProbabilidad());
        mediciones.setPresion(
            repoPresionUltimo.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0).getPresion());
        mediciones.setHumedadSuelo(
            repoHumedadSueloUltimo.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0).getHumedad());

        return new DatosMeteorologicosDTO(wind, humidity, temperature, mediciones);
    }
}
