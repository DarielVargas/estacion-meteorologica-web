package org.javadominicano.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// Repositorios bajo org.javadominicano.repositorios
import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.repositorios.RepositorioDatosDireccion;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
// Repositorios bajo org.javadominicano.visualizadorweb.repositorios
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosTemperatura;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosPresion;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedadSuelo;

@Controller
public class TablasController {

    @Autowired private RepositorioDatosVelocidad     repoVelocidad;
    @Autowired private RepositorioDatosDireccion     repoDireccion;
    @Autowired private RepositorioDatosPrecipitacion repoPrecipitacion;
    @Autowired private RepositorioDatosHumedad       repoHumedad;
    @Autowired private RepositorioDatosTemperatura   repoTemperatura;
    @Autowired private RepositorioDatosPresion       repoPresion;
    @Autowired private RepositorioDatosHumedadSuelo  repoHumedadSuelo;

    @GetMapping("/tablas")
    public String mostrarTablas(
        @RequestParam(name = "pagina", defaultValue = "0") int paginaActual,
        @RequestParam(name = "tamanoPagina", defaultValue = "10") int tamanoPagina,
        Model model
    ) {
        Sort ordenDesc = Sort.by("fecha").descending();
        PageRequest pr = PageRequest.of(paginaActual, tamanoPagina, ordenDesc);

        Page<?> velocidades     = repoVelocidad.findAll(pr);
        Page<?> direcciones     = repoDireccion.findAll(pr);
        Page<?> precipitaciones = repoPrecipitacion.findAll(pr);
        Page<?> humedades       = repoHumedad.findAll(pr);
        Page<?> temperaturas    = repoTemperatura.findAll(pr);

        Page<?> presiones;
        try {
            presiones = repoPresion.findAll(pr);
        } catch (Exception ex) {
            presiones = Page.empty();
        }

        Page<?> humedadesSuelo;
        try {
            humedadesSuelo = repoHumedadSuelo.findAll(pr);
        } catch (Exception ex) {
            humedadesSuelo = Page.empty();
        }

        model.addAttribute("velocidades",     velocidades);
        model.addAttribute("direcciones",     direcciones);
        model.addAttribute("precipitaciones", precipitaciones);
        model.addAttribute("humedades",       humedades);
        model.addAttribute("temperaturas",    temperaturas);
        model.addAttribute("presiones",       presiones);
        model.addAttribute("humedadesSuelo",  humedadesSuelo);
        model.addAttribute("paginaActual",    paginaActual);
        model.addAttribute("tamanoPagina",    tamanoPagina);

        return "tablas";
    }

}
