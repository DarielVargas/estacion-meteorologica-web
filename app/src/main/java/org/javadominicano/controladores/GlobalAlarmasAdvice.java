package org.javadominicano.controladores;

import org.javadominicano.dto.AlarmaActivaDTO;
import org.javadominicano.servicios.AlarmasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalAlarmasAdvice {
    @Autowired
    private AlarmasService alarmasService;

    @ModelAttribute("alarmasActivas")
    public List<AlarmaActivaDTO> alarmasActivas() {
        return alarmasService.obtenerAlarmasActivas();
    }
}
