package org.javadominicano.visualizadorweb.repositorios;

import org.javadominicano.visualizadorweb.entidades.DatosHumedad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.sql.Timestamp;

public interface RepositorioDatosHumedad extends JpaRepository<DatosHumedad, Integer> {

    List<DatosHumedad> findAllByOrderByFechaDesc();

    // Datos por rango de fechas
    List<DatosHumedad> findByFechaBetweenOrderByFechaDesc(Timestamp inicio, Timestamp fin);

    // Datos por rango de fechas con paginación
    Page<DatosHumedad> findByFechaBetween(Timestamp inicio, Timestamp fin, Pageable pageable);

    @Query("SELECT d FROM DatosHumedad d ORDER BY d.fecha DESC")
    List<DatosHumedad> findTopByOrderByFechaDesc(Pageable pageable);
}
