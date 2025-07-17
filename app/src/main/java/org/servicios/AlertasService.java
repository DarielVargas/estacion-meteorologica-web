package org.javadominicano.servicios;

import org.javadominicano.entidades.Alerta;
import org.javadominicano.entidades.DatosPrecipitacion;
import org.javadominicano.entidades.DatosVelocidad;
import org.javadominicano.visualizadorweb.entidades.DatosHumedad;
import org.javadominicano.visualizadorweb.entidades.DatosTemperatura;
import org.javadominicano.visualizadorweb.entidades.DatosPresion;
import org.javadominicano.visualizadorweb.entidades.DatosHumedadSuelo;
import org.javadominicano.dto.AlertaActivaDTO;
import org.javadominicano.repositorios.RepositorioAlerta;
import org.javadominicano.repositorios.RepositorioDatosPrecipitacion;
import org.javadominicano.repositorios.RepositorioDatosVelocidad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedad;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosTemperatura;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosPresion;
import org.javadominicano.visualizadorweb.repositorios.RepositorioDatosHumedadSuelo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AlertasService {

    @Autowired
    private RepositorioAlerta repoAlerta;
    @Autowired
    private RepositorioDatosVelocidad repoVelocidad;
    @Autowired
    private RepositorioDatosHumedad repoHumedad;
    @Autowired
    private RepositorioDatosTemperatura repoTemperatura;
    @Autowired
    private RepositorioDatosPrecipitacion repoPrecipitacion;
    @Autowired
    private RepositorioDatosPresion repoPresion;
    @Autowired
    private RepositorioDatosHumedadSuelo repoHumedadSuelo;

    private final Map<Long, Double> ultimoUmbral = new ConcurrentHashMap<>();
    private final Set<Long> alertasDisparadas = ConcurrentHashMap.newKeySet();

    public List<AlertaActivaDTO> obtenerAlertasActivas() {
        List<AlertaActivaDTO> lista = new ArrayList<>();

        List<Alerta> alertas = repoAlerta.findAll();
        PageRequest pr = PageRequest.of(0, 1);

        for (Alerta alerta : alertas) {
            Timestamp fecha = null;
            double valor = 0.0;

            switch (alerta.getNombre()) {
                case "Temperatura":
                    List<DatosTemperatura> temps = repoTemperatura.findTopByEstacionIdOrderByFechaDesc(alerta.getEstacionId(), pr);
                    if (!temps.isEmpty()) {
                        DatosTemperatura t = temps.get(0);
                        valor = t.getTemperatura();
                        fecha = t.getFecha();
                    }
                    break;
                case "Humedad":
                    List<DatosHumedad> hums = repoHumedad.findTopByEstacionIdOrderByFechaDesc(alerta.getEstacionId(), pr);
                    if (!hums.isEmpty()) {
                        DatosHumedad h = hums.get(0);
                        valor = h.getHumedad();
                        fecha = h.getFecha();
                    }
                    break;
                case "VelocidadViento":
                    List<DatosVelocidad> vels = repoVelocidad.findTopByEstacionIdOrderByFechaDesc(alerta.getEstacionId(), pr);
                    if (!vels.isEmpty()) {
                        DatosVelocidad v = vels.get(0);
                        valor = v.getVelocidad();
                        fecha = v.getFecha();
                    }
                    break;
                case "Precipitacion":
                    List<DatosPrecipitacion> pres = repoPrecipitacion.findTopByEstacionIdOrderByFechaDesc(alerta.getEstacionId(), pr);
                    if (!pres.isEmpty()) {
                        DatosPrecipitacion p = pres.get(0);
                        valor = p.getProbabilidad();
                        fecha = p.getFecha();
                    }
                    break;
                case "Presion":
                    List<DatosPresion> press = repoPresion.findTopByEstacionIdOrderByFechaDesc(alerta.getEstacionId(), pr);
                    if (!press.isEmpty()) {
                        DatosPresion p = press.get(0);
                        valor = p.getPresion();
                        fecha = p.getFecha();
                    }
                    break;
                case "HumedadSuelo":
                    List<DatosHumedadSuelo> hs = repoHumedadSuelo.findTopByEstacionIdOrderByFechaDesc(alerta.getEstacionId(), pr);
                    if (!hs.isEmpty()) {
                        DatosHumedadSuelo s = hs.get(0);
                        valor = s.getHumedad();
                        fecha = s.getFecha();
                    }
                    break;
            }

            if (fecha != null) {
                agregarAlertaActiva(lista, alerta, valor, fecha);
            }
        }

        return lista;
    }

    public List<String> obtenerAlertasActivasTexto() {
        List<String> textos = new ArrayList<>();
        for (AlertaActivaDTO dto : obtenerAlertasActivas()) {
            textos.add(formatoAlerta(dto.getAlerta()));
        }
        return textos;
    }

    private void agregarAlertaActiva(List<AlertaActivaDTO> lista, Alerta alerta, double valor, Timestamp fecha) {
        if (alerta == null) {
            return;
        }

        Double prev = ultimoUmbral.get(alerta.getId());
        if (prev == null || Double.compare(prev, alerta.getUmbral()) != 0) {
            ultimoUmbral.put(alerta.getId(), alerta.getUmbral());
            alertasDisparadas.remove(alerta.getId());
        }

        if (chequearAlerta(alerta, valor) && !alertasDisparadas.contains(alerta.getId())) {
            AlertaActivaDTO dto = new AlertaActivaDTO();
            dto.setAlerta(alerta);
            dto.setValorActual(valor);
            dto.setFecha(fecha);
            lista.add(dto);
            alertasDisparadas.add(alerta.getId());
        }
    }

    private boolean chequearAlerta(Alerta alerta, Double valor) {
        if (alerta == null || valor == null || !alerta.isActiva()) {
            return false;
        }
        if (">".equals(alerta.getOperador())) {
            return valor > alerta.getUmbral();
        } else {
            return valor < alerta.getUmbral();
        }
    }

    private String formatoAlerta(Alerta a) {
        if (a == null) {
            return "Umbral superado";
        }
        switch (a.getNombre()) {
            case "Temperatura":
                return "Umbral de temperatura superado";
            case "Humedad":
                return "Umbral de humedad superado";
            case "VelocidadViento":
                return "Umbral de velocidad de viento superado";
            case "Precipitacion":
                return "Umbral de precipitación superado";
            case "Presion":
                return "Umbral de presión superado";
            case "HumedadSuelo":
                return "Umbral de humedad del suelo superado";
            default:
                return "Umbral superado";
        }
    }}