package org.javadominicano.repositorios;

import org.javadominicano.entidades.DatosVelocidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.sql.Timestamp;

public interface RepositorioDatosVelocidad extends JpaRepository<DatosVelocidad, Long> {

    Page<DatosVelocidad> findAllByOrderByFechaDesc(Pageable pageable);

    // Datos por rango de fechas
    List<DatosVelocidad> findByFechaBetweenOrderByFechaDesc(Timestamp inicio, Timestamp fin);

    // Datos por rango de fechas con paginación
    Page<DatosVelocidad> findByFechaBetween(Timestamp inicio, Timestamp fin, Pageable pageable);

    @Query("SELECT d FROM DatosVelocidad d ORDER BY d.fecha DESC")
    List<DatosVelocidad> findTopByOrderByFechaDesc(Pageable pageable);

    // NUEVO MÉTODO: obtener la última fecha por cada sensor
    @Query("SELECT d.sensorId, MAX(d.fecha) FROM DatosVelocidad d GROUP BY d.sensorId")
    List<Object[]> findLastFechaBySensor();

    @Query("SELECT MAX(d.fecha) FROM DatosVelocidad d WHERE d.estacionId = :estacionId")
    Timestamp findUltimaFechaByEstacion(@Param("estacionId") String estacionId);

    DatosVelocidad findFirstByEstacionIdOrderByFechaDesc(String estacionId);
}