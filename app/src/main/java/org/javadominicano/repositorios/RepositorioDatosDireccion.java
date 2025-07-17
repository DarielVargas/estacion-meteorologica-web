package org.javadominicano.repositorios;

import org.javadominicano.entidades.DatosDireccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.sql.Timestamp;

public interface RepositorioDatosDireccion extends JpaRepository<DatosDireccion, Long> {

    Page<DatosDireccion> findAllByOrderByFechaDesc(Pageable pageable);

    // Datos por rango de fechas
    List<DatosDireccion> findByFechaBetweenOrderByFechaDesc(Timestamp inicio, Timestamp fin);

    // Datos por rango de fechas con paginación
    Page<DatosDireccion> findByFechaBetween(Timestamp inicio, Timestamp fin, Pageable pageable);

    @Query("SELECT d FROM DatosDireccion d ORDER BY d.fecha DESC")
    List<DatosDireccion> findTopByOrderByFechaDesc(Pageable pageable);

    @Query("SELECT d FROM DatosDireccion d WHERE d.estacionId = :estacionId ORDER BY d.fecha DESC")
    List<DatosDireccion> findTopByEstacionIdOrderByFechaDesc(@Param("estacionId") String estacionId, Pageable pageable);

    @Query("SELECT MAX(d.fecha) FROM DatosDireccion d WHERE d.estacionId = :estacionId")
    Timestamp findUltimaFechaByEstacion(@Param("estacionId") String estacionId);
}