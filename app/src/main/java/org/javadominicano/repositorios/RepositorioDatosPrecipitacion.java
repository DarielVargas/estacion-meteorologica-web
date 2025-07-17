package org.javadominicano.repositorios;

import org.javadominicano.entidades.DatosPrecipitacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.sql.Timestamp;

public interface RepositorioDatosPrecipitacion extends JpaRepository<DatosPrecipitacion, Long> {

    Page<DatosPrecipitacion> findAllByOrderByFechaDesc(Pageable pageable);

    // Datos por rango de fechas
    @Query("SELECT d FROM DatosPrecipitacion d WHERE d.fecha BETWEEN :inicio AND :fin ORDER BY d.fecha DESC")
    List<DatosPrecipitacion> findByFechaBetweenOrderByFechaDesc(@Param("inicio") Timestamp inicio,
                                                                @Param("fin") Timestamp fin);

    // Datos por rango de fechas con paginación
    Page<DatosPrecipitacion> findByFechaBetween(Timestamp inicio, Timestamp fin, Pageable pageable);

    @Query("SELECT d FROM DatosPrecipitacion d ORDER BY d.fecha DESC")
    List<DatosPrecipitacion> findTopByOrderByFechaDesc(Pageable pageable);

    @Query("SELECT d FROM DatosPrecipitacion d WHERE d.estacionId = :estacionId ORDER BY d.fecha DESC")
    List<DatosPrecipitacion> findTopByEstacionIdOrderByFechaDesc(@Param("estacionId") String estacionId, Pageable pageable);

    @Query("SELECT MAX(d.fecha) FROM DatosPrecipitacion d WHERE d.estacionId = :estacionId")
    Timestamp findUltimaFechaByEstacion(@Param("estacionId") String estacionId);
}