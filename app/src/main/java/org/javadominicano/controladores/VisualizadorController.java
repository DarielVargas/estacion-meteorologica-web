package org.javadominicano.controladores;

import org.javadominicano.repositorios.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VisualizadorController {

    @Autowired
    private RepositorioDatosVelocidad repoVelocidad;

    @Autowired
    private RepositorioDatosDireccion repoDireccion;

    @Autowired
    private RepositorioDatosPrecipitacion repoPrecipitacion;

    @GetMapping("/")
    public String verDashboard(Model model,
                               @RequestParam(defaultValue = "0") int pagina,
                               @RequestParam(defaultValue = "20") int tamano) {

        Pageable pageable = PageRequest.of(pagina, tamano);

        Page<?> velocidades = repoVelocidad.findAllByOrderByFechaDesc(pageable);
        Page<?> direcciones = repoDireccion.findAllByOrderByFechaDesc(pageable);
        Page<?> precipitaciones = repoPrecipitacion.findAllByOrderByFechaDesc(pageable);

        model.addAttribute("velocidades", velocidades);
        model.addAttribute("direcciones", direcciones);
        model.addAttribute("precipitaciones", precipitaciones);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("tamanoPagina", tamano); // Esta línea es crucial

        return "dashboard";
    }
}
