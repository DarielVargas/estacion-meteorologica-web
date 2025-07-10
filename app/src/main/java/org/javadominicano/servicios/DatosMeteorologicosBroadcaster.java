package org.javadominicano.servicios;

import org.javadominicano.dto.DatosMeteorologicosDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DatosMeteorologicosBroadcaster {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private DatosMeteorologicosService service;

    @Scheduled(fixedRate = 10000)
    public void enviarActualizacion() {
        DatosMeteorologicosDTO datos = service.obtenerDatos();
        messagingTemplate.convertAndSend("/topic/datos-meteorologicos", datos);
    }
}
