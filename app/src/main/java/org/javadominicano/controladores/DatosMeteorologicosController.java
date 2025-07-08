package org.javadominicano.controladores;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import org.javadominicano.dto.DatosMeteorologicosDTO;
import org.javadominicano.servicios.DashboardDataService;

@RestController
public class DatosMeteorologicosController {

    @Autowired
    private DashboardDataService dashboardDataService;

    @GetMapping("/api/datos-meteorologicos")
    public DatosMeteorologicosDTO obtenerDatos() {
        return dashboardDataService.obtenerSeries();
    }
}
