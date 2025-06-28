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

@Controller
public class MedicionesController {

    @Autowired private RepositorioDatosVelocidad     repoVelocidad;
    @Autowired private RepositorioDatosDireccion     repoDireccion;
    @Autowired private RepositorioDatosPrecipitacion repoPrecipitacion;
    @Autowired private RepositorioDatosHumedad       repoHumedad;
    @Autowired private RepositorioDatosTemperatura   repoTemperatura;

    @GetMapping("/mediciones")
    public String mostrarMediciones(
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

        model.addAttribute("velocidades",     velocidades);
        model.addAttribute("direcciones",     direcciones);
        model.addAttribute("precipitaciones", precipitaciones);
        model.addAttribute("humedades",       humedades);
        model.addAttribute("temperaturas",    temperaturas);
        model.addAttribute("paginaActual",    paginaActual);
        model.addAttribute("tamanoPagina",    tamanoPagina);

        return "mediciones";
    }

}
