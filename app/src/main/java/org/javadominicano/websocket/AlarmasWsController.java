package org.javadominicano.websocket;

import org.javadominicano.dto.AlarmaActivaDTO;
import org.javadominicano.servicios.AlarmasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AlarmasWsController {
    @Autowired
    private AlarmasService alarmasService;

    @MessageMapping("/alarmas/solicitar")
    @SendTo("/topic/alarmas")
    public List<AlarmaActivaDTO> solicitar() {
        return alarmasService.obtenerAlarmasActivas();
    }
}
