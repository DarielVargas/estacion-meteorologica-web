package org.javadominicano.websocket;

import org.javadominicano.dto.AlertaActivaDTO;
import org.javadominicano.servicios.AlertasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AlertasWsController {
    @Autowired
    private AlertasService alertasService;

    @MessageMapping("/alertas/solicitar")
    @SendTo("/topic/alertas")
    public List<AlertaActivaDTO> solicitar() {
        return alertasService.obtenerAlertasActivas();
    }
}
