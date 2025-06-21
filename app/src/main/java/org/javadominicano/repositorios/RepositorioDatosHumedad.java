package org.javadominicano.visualizadorweb.repositorios;

import org.javadominicano.visualizadorweb.entidades.DatosHumedad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RepositorioDatosHumedad extends JpaRepository<DatosHumedad, Integer> {
    List<DatosHumedad> findAllByOrderByFechaDesc();
}
