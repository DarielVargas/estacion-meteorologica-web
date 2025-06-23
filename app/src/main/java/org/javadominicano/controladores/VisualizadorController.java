package org.javadominicano.controladores;

import org.javadominicano.entidades.DatosDireccion;
import org.javadominicano.entidades.DatosPrecipitacion;
import org.javadominicano.entidades.DatosVelocidad;
import org.javadominicano.visualizadorweb.entidades.DatosHumedad;
import org.javadominicano.visualizadorweb.entidades.DatosTemperatura;
import org.javadominicano.visualizadorweb.dto.MedicionesRecientesDTO;
import org.javadominicano.visualizadorweb.entidades.Umbrales;

import org.javadominicano.repositorios.RepositorioDatosDireccion;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosTemperatura;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VisualizadorController {

    @Autowired
    private RepositorioDatosVelocidad repositorioVelocidad;

    @Autowired
    private RepositorioDatosDireccion repositorioDireccion;

    @Autowired
    private RepositorioDatosPrecipitacion repositorioPrecipitacion;

    @Autowired
    private RepositorioDatosHumedad repositorioHumedad;

    @Autowired
    private RepositorioDatosTemperatura repositorioTemperatura;

    // Inyecta el objeto umbrales para Thymeleaf con valores por defecto
    @ModelAttribute("umbrales")
    public Umbrales obtenerUmbrales() {
        Umbrales umbrales = new Umbrales();
        umbrales.setTemperatura(20.0);
        umbrales.setHumedad(60.0);
        umbrales.setVelocidadViento(10.0);
        umbrales.setPrecipitacion(5.0);
        return umbrales;
    }

    @GetMapping("/")
    public String mostrarDashboard(@RequestParam(name = "pagina", defaultValue = "0") int pagina,
                                   @RequestParam(name = "tamanoPagina", defaultValue = "20") int tamanoPagina,
                                   Model model) {

        Pageable pageable = PageRequest.of(pagina, tamanoPagina, Sort.by("fecha").descending());

        Page<DatosVelocidad> velocidades = repositorioVelocidad.findAll(pageable);
        Page<DatosDireccion> direcciones = repositorioDireccion.findAll(pageable);
        Page<DatosPrecipitacion> precipitaciones = repositorioPrecipitacion.findAll(pageable);
        Page<DatosHumedad> humedades = repositorioHumedad.findAll(pageable);
        Page<DatosTemperatura> temperaturas = repositorioTemperatura.findAll(pageable);

        // Últimas mediciones
        MedicionesRecientesDTO mediciones = new MedicionesRecientesDTO();
        mediciones.setTemperatura(
            repositorioTemperatura.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getTemperatura()
        );
        mediciones.setHumedad(
            repositorioHumedad.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getHumedad()
        );
        mediciones.setVelocidadViento(
            repositorioVelocidad.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getVelocidad()
        );
        mediciones.setDireccionViento(
            repositorioDireccion.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getDireccion()
        );
        mediciones.setPrecipitacion(
            repositorioPrecipitacion.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getProbabilidad()
        );

        model.addAttribute("velocidades", velocidades);
        model.addAttribute("direcciones", direcciones);
        model.addAttribute("precipitaciones", precipitaciones);
        model.addAttribute("humedades", humedades);
        model.addAttribute("temperaturas", temperaturas);
        model.addAttribute("mediciones", mediciones);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("tamanoPagina", tamanoPagina);

        return "dashboard";
    }

    @PostMapping("/configurar-alertas")
    public String configurarAlertas(@ModelAttribute Umbrales umbrales, Model model) {
        System.out.println("🔔 Nuevos umbrales configurados:");
        System.out.println("Temperatura > " + umbrales.getTemperatura());
        System.out.println("Humedad > " + umbrales.getHumedad());
        System.out.println("Velocidad Viento > " + umbrales.getVelocidadViento());
        System.out.println("Precipitación > " + umbrales.getPrecipitacion());

        // Aquí puedes agregar lógica para guardar los umbrales o procesarlos

        return "redirect:/"; // Redirige al dashboard para que se recargue
    }
}
