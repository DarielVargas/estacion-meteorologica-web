package org.javadominicano.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.repositorios.RepositorioDatosDireccion;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosTemperatura;
import org.javadominicano.dto.ReporteGenerado;
import org.javadominicano.entidades.EstacionMeteorologica;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Controller
public class ReportesController {

    @Autowired private RepositorioDatosVelocidad repoVelocidad;
    @Autowired private RepositorioDatosDireccion repoDireccion;
    @Autowired private RepositorioDatosPrecipitacion repoPrecipitacion;
    @Autowired private RepositorioDatosHumedad repoHumedad;
    @Autowired private RepositorioDatosTemperatura repoTemperatura;

    private static List<EstacionMeteorologica> estaciones = new ArrayList<>();
    private static List<ReporteGenerado> reportesGenerados = new ArrayList<>();

    public ReportesController() {
        if (estaciones.isEmpty()) {
            estaciones.add(new EstacionMeteorologica("EST001", "Estación1", "Santiago"));
            estaciones.add(new EstacionMeteorologica("EST002", "Estación2", "Mao"));
        }
    }

    @ModelAttribute("estaciones")
    public List<EstacionMeteorologica> getEstaciones() {
        return estaciones;
    }

    @GetMapping("/reportes")
    public String mostrarReportes(
            @RequestParam(value = "fecha", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(value = "estacion", required = false) String estacion,
            @RequestParam(value = "tipo", required = false) String tipo,
            @RequestParam(name = "pagina", defaultValue = "0") int paginaActual,
            @RequestParam(name = "tamanoPagina", defaultValue = "10") int tamanoPagina,
            Model model) {

        Page<?> velocidades = Page.empty();
        Page<?> direcciones = Page.empty();
        Page<?> precipitaciones = Page.empty();
        Page<?> humedades = Page.empty();
        Page<?> temperaturas = Page.empty();

        if (fecha != null) {
            LocalDateTime start = fecha.atStartOfDay();
            LocalDateTime end = start.plusDays(1);
            Timestamp inicio = Timestamp.valueOf(start);
            Timestamp fin = Timestamp.valueOf(end);

            Sort sort = Sort.by("fecha").descending();
            PageRequest pr = PageRequest.of(paginaActual, tamanoPagina, sort);

            velocidades = repoVelocidad.findByFechaBetween(inicio, fin, pr);
            direcciones = repoDireccion.findByFechaBetween(inicio, fin, pr);
            precipitaciones = repoPrecipitacion.findByFechaBetween(inicio, fin, pr);
            humedades = repoHumedad.findByFechaBetween(inicio, fin, pr);
            temperaturas = repoTemperatura.findByFechaBetween(inicio, fin, pr);
        }

        if (fecha != null && tipo != null && estacion != null) {
            String titulo = "";
            if ("diario".equals(tipo)) {
                titulo = "Reporte Diario - " + fecha;
            } else if ("semanal".equals(tipo)) {
                LocalDate finSemana = fecha.plusDays(6);
                titulo = "Reporte Semanal - " + fecha + " a " + finSemana;
            } else if ("mensual".equals(tipo)) {
                String mes = fecha.getMonth().getDisplayName(TextStyle.FULL, new Locale("es"));
                titulo = "Reporte Mensual - " + mes + " " + fecha.getYear();
            }

            String estacionNombre = "all".equals(estacion) ? "Todas las estaciones" : estacion;
            reportesGenerados.add(0, new ReporteGenerado(titulo, estacionNombre));
        }

        model.addAttribute("velocidades", velocidades);
        model.addAttribute("direcciones", direcciones);
        model.addAttribute("precipitaciones", precipitaciones);
        model.addAttribute("humedades", humedades);
        model.addAttribute("temperaturas", temperaturas);
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("tamanoPagina", tamanoPagina);
        model.addAttribute("fecha", fecha);
        model.addAttribute("estacionSeleccionada", estacion);
        model.addAttribute("tipoSeleccionado", tipo);
        model.addAttribute("reportesGenerados", reportesGenerados);

        return "reportes";
    }
}