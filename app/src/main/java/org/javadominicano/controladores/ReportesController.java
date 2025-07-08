package org.javadominicano.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.repositorios.RepositorioDatosDireccion;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosTemperatura;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosPresion;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedadSuelo;
import org.javadominicano.dto.ReporteGenerado;
import org.javadominicano.entidades.EstacionMeteorologica;
import org.javadominicano.repositorios.RepositorioEstacionMeteorologica;

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
    @Autowired private RepositorioDatosPresion repoPresion;
    @Autowired private RepositorioDatosHumedadSuelo repoHumedadSuelo;
    @Autowired private RepositorioEstacionMeteorologica repoEstacion;

    private static List<ReporteGenerado> reportesGenerados = new ArrayList<>();
    private static int nextId = 1;

    @ModelAttribute("estaciones")
    public List<EstacionMeteorologica> getEstaciones() {
        return repoEstacion.findAll();
    }

    @PostMapping("/reportes")
    public String generarReporte(
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam("estacion") String estacion,
            @RequestParam("tipo") String tipo) {

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
        ReporteGenerado rep = new ReporteGenerado(nextId++, titulo, estacionNombre, fecha, tipo);
        reportesGenerados.add(0, rep);

        return "redirect:/reportes?fecha=" + fecha + "&estacion=" + estacion + "&tipo=" + tipo;
    }

    @PostMapping("/reportes/limpiar")
    public String limpiarHistorial() {
        reportesGenerados.clear();
        return "redirect:/reportes";
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
        Page<?> presiones = Page.empty();
        Page<?> humedadesSuelo = Page.empty();

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
            presiones = repoPresion.findByFechaBetween(inicio, fin, pr);
            humedadesSuelo = repoHumedadSuelo.findByFechaBetween(inicio, fin, pr);
        }



        model.addAttribute("velocidades", velocidades);
        model.addAttribute("direcciones", direcciones);
        model.addAttribute("precipitaciones", precipitaciones);
        model.addAttribute("humedades", humedades);
        model.addAttribute("temperaturas", temperaturas);
        model.addAttribute("presiones", presiones);
        model.addAttribute("humedadesSuelo", humedadesSuelo);
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("tamanoPagina", tamanoPagina);
        model.addAttribute("fecha", fecha);
        model.addAttribute("estacionSeleccionada", estacion);
        model.addAttribute("tipoSeleccionado", tipo);
        model.addAttribute("reportesGenerados", reportesGenerados);

        return "reportes";
    }

    @GetMapping("/reportes/ver/{id}")
    public String verReporte(@PathVariable("id") int id, Model model) {
        ReporteGenerado rep = reportesGenerados.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
        if (rep == null) {
            return "redirect:/reportes";
        }

        LocalDateTime inicioLdt = rep.getFecha().atStartOfDay();
        LocalDateTime finLdt = inicioLdt.plusDays(1);
        if ("semanal".equals(rep.getTipo())) {
            finLdt = inicioLdt.plusDays(7);
        } else if ("mensual".equals(rep.getTipo())) {
            inicioLdt = rep.getFecha().withDayOfMonth(1).atStartOfDay();
            finLdt = inicioLdt.plusMonths(1);
        }

        Timestamp inicio = Timestamp.valueOf(inicioLdt);
        Timestamp fin = Timestamp.valueOf(finLdt);

        model.addAttribute("reporte", rep);
        model.addAttribute("velocidades", repoVelocidad.findByFechaBetweenOrderByFechaDesc(inicio, fin));
        model.addAttribute("direcciones", repoDireccion.findByFechaBetweenOrderByFechaDesc(inicio, fin));
        model.addAttribute("precipitaciones", repoPrecipitacion.findByFechaBetweenOrderByFechaDesc(inicio, fin));
        model.addAttribute("humedades", repoHumedad.findByFechaBetweenOrderByFechaDesc(inicio, fin));
        model.addAttribute("temperaturas", repoTemperatura.findByFechaBetweenOrderByFechaDesc(inicio, fin));
        model.addAttribute("presiones", repoPresion.findByFechaBetweenOrderByFechaDesc(inicio, fin));
        model.addAttribute("humedadesSuelo", repoHumedadSuelo.findByFechaBetweenOrderByFechaDesc(inicio, fin));

        return "reportePreview";
    }

    @GetMapping("/reportes/descargar/{id}")
    public void descargarReporte(@PathVariable("id") int id, HttpServletResponse response) throws java.io.IOException {
        ReporteGenerado rep = reportesGenerados.stream()
                .filter(r -> r.getId() == id)
                .findFirst()
                .orElse(null);
        if (rep == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        LocalDateTime inicioLdt = rep.getFecha().atStartOfDay();
        LocalDateTime finLdt = inicioLdt.plusDays(1);
        if ("semanal".equals(rep.getTipo())) {
            finLdt = inicioLdt.plusDays(7);
        } else if ("mensual".equals(rep.getTipo())) {
            inicioLdt = rep.getFecha().withDayOfMonth(1).atStartOfDay();
            finLdt = inicioLdt.plusMonths(1);
        }

        Timestamp inicio = Timestamp.valueOf(inicioLdt);
        Timestamp fin = Timestamp.valueOf(finLdt);

        StringBuilder csv = new StringBuilder();
        csv.append("VelocidadID,Velocidad,Fecha\n");
        repoVelocidad.findByFechaBetweenOrderByFechaDesc(inicio, fin)
            .forEach(v -> csv.append(v.getId()).append(',').append(v.getVelocidad()).append(',').append(v.getFecha()).append('\n'));
        csv.append("\nDireccionID,Direccion,Fecha\n");
        repoDireccion.findByFechaBetweenOrderByFechaDesc(inicio, fin)
            .forEach(d -> csv.append(d.getId()).append(',').append(d.getDireccion()).append(',').append(d.getFecha()).append('\n'));
        csv.append("\nPrecipitacionID,mm,Fecha\n");
        repoPrecipitacion.findByFechaBetweenOrderByFechaDesc(inicio, fin)
            .forEach(p -> csv.append(p.getId()).append(',').append(p.getProbabilidad()).append(',').append(p.getFecha()).append('\n'));
        csv.append("\nHumedadID,Humedad,Fecha\n");
        repoHumedad.findByFechaBetweenOrderByFechaDesc(inicio, fin)
            .forEach(h -> csv.append(h.getId()).append(',').append(h.getHumedad()).append(',').append(h.getFecha()).append('\n'));
        csv.append("\nTemperaturaID,Temperatura,Fecha\n");
        repoTemperatura.findByFechaBetweenOrderByFechaDesc(inicio, fin)
            .forEach(t -> csv.append(t.getId()).append(',').append(t.getTemperatura()).append(',').append(t.getFecha()).append('\n'));
        csv.append("\nPresionID,hPa,Fecha\n");
        repoPresion.findByFechaBetweenOrderByFechaDesc(inicio, fin)
            .forEach(p -> csv.append(p.getId()).append(',').append(p.getPresion()).append(',').append(p.getFecha()).append('\n'));
        csv.append("\nHumedadSueloID,HumedadSuelo,Fecha\n");
        repoHumedadSuelo.findByFechaBetweenOrderByFechaDesc(inicio, fin)
            .forEach(hs -> csv.append(hs.getId()).append(',').append(hs.getHumedadSuelo()).append(',').append(hs.getFecha()).append('\n'));

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_" + id + ".csv");
        response.setContentType(MediaType.TEXT_PLAIN_VALUE);
        response.getWriter().write(csv.toString());
    }}