package org.javadominicano.controladores;

import org.javadominicano.entidades.Alerta;
import org.javadominicano.repositorios.RepositorioAlerta;
import org.javadominicano.visualizadorweb.entidades.Umbrales;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AlertasController {

    @Autowired
    private RepositorioAlerta repoAlerta;

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
}