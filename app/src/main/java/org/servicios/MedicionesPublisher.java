package org.javadominicano.servicios;

import org.javadominicano.visualizadorweb.dto.MedicionesRecientesDTO;
import org.javadominicano.visualizadorweb.entidades.DatosHumedad;
import org.javadominicano.visualizadorweb.entidades.DatosTemperatura;
import org.javadominicano.entidades.DatosVelocidad;
import org.javadominicano.entidades.DatosDireccion;
import org.javadominicano.entidades.DatosPrecipitacion;
import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.repositorios.RepositorioDatosDireccion;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosTemperatura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class MedicionesPublisher {

    @Autowired private SimpMessagingTemplate messagingTemplate;
    @Autowired private RepositorioDatosVelocidad repoVelocidad;
    @Autowired private RepositorioDatosDireccion repoDireccion;
    @Autowired private RepositorioDatosPrecipitacion repoPrecipitacion;
    @Autowired private RepositorioDatosHumedad repoHumedad;
    @Autowired private RepositorioDatosTemperatura repoTemperatura;

    private Date ultimaEmision = new Date(0);

    @Scheduled(fixedDelay = 5000)
    public void verificarNuevasMediciones() {
        Timestamp fVel = repoVelocidad.findTopByOrderByFechaDesc(PageRequest.of(0,1)).stream().findFirst().map(DatosVelocidad::getFecha).orElse(null);
        Timestamp fDir = repoDireccion.findTopByOrderByFechaDesc(PageRequest.of(0,1)).stream().findFirst().map(DatosDireccion::getFecha).orElse(null);
        Timestamp fPre = repoPrecipitacion.findTopByOrderByFechaDesc(PageRequest.of(0,1)).stream().findFirst().map(DatosPrecipitacion::getFecha).orElse(null);
        Timestamp fHum = repoHumedad.findTopByOrderByFechaDesc(PageRequest.of(0,1)).stream().findFirst().map(DatosHumedad::getFecha).orElse(null);
        Timestamp fTemp= repoTemperatura.findTopByOrderByFechaDesc(PageRequest.of(0,1)).stream().findFirst().map(DatosTemperatura::getFecha).orElse(null);

        Timestamp masReciente = fVel;
        if (fDir != null && (masReciente == null || fDir.after(masReciente))) masReciente = fDir;
        if (fPre != null && (masReciente == null || fPre.after(masReciente))) masReciente = fPre;
        if (fHum != null && (masReciente == null || fHum.after(masReciente))) masReciente = fHum;
        if (fTemp != null && (masReciente == null || fTemp.after(masReciente))) masReciente = fTemp;

        if (masReciente != null && masReciente.after(ultimaEmision)) {
            ultimaEmision = masReciente;
            MedicionesRecientesDTO dto = new MedicionesRecientesDTO();
            dto.setTemperatura(repoTemperatura.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getTemperatura());
            dto.setHumedad(repoHumedad.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getHumedad());
            dto.setVelocidadViento(repoVelocidad.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getVelocidad());
            dto.setDireccionViento(repoDireccion.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getDireccion());
            dto.setPrecipitacion(repoPrecipitacion.findTopByOrderByFechaDesc(PageRequest.of(0,1)).get(0).getProbabilidad());
            messagingTemplate.convertAndSend("/topic/mediciones", dto);
        }
    }
}
