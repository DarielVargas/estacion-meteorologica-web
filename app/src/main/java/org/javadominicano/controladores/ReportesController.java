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
import java.io.ByteArrayOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.DoubleSummaryStatistics;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import org.javadominicano.entidades.DatosDireccion;
import org.javadominicano.entidades.DatosPrecipitacion;
import org.javadominicano.entidades.DatosVelocidad;
import org.javadominicano.visualizadorweb.entidades.DatosHumedad;
import org.javadominicano.visualizadorweb.entidades.DatosTemperatura;
import org.javadominicano.visualizadorweb.entidades.DatosPresion;
import org.javadominicano.visualizadorweb.entidades.DatosHumedadSuelo;

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
    @Autowired private SpringTemplateEngine templateEngine;

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
    public String verReporte(
            @PathVariable("id") int id,
            @RequestParam(name = "pagina", defaultValue = "0") int paginaActual,
            @RequestParam(name = "tamanoPagina", defaultValue = "10") int tamanoPagina,
            Model model) {
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

        Sort sort = Sort.by("fecha").descending();
        PageRequest pr = PageRequest.of(paginaActual, tamanoPagina, sort);

        model.addAttribute("reporte", rep);
        Page<DatosVelocidad> velocidadesPage = repoVelocidad.findByFechaBetween(inicio, fin, pr);
        Page<DatosDireccion> direccionesPage = repoDireccion.findByFechaBetween(inicio, fin, pr);
        Page<DatosPrecipitacion> precipitacionesPage = repoPrecipitacion.findByFechaBetween(inicio, fin, pr);
        Page<DatosHumedad> humedadesPage = repoHumedad.findByFechaBetween(inicio, fin, pr);
        Page<DatosTemperatura> temperaturasPage = repoTemperatura.findByFechaBetween(inicio, fin, pr);
        Page<DatosPresion> presionesPage = repoPresion.findByFechaBetween(inicio, fin, pr);
        Page<DatosHumedadSuelo> humedadesSueloPage = repoHumedadSuelo.findByFechaBetween(inicio, fin, pr);

        // Calcular dirección más frecuente
        String dirFrecuente = repoDireccion
                .findByFechaBetweenOrderByFechaDesc(inicio, fin)
                .stream()
                .collect(Collectors.groupingBy(d -> d.getDireccion(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        model.addAttribute("velocidades", velocidadesPage);
        model.addAttribute("direcciones", direccionesPage);
        model.addAttribute("precipitaciones", precipitacionesPage);
        model.addAttribute("humedades", humedadesPage);
        model.addAttribute("temperaturas", temperaturasPage);
        model.addAttribute("presiones", presionesPage);
        model.addAttribute("humedadesSuelo", humedadesSueloPage);
        model.addAttribute("dirFrecuente", dirFrecuente);

        DoubleSummaryStatistics statsVel = velocidadesPage.getContent().stream()
                .mapToDouble(DatosVelocidad::getVelocidad).summaryStatistics();
        DoubleSummaryStatistics statsPre = precipitacionesPage.getContent().stream()
                .mapToDouble(DatosPrecipitacion::getProbabilidad).summaryStatistics();
        DoubleSummaryStatistics statsHum = humedadesPage.getContent().stream()
                .mapToDouble(DatosHumedad::getHumedad).summaryStatistics();
        DoubleSummaryStatistics statsTem = temperaturasPage.getContent().stream()
                .mapToDouble(DatosTemperatura::getTemperatura).summaryStatistics();
        DoubleSummaryStatistics statsPreS = presionesPage.getContent().stream()
                .mapToDouble(DatosPresion::getPresion).summaryStatistics();
        DoubleSummaryStatistics statsHumS = humedadesSueloPage.getContent().stream()
                .mapToDouble(DatosHumedadSuelo::getHumedad).summaryStatistics();

        model.addAttribute("velMin", statsVel.getCount() > 0 ? statsVel.getMin() : null);
        model.addAttribute("velMax", statsVel.getCount() > 0 ? statsVel.getMax() : null);
        model.addAttribute("velAvg", statsVel.getCount() > 0 ? statsVel.getAverage() : null);
        model.addAttribute("preMin", statsPre.getCount() > 0 ? statsPre.getMin() : null);
        model.addAttribute("preMax", statsPre.getCount() > 0 ? statsPre.getMax() : null);
        model.addAttribute("preAvg", statsPre.getCount() > 0 ? statsPre.getAverage() : null);
        model.addAttribute("humMin", statsHum.getCount() > 0 ? statsHum.getMin() : null);
        model.addAttribute("humMax", statsHum.getCount() > 0 ? statsHum.getMax() : null);
        model.addAttribute("humAvg", statsHum.getCount() > 0 ? statsHum.getAverage() : null);
        model.addAttribute("temMin", statsTem.getCount() > 0 ? statsTem.getMin() : null);
        model.addAttribute("temMax", statsTem.getCount() > 0 ? statsTem.getMax() : null);
        model.addAttribute("temAvg", statsTem.getCount() > 0 ? statsTem.getAverage() : null);
        model.addAttribute("presMin", statsPreS.getCount() > 0 ? statsPreS.getMin() : null);
        model.addAttribute("presMax", statsPreS.getCount() > 0 ? statsPreS.getMax() : null);
        model.addAttribute("presAvg", statsPreS.getCount() > 0 ? statsPreS.getAverage() : null);
        model.addAttribute("humSMin", statsHumS.getCount() > 0 ? statsHumS.getMin() : null);
        model.addAttribute("humSMax", statsHumS.getCount() > 0 ? statsHumS.getMax() : null);
        model.addAttribute("humSAvg", statsHumS.getCount() > 0 ? statsHumS.getAverage() : null);
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("tamanoPagina", tamanoPagina);

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

        Sort sort = Sort.by("fecha").descending();
        PageRequest pr = PageRequest.of(0, Integer.MAX_VALUE, sort);

        Page<DatosVelocidad> velocidadesPage = repoVelocidad.findByFechaBetween(inicio, fin, pr);
        Page<DatosDireccion> direccionesPage = repoDireccion.findByFechaBetween(inicio, fin, pr);
        Page<DatosPrecipitacion> precipitacionesPage = repoPrecipitacion.findByFechaBetween(inicio, fin, pr);
        Page<DatosHumedad> humedadesPage = repoHumedad.findByFechaBetween(inicio, fin, pr);
        Page<DatosTemperatura> temperaturasPage = repoTemperatura.findByFechaBetween(inicio, fin, pr);
        Page<DatosPresion> presionesPage = repoPresion.findByFechaBetween(inicio, fin, pr);
        Page<DatosHumedadSuelo> humedadesSueloPage = repoHumedadSuelo.findByFechaBetween(inicio, fin, pr);

        String dirFrecuente = repoDireccion
                .findByFechaBetweenOrderByFechaDesc(inicio, fin)
                .stream()
                .collect(Collectors.groupingBy(d -> d.getDireccion(), Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);

        DoubleSummaryStatistics statsVel = velocidadesPage.getContent().stream()
                .mapToDouble(DatosVelocidad::getVelocidad).summaryStatistics();
        DoubleSummaryStatistics statsPre = precipitacionesPage.getContent().stream()
                .mapToDouble(DatosPrecipitacion::getProbabilidad).summaryStatistics();
        DoubleSummaryStatistics statsHum = humedadesPage.getContent().stream()
                .mapToDouble(DatosHumedad::getHumedad).summaryStatistics();
        DoubleSummaryStatistics statsTem = temperaturasPage.getContent().stream()
                .mapToDouble(DatosTemperatura::getTemperatura).summaryStatistics();
        DoubleSummaryStatistics statsPreS = presionesPage.getContent().stream()
                .mapToDouble(DatosPresion::getPresion).summaryStatistics();
        DoubleSummaryStatistics statsHumS = humedadesSueloPage.getContent().stream()
                .mapToDouble(DatosHumedadSuelo::getHumedad).summaryStatistics();

        Context ctx = new Context();
        ctx.setVariable("reporte", rep);
        ctx.setVariable("velocidades", velocidadesPage);
        ctx.setVariable("direcciones", direccionesPage);
        ctx.setVariable("precipitaciones", precipitacionesPage);
        ctx.setVariable("humedades", humedadesPage);
        ctx.setVariable("temperaturas", temperaturasPage);
        ctx.setVariable("presiones", presionesPage);
        ctx.setVariable("humedadesSuelo", humedadesSueloPage);
        ctx.setVariable("dirFrecuente", dirFrecuente);
        ctx.setVariable("velMin", statsVel.getCount() > 0 ? statsVel.getMin() : null);
        ctx.setVariable("velMax", statsVel.getCount() > 0 ? statsVel.getMax() : null);
        ctx.setVariable("velAvg", statsVel.getCount() > 0 ? statsVel.getAverage() : null);
        ctx.setVariable("preMin", statsPre.getCount() > 0 ? statsPre.getMin() : null);
        ctx.setVariable("preMax", statsPre.getCount() > 0 ? statsPre.getMax() : null);
        ctx.setVariable("preAvg", statsPre.getCount() > 0 ? statsPre.getAverage() : null);
        ctx.setVariable("humMin", statsHum.getCount() > 0 ? statsHum.getMin() : null);
        ctx.setVariable("humMax", statsHum.getCount() > 0 ? statsHum.getMax() : null);
        ctx.setVariable("humAvg", statsHum.getCount() > 0 ? statsHum.getAverage() : null);
        ctx.setVariable("temMin", statsTem.getCount() > 0 ? statsTem.getMin() : null);
        ctx.setVariable("temMax", statsTem.getCount() > 0 ? statsTem.getMax() : null);
        ctx.setVariable("temAvg", statsTem.getCount() > 0 ? statsTem.getAverage() : null);
        ctx.setVariable("presMin", statsPreS.getCount() > 0 ? statsPreS.getMin() : null);
        ctx.setVariable("presMax", statsPreS.getCount() > 0 ? statsPreS.getMax() : null);
        ctx.setVariable("presAvg", statsPreS.getCount() > 0 ? statsPreS.getAverage() : null);
        ctx.setVariable("humSMin", statsHumS.getCount() > 0 ? statsHumS.getMin() : null);
        ctx.setVariable("humSMax", statsHumS.getCount() > 0 ? statsHumS.getMax() : null);
        ctx.setVariable("humSAvg", statsHumS.getCount() > 0 ? statsHumS.getAverage() : null);

        String html = templateEngine.process("reportePreview", ctx);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PdfRendererBuilder builder = new PdfRendererBuilder();
        builder.withHtmlContent(html, "");
        builder.toStream(baos);
        builder.run();

        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reporte_" + id + ".pdf");
        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
        response.getOutputStream().write(baos.toByteArray());
    }}