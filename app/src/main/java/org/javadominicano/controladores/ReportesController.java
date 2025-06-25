package org.javadominicano.controladores;

import org.javadominicano.entidades.*;
import org.javadominicano.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;

@Controller
public class ReportesController {

    @Autowired private RepositorioDatosVelocidad repoVelocidad;
    @Autowired private RepositorioDatosDireccion repoDireccion;
    @Autowired private RepositorioDatosPrecipitacion repoPrecipitacion;
    @Autowired private RepositorioDatosHumedad repoHumedad;
    @Autowired private RepositorioDatosTemperatura repoTemperatura;

    // Lista de estaciones simulada (compartida con VisualizadorController)
    private static List<EstacionMeteorologica> estaciones = VisualizadorController.estaciones;

    @GetMapping("/reportes")
    public String mostrarReportes(
            @RequestParam(value = "fecha", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model
    ) {
        if (fecha == null) {
            fecha = LocalDate.now();
        }

        Timestamp inicio = Timestamp.valueOf(fecha.atStartOfDay());
        Timestamp fin = Timestamp.valueOf(fecha.plusDays(1).atStartOfDay());

        List<DatosVelocidad> velocidades = repoVelocidad.findByFechaBetweenOrderByFechaAsc(inicio, fin);
        List<DatosDireccion> direcciones = repoDireccion.findByFechaBetweenOrderByFechaAsc(inicio, fin);
        List<DatosPrecipitacion> precipitaciones = repoPrecipitacion.findByFechaBetweenOrderByFechaAsc(inicio, fin);
        List<DatosHumedad> humedades = repoHumedad.findByFechaBetweenOrderByFechaAsc(inicio, fin);
        List<DatosTemperatura> temperaturas = repoTemperatura.findByFechaBetweenOrderByFechaAsc(inicio, fin);

        int activas = 0;
        int inactivas = 0;
        for (EstacionMeteorologica est : estaciones) {
            boolean tiene = velocidades.stream().anyMatch(v -> est.getId().equals(v.getSensorId()))
                    || direcciones.stream().anyMatch(d -> est.getId().equals(d.getSensorId()))
                    || precipitaciones.stream().anyMatch(p -> est.getId().equals(p.getSensorId()))
                    || humedades.stream().anyMatch(h -> est.getId().equals(h.getSensorId()))
                    || temperaturas.stream().anyMatch(t -> est.getId().equals(t.getSensorId()));
            if (tiene) activas++; else inactivas++;
        }

        model.addAttribute("fecha", fecha);
        model.addAttribute("velocidades", velocidades);
        model.addAttribute("direcciones", direcciones);
        model.addAttribute("precipitaciones", precipitaciones);
        model.addAttribute("humedades", humedades);
        model.addAttribute("temperaturas", temperaturas);
        model.addAttribute("estacionesActivas", activas);
        model.addAttribute("estacionesInactivas", inactivas);
        return "reportes";
    }
}
