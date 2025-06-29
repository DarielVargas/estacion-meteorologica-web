package org.javadominicano.controladores;

import org.javadominicano.entidades.Alerta;
import org.javadominicano.repositorios.RepositorioAlerta;
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

    @GetMapping("/alertas")
    public String listarAlertas(Model model) {
        model.addAttribute("alertas", repoAlerta.findAll());
        return "alertas";
    }

    @PostMapping("/alertas/nueva")
    public String nuevaAlerta(@ModelAttribute Alerta alerta) {
        repoAlerta.save(alerta);
        return "redirect:/alertas";
    }

    @PostMapping("/alertas/editar")
    public String editarAlerta(@ModelAttribute Alerta alerta) {
        repoAlerta.save(alerta);
        return "redirect:/alertas";
    }

    @PostMapping("/alertas/eliminar")
    public String eliminarAlerta(@RequestParam Long id) {
        repoAlerta.deleteById(id);
        return "redirect:/alertas";
    }
}
