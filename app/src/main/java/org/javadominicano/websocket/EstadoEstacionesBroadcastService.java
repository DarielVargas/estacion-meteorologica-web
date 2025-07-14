package org.javadominicano.websocket;

import org.javadominicano.dto.EstadoEstacionDTO;
import org.javadominicano.dto.ResumenEstadoEstacionesDTO;
import org.javadominicano.servicios.EstadoEstacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EstadoEstacionesBroadcastService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private EstadoEstacionesService estadoService;

    @Scheduled(fixedRate = 5000)
    public void broadcastEstado() {
        List<EstadoEstacionDTO> estados = estadoService.obtenerEstados();
        long total = estados.size();
        long activas = estados.stream().filter(EstadoEstacionDTO::isActiva).count();
        long inactivas = total - activas;
        Optional<String> primeraInactiva = estados.stream()
                .filter(e -> !e.isActiva())
                .map(EstadoEstacionDTO::getId)
                .findFirst();

        ResumenEstadoEstacionesDTO dto = new ResumenEstadoEstacionesDTO();
        dto.setTotal(total);
        dto.setActivas(activas);
        dto.setInactivas(inactivas);
        dto.setPrimeraInactivaId(primeraInactiva.orElse(null));

        messagingTemplate.convertAndSend("/topic/estadoEstaciones", dto);
    }
}