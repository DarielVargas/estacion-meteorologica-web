package org.javadominicano.controladores;

import org.javadominicano.entidades.DatosDireccion;
import org.javadominicano.entidades.DatosPrecipitacion;
import org.javadominicano.entidades.DatosVelocidad;
import org.javadominicano.visualizadorweb.entidades.DatosHumedad;
import org.javadominicano.visualizadorweb.entidades.DatosTemperatura;
import org.javadominicano.visualizadorweb.dto.MedicionesRecientesDTO;
import org.javadominicano.visualizadorweb.entidades.Umbrales;
import org.javadominicano.entidades.Alerta;
import org.javadominicano.entidades.EstacionMeteorologica;
import org.javadominicano.repositorios.RepositorioEstacionMeteorologica;

import org.javadominicano.repositorios.RepositorioDatosDireccion;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosTemperatura;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosPresion;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedadSuelo;
import org.javadominicano.repositorios.RepositorioAlerta;

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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.sql.Timestamp;

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

    @Autowired
    private RepositorioDatosPresion repositorioPresion;

    @Autowired
    private RepositorioDatosHumedadSuelo repositorioHumedadSuelo;

    @Autowired
    private RepositorioEstacionMeteorologica repositorioEstacion;

    @Autowired
    private RepositorioAlerta repoAlerta;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        PropertyEditorSupport editor = new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.isBlank()) {
                    setValue(0.0);
                } else {
                    setValue(Double.parseDouble(text.replace(',', '.')));
                }
            }
        };
        binder.registerCustomEditor(Double.class, editor);
        binder.registerCustomEditor(double.class, editor);
    }

    // Inyecta el objeto umbrales para Thymeleaf con valores por defecto
    @ModelAttribute("umbrales")
    public Umbrales obtenerUmbrales() {
        Umbrales umbrales = new Umbrales();
        Alerta temp   = repoAlerta.findByNombre("Temperatura");
        Alerta hum    = repoAlerta.findByNombre("Humedad");
        Alerta vel    = repoAlerta.findByNombre("VelocidadViento");
        Alerta pre    = repoAlerta.findByNombre("Precipitacion");
        Alerta pres   = repoAlerta.findByNombre("Presion");
        Alerta humSu  = repoAlerta.findByNombre("HumedadSuelo");

        umbrales.setTemperatura(temp != null ? temp.getUmbral() : 20.0);
        umbrales.setHumedad(hum != null ? hum.getUmbral() : 60.0);
        umbrales.setVelocidadViento(vel != null ? vel.getUmbral() : 10.0);
        umbrales.setPrecipitacion(pre != null ? pre.getUmbral() : 5.0);
        umbrales.setPresion(pres != null ? pres.getUmbral() : 1013.0);
        umbrales.setHumedadSuelo(humSu != null ? humSu.getUmbral() : 40.0);
        return umbrales;
    }

    // Inyecta la lista de estaciones para Thymeleaf
    @ModelAttribute("estaciones")
    public List<EstacionMeteorologica> getEstaciones() {
        return repositorioEstacion.findAll();
    }

    private Date obtenerUltimaFechaEstacion(String estacionId) {
        Date ultima = null;
        Timestamp t;

        t = repositorioTemperatura.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) {
            ultima = t;
        }

        t = repositorioHumedad.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) {
            ultima = t;
        }

        t = repositorioVelocidad.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) {
            ultima = t;
        }

        t = repositorioDireccion.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) {
            ultima = t;
        }

        t = repositorioPrecipitacion.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) {
            ultima = t;
        }

        t = repositorioPresion.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) {
            ultima = t;
        }

        t = repositorioHumedadSuelo.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) {
            ultima = t;
        }

        return ultima;
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
        mediciones.setPresion(
            repositorioPresion.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getPresion()
        );
        mediciones.setHumedadSuelo(
            repositorioHumedadSuelo.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getHumedad()
        );

        // Fecha límite para considerar una estación activa (30 segundos)
        Date ahora = new Date();
        Date fechaLimite = new Date(ahora.getTime() - 30_000L);

        List<EstacionMeteorologica> todasEstaciones = repositorioEstacion.findAll();
        int estacionesActivas = 0;
        int estacionesInactivas = 0;
        Date ultimaFechaActualizacion = new Date(0);
        List<EstacionMeteorologica> estacionesInactivasList = new ArrayList<>();
        Map<String, Boolean> estadoEstaciones = new HashMap<>();
        Map<String, Date> ultimaFechaPorEstacion = new HashMap<>();

        for (EstacionMeteorologica estacion : todasEstaciones) {
            Date ultimaFechaEstacion = obtenerUltimaFechaEstacion(estacion.getId());
            ultimaFechaPorEstacion.put(estacion.getId(), ultimaFechaEstacion);

            if (ultimaFechaEstacion != null && ultimaFechaEstacion.after(fechaLimite)) {
                estacionesActivas++;
                estadoEstaciones.put(estacion.getId(), true);
            } else {
                estacionesInactivas++;
                estadoEstaciones.put(estacion.getId(), false);
                estacionesInactivasList.add(estacion);
            }
            if (ultimaFechaEstacion != null && ultimaFechaEstacion.after(ultimaFechaActualizacion)) {
                ultimaFechaActualizacion = ultimaFechaEstacion;
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
        model.addAttribute("estadoEstaciones", estadoEstaciones);
        model.addAttribute("ultimaFechaPorEstacion", ultimaFechaPorEstacion);

        return "dashboard";
    }

    @PostMapping("/configurar-alertas")
    public String configurarAlertas(@ModelAttribute Umbrales umbrales,
                                    @RequestParam(required = false) Boolean chkTemp,
                                    @RequestParam(required = false) Boolean chkHum,
                                    @RequestParam(required = false) Boolean chkVel,
                                    @RequestParam(required = false) Boolean chkPre,
                                    @RequestParam(required = false) Boolean chkPres,
                                    @RequestParam(required = false) Boolean chkHumSu,
                                    Model model) {
        if (Boolean.TRUE.equals(chkTemp)) {
            guardarOActualizar("Temperatura", umbrales.getTemperatura());
        }
        if (Boolean.TRUE.equals(chkHum)) {
            guardarOActualizar("Humedad", umbrales.getHumedad());
        }
        if (Boolean.TRUE.equals(chkVel)) {
            guardarOActualizar("VelocidadViento", umbrales.getVelocidadViento());
        }
        if (Boolean.TRUE.equals(chkPre)) {
            guardarOActualizar("Precipitacion", umbrales.getPrecipitacion());
        }
        if (Boolean.TRUE.equals(chkPres)) {
            guardarOActualizar("Presion", umbrales.getPresion());
        }
        if (Boolean.TRUE.equals(chkHumSu)) {
            guardarOActualizar("HumedadSuelo", umbrales.getHumedadSuelo());
        }

        return "redirect:/"; // Redirige al dashboard para que se recargue
    }

    private void guardarOActualizar(String nombre, double umbral) {
        Alerta a = repoAlerta.findByNombre(nombre);
        if (a == null) {
            a = new Alerta();
            a.setNombre(nombre);
            a.setOperador(">");
            a.setPrioridad("Media");
            a.setActiva(true);
        }
        a.setUmbral(umbral);
        repoAlerta.save(a);
    }


    // Eliminar estación
    @PostMapping("/estaciones/eliminar")
    public String eliminarEstacion(@RequestParam String id) {
        repositorioEstacion.deleteById(id);
        return "redirect:/estaciones";
    }

    // Mostrar formulario de edición
    @GetMapping("/estaciones/editar")
    public String mostrarEditarEstacion(@RequestParam String id, Model model) {
        EstacionMeteorologica estacionOpt = repositorioEstacion.findById(id).orElse(null);

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
        repositorioEstacion.save(estacion);
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
        repositorioEstacion.save(estacion);
        return "redirect:/estaciones";
    }

    @PostMapping("/estaciones/toggle")
    public String toggleActiva(@RequestParam String id) {
        EstacionMeteorologica estacion = repositorioEstacion.findById(id).orElse(null);
        if (estacion != null) {
            estacion.setActiva(!estacion.isActiva());
            repositorioEstacion.save(estacion);
        }
        return "redirect:/estaciones";
    }

    // Listar estaciones en página independiente
    @GetMapping("/estaciones")
    public String listarEstaciones(Model model) {
        Date ahora = new Date();
        Date fechaLimite = new Date(ahora.getTime() - 30_000L);

        int estacionesActivas = 0;
        int estacionesInactivas = 0;
        Date ultimaFechaActualizacion = new Date(0);
        List<EstacionMeteorologica> todasEstaciones = repositorioEstacion.findAll();
        List<EstacionMeteorologica> estacionesInactivasList = new ArrayList<>();
        Map<String, Boolean> estadoEstaciones = new HashMap<>();
        Map<String, Date> ultimaFechaPorEstacion = new HashMap<>();

        for (EstacionMeteorologica estacion : todasEstaciones) {
            Date ultimaFechaEstacion = obtenerUltimaFechaEstacion(estacion.getId());
            ultimaFechaPorEstacion.put(estacion.getId(), ultimaFechaEstacion);

            if (ultimaFechaEstacion != null && ultimaFechaEstacion.after(fechaLimite)) {
                estacionesActivas++;
                estadoEstaciones.put(estacion.getId(), true);
            } else {
                estacionesInactivas++;
                estadoEstaciones.put(estacion.getId(), false);
                estacionesInactivasList.add(estacion);
            }
            if (ultimaFechaEstacion != null && ultimaFechaEstacion.after(ultimaFechaActualizacion)) {
                ultimaFechaActualizacion = ultimaFechaEstacion;
            }
        }

        model.addAttribute("estaciones", todasEstaciones);
        model.addAttribute("estacionesActivas", estacionesActivas);
        model.addAttribute("estacionesInactivas", estacionesInactivas);
        model.addAttribute("estacionesInactivasList", estacionesInactivasList);
        model.addAttribute("ultimaFechaActualizacion", ultimaFechaActualizacion);
        model.addAttribute("estadoEstaciones", estadoEstaciones);
        model.addAttribute("ultimaFechaPorEstacion", ultimaFechaPorEstacion);

        return "estaciones";
    }
}