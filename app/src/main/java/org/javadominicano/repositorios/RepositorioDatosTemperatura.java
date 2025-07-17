package org.javadominicano.visualizadorweb.repositorios;

import org.javadominicano.visualizadorweb.entidades.DatosTemperatura;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.sql.Timestamp;

public interface RepositorioDatosTemperatura extends JpaRepository<DatosTemperatura, Integer> {

    List<DatosTemperatura> findAllByOrderByFechaDesc();

    // Datos por rango de fechas
    List<DatosTemperatura> findByFechaBetweenOrderByFechaDesc(Timestamp inicio, Timestamp fin);

    // Datos por rango de fechas con paginación
    Page<DatosTemperatura> findByFechaBetween(Timestamp inicio, Timestamp fin, Pageable pageable);

    @Query("SELECT d FROM DatosTemperatura d ORDER BY d.fecha DESC")
    List<DatosTemperatura> findTopByOrderByFechaDesc(Pageable pageable);

    @Query("SELECT d FROM DatosTemperatura d WHERE d.estacionId = :estacionId ORDER BY d.fecha DESC")
    List<DatosTemperatura> findTopByEstacionIdOrderByFechaDesc(@Param("estacionId") String estacionId, Pageable pageable);

    @Query("SELECT MAX(d.fecha) FROM DatosTemperatura d WHERE d.estacionId = :estacionId")
    Timestamp findUltimaFechaByEstacion(@Param("estacionId") String estacionId);
}