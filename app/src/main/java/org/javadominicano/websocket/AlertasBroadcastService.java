package org.javadominicano.websocket;

import org.javadominicano.dto.AlertaActivaDTO;
import org.javadominicano.servicios.AlertasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlertasBroadcastService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private AlertasService alertasService;

    @Scheduled(fixedRate = 5000)
    public void broadcastAlertas() {
        List<AlertaActivaDTO> alertas = alertasService.obtenerAlertasActivas();
        messagingTemplate.convertAndSend("/topic/alertas", alertas);
    }
}