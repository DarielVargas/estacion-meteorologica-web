package org.javadominicano.controladores;

import org.javadominicano.entidades.EstacionMeteorologica;
import org.javadominicano.entidades.Notificacion;
import org.javadominicano.repositorios.RepositorioEstacionMeteorologica;
import org.javadominicano.repositorios.RepositorioNotificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ActividadController {

    @Autowired
    private RepositorioNotificacion repoNotificacion;

    @Autowired
    private RepositorioEstacionMeteorologica repoEstacion;

    @ModelAttribute("estaciones")
    public List<EstacionMeteorologica> getEstaciones() {
        return repoEstacion.findAll();
    }

    @GetMapping("/actividad")
    public String verActividad(
            @RequestParam(value = "fecha", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam(value = "estacion", required = false) String estacion,
            @RequestParam(name = "pagina", defaultValue = "0") int paginaActual,
            @RequestParam(name = "tamanoPagina", defaultValue = "10") int tamanoPagina,
            Model model) {

        Page<Notificacion> notificaciones = Page.empty();

        if (fecha != null) {
            LocalDateTime start = fecha.atStartOfDay();
            LocalDateTime end = start.plusDays(1);
            Timestamp inicio = Timestamp.valueOf(start);
            Timestamp fin = Timestamp.valueOf(end);

            Pageable pr = PageRequest.of(paginaActual, tamanoPagina, Sort.by("fecha").descending());
            if (estacion != null && !"all".equals(estacion)) {
                notificaciones = repoNotificacion.findByEstacionAndFechaBetween(estacion, inicio, fin, pr);
            } else {
                notificaciones = repoNotificacion.findByFechaBetween(inicio, fin, pr);
            }
        }

        model.addAttribute("notificaciones", notificaciones);
        model.addAttribute("fecha", fecha);
        model.addAttribute("estacionSeleccionada", estacion);
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("tamanoPagina", tamanoPagina);

        return "actividad";
    }
}