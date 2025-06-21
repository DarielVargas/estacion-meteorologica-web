package org.javadominicano.controladores;

import org.javadominicano.entidades.DatosDireccion;
import org.javadominicano.entidades.DatosPrecipitacion;
import org.javadominicano.entidades.DatosVelocidad;

import org.javadominicano.repositorios.RepositorioDatosDireccion;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
import org.javadominicano.repositorios.RepositorioDatosVelocidad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VisualizadorController {

    @Autowired
    private RepositorioDatosVelocidad repositorioVelocidad;

    @Autowired
    private RepositorioDatosDireccion repositorioDireccion;

    @Autowired
    private RepositorioDatosPrecipitacion repositorioPrecipitacion;

    @GetMapping("/")
    public String mostrarDashboard(@RequestParam(defaultValue = "0") int pagina,
                                   @RequestParam(defaultValue = "20") int tamano,
                                   Model model) {

        Pageable pageable = PageRequest.of(pagina, tamano, Sort.by("fecha").descending());

        Page<DatosVelocidad> velocidades = repositorioVelocidad.findAll(pageable);
        Page<DatosDireccion> direcciones = repositorioDireccion.findAll(pageable);
        Page<DatosPrecipitacion> precipitaciones = repositorioPrecipitacion.findAll(pageable);

        model.addAttribute("velocidades", velocidades);
        model.addAttribute("direcciones", direcciones);
        model.addAttribute("precipitaciones", precipitaciones);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("tamanoPagina", tamano);

        return "dashboard";
    }
}
