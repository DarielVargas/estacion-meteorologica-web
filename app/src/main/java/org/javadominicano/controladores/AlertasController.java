package org.javadominicano.controladores;

import org.javadominicano.entidades.Alerta;
import org.javadominicano.repositorios.RepositorioAlerta;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosTemperatura;
import org.javadominicano.visualizadorweb.entidades.Umbrales;
import org.javadominicano.entidades.DatosPrecipitacion;
import org.javadominicano.entidades.DatosVelocidad;
import org.javadominicano.visualizadorweb.entidades.DatosHumedad;
import org.javadominicano.visualizadorweb.entidades.DatosTemperatura;
import org.javadominicano.dto.AlertaActivaDTO;
import org.javadominicano.servicios.AlertasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.PageRequest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AlertasController {

    @Autowired
    private RepositorioAlerta repoAlerta;

    @Autowired
    private RepositorioDatosVelocidad repoVelocidad;

    @Autowired
    private RepositorioDatosHumedad repoHumedad;

    @Autowired
    private RepositorioDatosTemperatura repoTemperatura;

    @Autowired
    private RepositorioDatosPrecipitacion repoPrecipitacion;

    @Autowired
    private AlertasService alertasService;

    @ModelAttribute("umbrales")
    public Umbrales obtenerUmbrales() {
        Umbrales umbrales = new Umbrales();
        Alerta temp = repoAlerta.findByNombre("Temperatura");
        Alerta hum  = repoAlerta.findByNombre("Humedad");
        Alerta vel  = repoAlerta.findByNombre("VelocidadViento");
        Alerta pre  = repoAlerta.findByNombre("Precipitacion");

        umbrales.setTemperatura(temp != null ? temp.getUmbral() : 20.0);
        umbrales.setHumedad(hum != null ? hum.getUmbral() : 60.0);
        umbrales.setVelocidadViento(vel != null ? vel.getUmbral() : 10.0);
        umbrales.setPrecipitacion(pre != null ? pre.getUmbral() : 5.0);
        return umbrales;
    }

    @GetMapping("/alertas")
    public String listarAlertas(Model model) {
        model.addAttribute("alertas", repoAlerta.findAll());
        model.addAttribute("nuevaAlerta", new Alerta());

        List<AlertaActivaDTO> activas = alertasService.obtenerAlertasActivas();
        model.addAttribute("alertasActivas", activas);

        return "alertas";
    }

    @PostMapping("/alertas/guardar")
    public String guardarAlerta(@ModelAttribute Alerta alerta) {
        repoAlerta.save(alerta);
        return "redirect:/alertas";
    }

    @PostMapping("/alertas/eliminar")
    public String eliminarAlerta(@RequestParam Long id) {
        repoAlerta.deleteById(id);
        return "redirect:/alertas";
    }

    @PostMapping("/alertas/eliminar-activas")
    public String eliminarAlertasActivas(Model model) {
        model.addAttribute("alertas", repoAlerta.findAll());
        model.addAttribute("nuevaAlerta", new Alerta());
        model.addAttribute("alertasActivas", new ArrayList<AlertaActivaDTO>());
        return "alertas";
    }

    @PostMapping("/alertas/eliminar-configuradas")
    public String eliminarAlertasConfiguradas() {
        repoAlerta.deleteAll();
        return "redirect:/alertas";
    }

}