package org.javadominicano.controladores;

import org.javadominicano.entidades.Notificacion;
import org.javadominicano.repositorios.RepositorioNotificacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificacionesController {

    @Autowired
    private RepositorioNotificacion repo;

    @PostMapping("/api/notificaciones")
    public void guardar(@RequestBody Notificacion notificacion) {
        repo.save(notificacion);
    }
}
