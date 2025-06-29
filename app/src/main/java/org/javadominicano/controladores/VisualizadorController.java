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
import org.javadominicano.servicio.ServicioAlertas;

import org.javadominicano.repositorios.RepositorioDatosDireccion;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosTemperatura;
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

    @Autowired
    private RepositorioEstacionMeteorologica repositorioEstacion;

    @Autowired
    private RepositorioAlerta repoAlerta;

    @Autowired
    private ServicioAlertas servicioAlertas;

    // Inyecta el objeto umbrales para Thymeleaf con valores por defecto
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

    // Inyecta la lista de estaciones para Thymeleaf
    @ModelAttribute("estaciones")
    public List<EstacionMeteorologica> getEstaciones() {
        return repositorioEstacion.findAll();
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

        List<EstacionMeteorologica> todasEstaciones = repositorioEstacion.findAll();
        int estacionesActivas = 0;
        int estacionesInactivas = 0;
        Date ultimaFechaActualizacion = new Date(0); // Epoch como valor inicial mínimo

        List<EstacionMeteorologica> estacionesInactivasList = new ArrayList<>();

        for (EstacionMeteorologica estacion : todasEstaciones) {
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

        List<String> alertasActivas = new ArrayList<>();
        Alerta alTemp = repoAlerta.findByNombre("Temperatura");
        if (chequearAlerta(alTemp, mediciones.getTemperatura())) {
            alertasActivas.add(formatoAlerta(alTemp));
        }
        Alerta alHum = repoAlerta.findByNombre("Humedad");
        if (chequearAlerta(alHum, mediciones.getHumedad())) {
            alertasActivas.add(formatoAlerta(alHum));
        }
        Alerta alVel = repoAlerta.findByNombre("VelocidadViento");
        if (chequearAlerta(alVel, mediciones.getVelocidadViento())) {
            alertasActivas.add(formatoAlerta(alVel));
        }
        Alerta alPre = repoAlerta.findByNombre("Precipitacion");
        if (chequearAlerta(alPre, mediciones.getPrecipitacion())) {
            alertasActivas.add(formatoAlerta(alPre));
        }

        model.addAttribute("velocidades", velocidades);
        model.addAttribute("direcciones", direcciones);
        model.addAttribute("precipitaciones", precipitaciones);
        model.addAttribute("humedades", humedades);
        model.addAttribute("temperaturas", temperaturas);
        model.addAttribute("mediciones", mediciones);
        model.addAttribute("paginaActual", pagina);
        model.addAttribute("tamanoPagina", tamanoPagina);
        model.addAttribute("alertasActivas", alertasActivas);

        // Nuevos atributos para la vista
        model.addAttribute("estacionesActivas", estacionesActivas);
        model.addAttribute("estacionesInactivas", estacionesInactivas);
        model.addAttribute("estacionesInactivasList", estacionesInactivasList);
        model.addAttribute("ultimaFechaActualizacion", ultimaFechaActualizacion);

        return "dashboard";
    }

    @PostMapping("/configurar-alertas")
    public String configurarAlertas(@ModelAttribute Umbrales umbrales, Model model) {
        guardarOActualizar("Temperatura", umbrales.getTemperatura());
        guardarOActualizar("Humedad", umbrales.getHumedad());
        guardarOActualizar("VelocidadViento", umbrales.getVelocidadViento());
        guardarOActualizar("Precipitacion", umbrales.getPrecipitacion());

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

    private String formatoAlerta(Alerta a) {
        return servicioAlertas.descripcion(a);
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

    // Listar estaciones en página independiente
    @GetMapping("/estaciones")
    public String listarEstaciones(Model model) {
        Date ahora = new Date();
        Date fechaLimite = new Date(ahora.getTime() - 6L * 3600 * 1000);

        int estacionesActivas = 0;
        int estacionesInactivas = 0;
        Date ultimaFechaActualizacion = new Date(0);
        List<EstacionMeteorologica> todasEstaciones = repositorioEstacion.findAll();
        List<EstacionMeteorologica> estacionesInactivasList = new ArrayList<>();

        for (EstacionMeteorologica estacion : todasEstaciones) {
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

        model.addAttribute("estaciones", todasEstaciones);
        model.addAttribute("estacionesActivas", estacionesActivas);
        model.addAttribute("estacionesInactivas", estacionesInactivas);
        model.addAttribute("estacionesInactivasList", estacionesInactivasList);
        model.addAttribute("ultimaFechaActualizacion", ultimaFechaActualizacion);

        return "estaciones";
    }
}