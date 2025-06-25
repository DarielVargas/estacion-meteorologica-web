package org.javadominicano.repositorios;

import org.javadominicano.entidades.DatosDireccion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.sql.Timestamp;

public interface RepositorioDatosDireccion extends JpaRepository<DatosDireccion, Long> {

    Page<DatosDireccion> findAllByOrderByFechaDesc(Pageable pageable);

    @Query("SELECT d FROM DatosDireccion d ORDER BY d.fecha DESC")
    List<DatosDireccion> findTopByOrderByFechaDesc(Pageable pageable);

    List<DatosDireccion> findByFechaBetweenOrderByFechaAsc(Timestamp inicio, Timestamp fin);
}
