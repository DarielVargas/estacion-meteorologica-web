package org.javadominicano.repositorios;

import org.javadominicano.entidades.DatosPrecipitacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorioDatosPrecipitacion extends JpaRepository<DatosPrecipitacion, Long> {
    List<DatosPrecipitacion> findAllByOrderByFechaDesc();
}
