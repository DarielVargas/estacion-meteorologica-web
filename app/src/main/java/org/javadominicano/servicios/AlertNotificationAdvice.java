package org.javadominicano.servicios;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class AlertNotificationAdvice {
    @Autowired
    private AlertaNotificacionService servicio;

    @ModelAttribute("alertasActivas")
    public List<String> agregarAlertas() {
        return servicio.obtenerAlertasActivas();
    }
}
