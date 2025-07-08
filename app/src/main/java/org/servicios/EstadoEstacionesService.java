//package org.servicios;

package org.javadominicano.servicios;

import org.javadominicano.dto.EstadoEstacionDTO;
import org.javadominicano.entidades.EstacionMeteorologica;
import org.javadominicano.repositorios.RepositorioEstacionMeteorologica;
import org.javadominicano.repositorios.RepositorioDatosDireccion;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosTemperatura;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosPresion;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedadSuelo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class EstadoEstacionesService {
    @Autowired
    private RepositorioDatosVelocidad repoVelocidad;
    @Autowired
    private RepositorioDatosDireccion repoDireccion;
    @Autowired
    private RepositorioDatosPrecipitacion repoPrecipitacion;
    @Autowired
    private RepositorioDatosHumedad repoHumedad;
    @Autowired
    private RepositorioDatosTemperatura repoTemperatura;
    @Autowired
    private RepositorioDatosPresion repoPresion;
    @Autowired
    private RepositorioDatosHumedadSuelo repoHumedadSuelo;
    @Autowired
    private RepositorioEstacionMeteorologica repoEstacion;

    public List<EstadoEstacionDTO> obtenerEstados() {
        Date ahora = new Date();
        Date limite = new Date(ahora.getTime() - 30_000L);
        List<EstadoEstacionDTO> lista = new ArrayList<>();
        for (EstacionMeteorologica e : repoEstacion.findAll()) {
            Date ultima = obtenerUltimaFechaEstacion(e.getId());
            boolean activa = ultima != null && ultima.after(limite);
            EstadoEstacionDTO dto = new EstadoEstacionDTO();
            dto.setId(e.getId());
            dto.setNombre(e.getNombre());
            dto.setActiva(activa);
            lista.add(dto);
        }
        return lista;
    }

    private Date obtenerUltimaFechaEstacion(String estacionId) {
        Date ultima = null;
        Timestamp t;
        t = repoTemperatura.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) {
            ultima = t;
        }
        t = repoHumedad.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) {
            ultima = t;
        }
        t = repoVelocidad.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) {
            ultima = t;
        }
        t = repoDireccion.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) {
            ultima = t;
        }
        t = repoPrecipitacion.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) {
            ultima = t;
        }
        t = repoPresion.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) {
            ultima = t;
        }
        t = repoHumedadSuelo.findUltimaFechaByEstacion(estacionId);
        if (t != null && (ultima == null || t.after(ultima))) {
            ultima = t;
        }
        return ultima;
    }}