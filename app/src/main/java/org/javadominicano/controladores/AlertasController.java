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

        List<AlertaActivaDTO> activas = obtenerAlertasActivas();
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

    @PostMapping("/alertas/eliminar-todas")
    public String eliminarTodasLasAlertas() {
        repoAlerta.deleteAll();
        return "redirect:/alertas";
    }

    private List<AlertaActivaDTO> obtenerAlertasActivas() {
        List<AlertaActivaDTO> lista = new ArrayList<>();

        DatosTemperatura temp = repoTemperatura.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0);
        DatosHumedad hum = repoHumedad.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0);
        DatosVelocidad vel = repoVelocidad.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0);
        DatosPrecipitacion pre = repoPrecipitacion.findTopByOrderByFechaDesc(PageRequest.of(0, 1)).get(0);

        agregarAlertaActiva(lista, repoAlerta.findByNombre("Temperatura"), temp.getTemperatura(), temp.getFecha());
        agregarAlertaActiva(lista, repoAlerta.findByNombre("Humedad"), hum.getHumedad(), hum.getFecha());
        agregarAlertaActiva(lista, repoAlerta.findByNombre("VelocidadViento"), vel.getVelocidad(), vel.getFecha());
        agregarAlertaActiva(lista, repoAlerta.findByNombre("Precipitacion"), pre.getProbabilidad(), pre.getFecha());

        return lista;
    }

    private void agregarAlertaActiva(List<AlertaActivaDTO> lista, Alerta alerta, double valor, Timestamp fecha) {
        if (chequearAlerta(alerta, valor)) {
            AlertaActivaDTO dto = new AlertaActivaDTO();
            dto.setAlerta(alerta);
            dto.setValorActual(valor);
            dto.setFecha(fecha);
            lista.add(dto);
        }
    }

    private boolean chequearAlerta(Alerta alerta, Double valor) {
        if (alerta == null || valor == null || !alerta.isActiva()) {
            return false;
        }
        if (">".equals(alerta.getOperador())) {
            return valor > alerta.getUmbral();
        } else {
            return valor < alerta.getUmbral();
        }
    }
}