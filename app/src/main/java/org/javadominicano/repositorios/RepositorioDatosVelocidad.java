package org.javadominicano.repositorios;

import org.javadominicano.entidades.DatosVelocidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositorioDatosVelocidad extends JpaRepository<DatosVelocidad, Long> {

    Page<DatosVelocidad> findAllByOrderByFechaDesc(Pageable pageable);

    @Query("SELECT d FROM DatosVelocidad d ORDER BY d.fecha DESC")
    List<DatosVelocidad> findTopByOrderByFechaDesc(Pageable pageable);

    // NUEVO MÉTODO: obtener la última fecha por cada sensor
    @Query("SELECT d.sensorId, MAX(d.fecha) FROM DatosVelocidad d GROUP BY d.sensorId")
    List<Object[]> findLastFechaBySensor();
}
