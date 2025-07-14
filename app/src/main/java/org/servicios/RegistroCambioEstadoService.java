package org.javadominicano.servicios;

import org.javadominicano.dto.EstadoEstacionDTO;
import org.javadominicano.entidades.Notificacion;
import org.javadominicano.repositorios.RepositorioNotificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RegistroCambioEstadoService {
    @Autowired
    private EstadoEstacionesService estadoService;

    @Autowired
    private RepositorioNotificacion repoNotificacion;

    private final Map<String, Boolean> estadosPrevios = new HashMap<>();

    @Scheduled(fixedRate = 5000)
    public void verificarCambios() {
        List<EstadoEstacionDTO> estados = estadoService.obtenerEstados();
        for (EstadoEstacionDTO dto : estados) {
            Boolean previo = estadosPrevios.get(dto.getId());
            if (previo != null && previo != dto.isActiva()) {
                registrarCambio(dto);
            }
            estadosPrevios.put(dto.getId(), dto.isActiva());
        }
    }

    private void registrarCambio(EstadoEstacionDTO dto) {
        Notificacion n = new Notificacion();
        n.setEstacion(dto.getNombre());
        if (dto.isActiva()) {
            n.setMensaje("La estación " + dto.getNombre() + " se ha reconectado correctamente.");
            n.setEstado("ACTIVA");
        } else {
            n.setMensaje("La estación " + dto.getNombre() + " se ha desconectado.");
            n.setEstado("INACTIVA");
        }
        n.setFecha(new Timestamp(System.currentTimeMillis()));
        repoNotificacion.save(n);
    }
}