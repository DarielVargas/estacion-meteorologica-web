package org.javadominicano.websocket;

import org.javadominicano.dto.EstadoEstacionDTO;
import org.javadominicano.dto.ResumenEstacionesDTO;
import org.javadominicano.servicios.EstadoEstacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EstadoEstacionesBroadcastService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private EstadoEstacionesService estadoService;

    @Scheduled(fixedRate = 5000)
    public void sendResumen() {
        List<EstadoEstacionDTO> estados = estadoService.obtenerEstados();
        int total = estados.size();
        int activas = 0;
        String primeraInactiva = null;
        for (EstadoEstacionDTO e : estados) {
            if (e.isActiva()) {
                activas++;
            } else if (primeraInactiva == null) {
                primeraInactiva = e.getId();
            }
        }
        ResumenEstacionesDTO dto = new ResumenEstacionesDTO();
        dto.setTotal(total);
        dto.setActivas(activas);
        dto.setInactivas(total - activas);
        dto.setPrimeraInactiva(primeraInactiva);
        messagingTemplate.convertAndSend("/topic/estado-estaciones", dto);
    }
}
