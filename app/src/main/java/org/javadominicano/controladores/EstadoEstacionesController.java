package org.javadominicano.controladores;

import org.javadominicano.dto.EstadoEstacionDTO;
import org.javadominicano.servicios.EstadoEstacionesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EstadoEstacionesController {

    @Autowired
    private EstadoEstacionesService estadoService;

    @GetMapping("/api/estado-estaciones")
    public List<EstadoEstacionDTO> obtenerEstados() {
        return estadoService.obtenerEstados();
    }
}