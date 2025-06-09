package org.javadominicano.repositorios;

import org.javadominicano.entidades.DatosVelocidad;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioDatosVelocidad extends JpaRepository<DatosVelocidad, Long> {
    Page<DatosVelocidad> findAllByOrderByFechaDesc(Pageable pageable);
}
