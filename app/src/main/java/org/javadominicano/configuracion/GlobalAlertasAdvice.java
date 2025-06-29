package org.javadominicano.configuracion;

import org.javadominicano.servicio.ServicioAlertas;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@ControllerAdvice
public class GlobalAlertasAdvice {

    @Autowired
    private ServicioAlertas servicioAlertas;

    @ModelAttribute("alertasActivas")
    public List<String> alertasActivas() {
        return servicioAlertas.obtenerAlertasActivasDescripcion();
    }
}
