package org.javadominicano.repositorios;

import org.javadominicano.entidades.DatosDireccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorioDatosDireccion extends JpaRepository<DatosDireccion, Long> {
    List<DatosDireccion> findAllByOrderByFechaDesc();
}
