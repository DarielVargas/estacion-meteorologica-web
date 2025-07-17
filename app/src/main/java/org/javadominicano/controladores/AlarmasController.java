package org.javadominicano.controladores;

import org.javadominicano.entidades.Alarma;
import org.javadominicano.entidades.TipoSensor;
import org.javadominicano.repositorios.RepositorioAlarma;
import org.javadominicano.repositorios.RepositorioEstacionMeteorologica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;

@Controller
public class AlarmasController {
    @Autowired
    private RepositorioAlarma repoAlarma;
    @Autowired
    private RepositorioEstacionMeteorologica repoEstacion;

    @ModelAttribute("sensores")
    public List<TipoSensor> sensores() {
        return Arrays.asList(TipoSensor.values());
    }

    @ModelAttribute("estaciones")
    public List<?> estaciones() {
        return repoEstacion.findAll();
    }

    @GetMapping("/alarmas")
    public String listar(Model model) {
        model.addAttribute("alarmas", repoAlarma.findAll());
        model.addAttribute("nuevaAlarma", new Alarma());
        return "alarmas";
    }

    @PostMapping("/alarmas/guardar")
    public String guardar(@ModelAttribute Alarma alarma) {
        repoAlarma.save(alarma);
        return "redirect:/alarmas";
    }

    @PostMapping("/alarmas/eliminar")
    public String eliminar(@RequestParam Long id) {
        repoAlarma.deleteById(id);
        return "redirect:/alarmas";
    }

    @PostMapping("/alarmas/toggle")
    public String toggle(@RequestParam Long id) {
        repoAlarma.findById(id).ifPresent(a -> { a.setActiva(!a.isActiva()); repoAlarma.save(a);});
        return "redirect:/alarmas";
    }
}
