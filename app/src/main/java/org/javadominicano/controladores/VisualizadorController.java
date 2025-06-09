package org.javadominicano.controladores;

import org.javadominicano.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VisualizadorController {

    @Autowired
    private RepositorioDatosVelocidad repoVelocidad;

    @Autowired
    private RepositorioDatosDireccion repoDireccion;

    @Autowired
    private RepositorioDatosPrecipitacion repoPrecipitacion;

    @GetMapping("/")
    public String verDashboard(Model model) {
        model.addAttribute("velocidades", repoVelocidad.findAllByOrderByFechaDesc());
        model.addAttribute("direcciones", repoDireccion.findAllByOrderByFechaDesc());
        model.addAttribute("precipitaciones", repoPrecipitacion.findAllByOrderByFechaDesc());
        return "dashboard";
    }
}


