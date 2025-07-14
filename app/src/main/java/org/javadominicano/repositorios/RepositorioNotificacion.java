package org.javadominicano.repositorios;

import org.javadominicano.entidades.Notificacion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface RepositorioNotificacion extends JpaRepository<Notificacion, Long> {

    Page<Notificacion> findByFechaBetween(Timestamp inicio, Timestamp fin, Pageable pageable);

    Page<Notificacion> findByEstacionAndFechaBetween(String estacion, Timestamp inicio, Timestamp fin, Pageable pageable);

    List<Notificacion> findByFechaBetweenOrderByFechaDesc(Timestamp inicio, Timestamp fin);

    List<Notificacion> findByEstacionAndFechaBetweenOrderByFechaDesc(String estacion, Timestamp inicio, Timestamp fin);
}