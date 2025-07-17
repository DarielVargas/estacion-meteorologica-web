package org.javadominicano.visualizadorweb.repositorios;

import org.javadominicano.visualizadorweb.entidades.DatosPresion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface RepositorioDatosPresion extends JpaRepository<DatosPresion, Integer> {
    List<DatosPresion> findAllByOrderByFechaDesc();

    List<DatosPresion> findByFechaBetweenOrderByFechaDesc(Timestamp inicio, Timestamp fin);

    Page<DatosPresion> findByFechaBetween(Timestamp inicio, Timestamp fin, Pageable pageable);

    @Query("SELECT d FROM DatosPresion d ORDER BY d.fecha DESC")
    List<DatosPresion> findTopByOrderByFechaDesc(Pageable pageable);

    @Query("SELECT d FROM DatosPresion d WHERE d.estacionId = :estacionId ORDER BY d.fecha DESC")
    List<DatosPresion> findTopByEstacionIdOrderByFechaDesc(@Param("estacionId") String estacionId, Pageable pageable);

    @Query("SELECT MAX(d.fecha) FROM DatosPresion d WHERE d.estacionId = :estacionId")
    Timestamp findUltimaFechaByEstacion(@Param("estacionId") String estacionId);
}