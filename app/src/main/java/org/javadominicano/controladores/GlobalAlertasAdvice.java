package org.javadominicano.controladores;

import org.javadominicano.servicios.AlertasService;
import org.javadominicano.dto.AlertaActivaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.List;

@ControllerAdvice
public class GlobalAlertasAdvice {

    @Autowired
    private AlertasService alertasService;

    @ModelAttribute("alertasActivas")
    public List<AlertaActivaDTO> alertasActivas() {
        return alertasService.obtenerAlertasActivas();
    }
}
