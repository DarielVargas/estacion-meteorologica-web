package org.javadominicano.websocket;

import org.javadominicano.dto.AlarmaActivaDTO;
import org.javadominicano.servicios.AlarmasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlarmasBroadcastService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private AlarmasService alarmasService;

    @Scheduled(fixedRate = 5000)
    public void broadcastAlarmas() {
        List<AlarmaActivaDTO> alarmas = alarmasService.obtenerAlarmasActivas();
        messagingTemplate.convertAndSend("/topic/alarmas", alarmas);
    }
}
