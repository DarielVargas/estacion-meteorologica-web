package org.javadominicano.repositorios;

import org.javadominicano.entidades.DatosPrecipitacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RepositorioDatosPrecipitacion extends JpaRepository<DatosPrecipitacion, Long> {

    Page<DatosPrecipitacion> findAllByOrderByFechaDesc(Pageable pageable);

    @Query("SELECT d FROM DatosPrecipitacion d ORDER BY d.fecha DESC")
    List<DatosPrecipitacion> findTopByOrderByFechaDesc(Pageable pageable);
}
