package org.javadominicano.repositorios;

import org.javadominicano.entidades.DatosDireccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioDatosDireccion extends JpaRepository<DatosDireccion, Long> {
    Page<DatosDireccion> findAllByOrderByFechaDesc(Pageable pageable);
}

