package org.javadominicano.repositorios;

import org.javadominicano.entidades.DatosPrecipitacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioDatosPrecipitacion extends JpaRepository<DatosPrecipitacion, Long> {
    Page<DatosPrecipitacion> findAllByOrderByFechaDesc(Pageable pageable);
}

