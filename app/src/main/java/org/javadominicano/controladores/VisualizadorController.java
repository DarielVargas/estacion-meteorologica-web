package org.javadominicano.controladores;

import org.javadominicano.entidades.DatosDireccion;
import org.javadominicano.entidades.DatosPrecipitacion;
import org.javadominicano.entidades.DatosVelocidad;
import org.javadominicano.visualizadorweb.entidades.DatosHumedad;
import org.javadominicano.visualizadorweb.entidades.DatosTemperatura;
import org.javadominicano.visualizadorweb.dto.MedicionesRecientesDTO;
import org.javadominicano.visualizadorweb.entidades.Umbrales;
import org.javadominicano.entidades.EstacionMeteorologica;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    // Lista estática para simular base de datos de estaciones
    private static List<EstacionMeteorologica> estaciones = new ArrayList<>();

    // Inicializa la lista si está vacía
    public VisualizadorController() {
        if (estaciones.isEmpty()) {
            estaciones.add(new EstacionMeteorologica("EST001", "Estación1", "Santiago"));
            estaciones.add(new EstacionMeteorologica("EST002", "Estación2", "Mao"));
        }
    }

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

    // Inyecta la lista de estaciones para Thymeleaf
    @ModelAttribute("estaciones")
    public List<EstacionMeteorologica> getEstaciones() {
        return estaciones;
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

        // Fecha límite para considerar una estación activa (hace 6 horas)
        Date ahora = new Date();
        Date fechaLimite = new Date(ahora.getTime() - 6L * 3600 * 1000); // 6 horas en milis

        int estacionesActivas = 0;
        int estacionesInactivas = 0;
        Date ultimaFechaActualizacion = new Date(0); // Epoch como valor inicial mínimo

        List<EstacionMeteorologica> estacionesInactivasList = new ArrayList<>();

        for (EstacionMeteorologica estacion : estaciones) {
            // Aquí debes implementar la lógica real para obtener la última fecha de datos de cada estación,
            // por ejemplo consultando repositorioVelocidad u otros.
            // Por simplicidad, usamos ahora como última fecha para que funcione.

            Date ultimaFechaEstacion = ahora; // <- reemplaza esta línea con la consulta real a datos de la estación

            if (ultimaFechaEstacion != null) {
                if (ultimaFechaEstacion.after(fechaLimite)) {
                    estacionesActivas++;
                } else {
                    estacionesInactivas++;
                    estacionesInactivasList.add(estacion);
                }
                if (ultimaFechaEstacion.after(ultimaFechaActualizacion)) {
                    ultimaFechaActualizacion = ultimaFechaEstacion;
                }
            } else {
                estacionesInactivas++;
                estacionesInactivasList.add(estacion);
            }
        }

        model.addAttribute("velocidades", velocidades);
        model.addAttribute("direcciones", direcciones);
        model.addAttribute("precipitaciones", precipitaciones);
        model.addAttribute("humedades", humedades);
        model.addAttribute("temperaturas", temperaturas);
        model.addAttribute("mediciones", mediciones);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("tamanoPagina", tamanoPagina);

        // Nuevos atributos para la vista
        model.addAttribute("estacionesActivas", estacionesActivas);
        model.addAttribute("estacionesInactivas", estacionesInactivas);
        model.addAttribute("estacionesInactivasList", estacionesInactivasList);
        model.addAttribute("ultimaFechaActualizacion", ultimaFechaActualizacion);

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

    // Eliminar estación
    @PostMapping("/estaciones/eliminar")
    public String eliminarEstacion(@RequestParam String id) {
        estaciones.removeIf(est -> est.getId().equals(id));
        return "redirect:/estaciones";
    }

    // Mostrar formulario de edición
    @GetMapping("/estaciones/editar")
    public String mostrarEditarEstacion(@RequestParam String id, Model model) {
        EstacionMeteorologica estacionOpt = estaciones.stream()
                .filter(est -> est.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (estacionOpt != null) {
            model.addAttribute("estacion", estacionOpt);
            return "editarEstacion";  // Debes crear esta vista Thymeleaf
        } else {
            return "redirect:/";
        }
    }

    // Guardar estación editada
    @PostMapping("/estaciones/editar")
    public String guardarEstacionEditada(@ModelAttribute EstacionMeteorologica estacion) {
        for (int i = 0; i < estaciones.size(); i++) {
            if (estaciones.get(i).getId().equals(estacion.getId())) {
                estaciones.set(i, estacion);
                break;
            }
        }
        return "redirect:/estaciones";
    }

    // Mostrar formulario nueva estación
    @GetMapping("/estaciones/nueva")
    public String mostrarFormularioNuevaEstacion(Model model) {
        EstacionMeteorologica nuevaEstacion = new EstacionMeteorologica();
        model.addAttribute("estacion", nuevaEstacion);
        return "form-estacion"; // Debes crear esta vista Thymeleaf para el formulario
    }

    // Guardar nueva estación
    @PostMapping("/estaciones/nueva")
    public String guardarNuevaEstacion(@ModelAttribute EstacionMeteorologica estacion) {
        estaciones.add(estacion);
        return "redirect:/estaciones";
    }

    // Listar estaciones en página independiente
    @GetMapping("/estaciones")
    public String listarEstaciones(Model model) {
        Date ahora = new Date();
        Date fechaLimite = new Date(ahora.getTime() - 6L * 3600 * 1000);

        int estacionesActivas = 0;
        int estacionesInactivas = 0;
        Date ultimaFechaActualizacion = new Date(0);
        List<EstacionMeteorologica> estacionesInactivasList = new ArrayList<>();

        for (EstacionMeteorologica estacion : estaciones) {
            Date ultimaFechaEstacion = ahora; // reemplazar con consulta real

            if (ultimaFechaEstacion != null) {
                if (ultimaFechaEstacion.after(fechaLimite)) {
                    estacionesActivas++;
                } else {
                    estacionesInactivas++;
                    estacionesInactivasList.add(estacion);
                }
                if (ultimaFechaEstacion.after(ultimaFechaActualizacion)) {
                    ultimaFechaActualizacion = ultimaFechaEstacion;
                }
            } else {
                estacionesInactivas++;
                estacionesInactivasList.add(estacion);
            }
        }

        model.addAttribute("estaciones", estaciones);
        model.addAttribute("estacionesActivas", estacionesActivas);
        model.addAttribute("estacionesInactivas", estacionesInactivas);
        model.addAttribute("estacionesInactivasList", estacionesInactivasList);
        model.addAttribute("ultimaFechaActualizacion", ultimaFechaActualizacion);

        return "estaciones";
    }
}