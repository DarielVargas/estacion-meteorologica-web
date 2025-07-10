package org.javadominicano.websocket;

import org.javadominicano.visualizadorweb.dto.MedicionesRecientesDTO;

// Repositorios del paquete visualizadorweb
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosTemperatura;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosPresion;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedadSuelo;

// Repositorios del paquete repositorios
import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.repositorios.RepositorioDatosDireccion;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class MedicionesBroadcastService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private RepositorioDatosTemperatura repoTemperatura;

    @Autowired
    private RepositorioDatosHumedad repoHumedad;

    @Autowired
    private RepositorioDatosVelocidad repoVelocidad;

    @Autowired
    private RepositorioDatosDireccion repoDireccion;

    @Autowired
    private RepositorioDatosPrecipitacion repoPrecipitacion;

    @Autowired
    private RepositorioDatosPresion repoPresion;

    @Autowired
    private RepositorioDatosHumedadSuelo repoHumedadSuelo;

    @Scheduled(fixedRate = 5000)
    public void sendLatest() {
        PageRequest pr = PageRequest.of(0, 1);
        MedicionesRecientesDTO m = new MedicionesRecientesDTO();

        m.setTemperatura(
            repoTemperatura.findTopByOrderByFechaDesc(pr).get(0).getTemperatura()
        );

        m.setHumedad(
            repoHumedad.findTopByOrderByFechaDesc(pr).get(0).getHumedad()
        );

        m.setVelocidadViento(
            repoVelocidad.findTopByOrderByFechaDesc(pr).get(0).getVelocidad()
        );

        m.setDireccionViento(
            repoDireccion.findTopByOrderByFechaDesc(pr).get(0).getDireccion()
        );

        m.setPrecipitacion(
            repoPrecipitacion.findTopByOrderByFechaDesc(pr).get(0).getProbabilidad()
        );

        m.setPresion(
            repoPresion.findTopByOrderByFechaDesc(pr).get(0).getPresion()
        );

        m.setHumedadSuelo(
            repoHumedadSuelo.findTopByOrderByFechaDesc(pr).get(0).getHumedad()
        );

        messagingTemplate.convertAndSend("/topic/mediciones", m);
    }
}
