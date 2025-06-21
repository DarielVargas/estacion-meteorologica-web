package org.javadominicano.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.repositorios.RepositorioDatosDireccion;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;

@Controller
public class DashboardController {

    @Autowired
    private RepositorioDatosVelocidad repoVelocidad;

    @Autowired
    private RepositorioDatosDireccion repoDireccion;

    @Autowired
    private RepositorioDatosPrecipitacion repoPrecipitacion;

    @GetMapping("/dashboard")
    public String mostrarDashboard(Model model, Pageable pageable) {
        int paginaActual = pageable.getPageNumber();
        int tamanoPagina = pageable.getPageSize();

        model.addAttribute("velocidades", repoVelocidad.findAll(pageable));
        model.addAttribute("direcciones", repoDireccion.findAll(pageable));
        model.addAttribute("precipitaciones", repoPrecipitacion.findAll(pageable));
        model.addAttribute("paginaActual", paginaActual);
        model.addAttribute("tamanoPagina", tamanoPagina);

        return "dashboard";
    }
}
