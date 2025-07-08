package org.javadominicano.visualizadorweb.repositorios;

import org.javadominicano.visualizadorweb.entidades.DatosHumedadSuelo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface RepositorioDatosHumedadSuelo extends JpaRepository<DatosHumedadSuelo, Integer> {
    List<DatosHumedadSuelo> findAllByOrderByFechaDesc();

    List<DatosHumedadSuelo> findByFechaBetweenOrderByFechaDesc(Timestamp inicio, Timestamp fin);

    Page<DatosHumedadSuelo> findByFechaBetween(Timestamp inicio, Timestamp fin, Pageable pageable);

    @Query("SELECT d FROM DatosHumedadSuelo d ORDER BY d.fecha DESC")
    List<DatosHumedadSuelo> findTopByOrderByFechaDesc(Pageable pageable);

    @Query("SELECT MAX(d.fecha) FROM DatosHumedadSuelo d WHERE d.estacionId = :estacionId")
    Timestamp findUltimaFechaByEstacion(@Param("estacionId") String estacionId);
}