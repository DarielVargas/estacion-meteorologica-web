package org.javadominicano.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.repositorios.RepositorioDatosDireccion;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosTemperatura;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
public class ReportesController {

    @Autowired private RepositorioDatosVelocidad repoVelocidad;
    @Autowired private RepositorioDatosDireccion repoDireccion;
    @Autowired private RepositorioDatosPrecipitacion repoPrecipitacion;
    @Autowired private RepositorioDatosHumedad repoHumedad;
    @Autowired private RepositorioDatosTemperatura repoTemperatura;

    @GetMapping("/reportes")
    public String mostrarReportes(
            @RequestParam(value = "fecha", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            Model model) {

        if (fecha != null) {
            LocalDateTime start = fecha.atStartOfDay();
            LocalDateTime end = start.plusDays(1);
            Timestamp inicio = Timestamp.valueOf(start);
            Timestamp fin = Timestamp.valueOf(end);

            model.addAttribute("velocidades", repoVelocidad.findByFechaBetweenOrderByFechaDesc(inicio, fin));
            model.addAttribute("direcciones", repoDireccion.findByFechaBetweenOrderByFechaDesc(inicio, fin));
            model.addAttribute("precipitaciones", repoPrecipitacion.findByFechaBetweenOrderByFechaDesc(inicio, fin));
            model.addAttribute("humedades", repoHumedad.findByFechaBetweenOrderByFechaDesc(inicio, fin));
            model.addAttribute("temperaturas", repoTemperatura.findByFechaBetweenOrderByFechaDesc(inicio, fin));
        }

        model.addAttribute("fecha", fecha);
        return "reportes";
    }
}
