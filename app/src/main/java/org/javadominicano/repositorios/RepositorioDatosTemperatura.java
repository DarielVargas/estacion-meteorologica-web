package org.javadominicano.visualizadorweb.repositorios;

import org.javadominicano.visualizadorweb.entidades.DatosTemperatura;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.sql.Timestamp;

public interface RepositorioDatosTemperatura extends JpaRepository<DatosTemperatura, Integer> {

    List<DatosTemperatura> findAllByOrderByFechaDesc();

    @Query("SELECT d FROM DatosTemperatura d ORDER BY d.fecha DESC")
    List<DatosTemperatura> findTopByOrderByFechaDesc(Pageable pageable);

    List<DatosTemperatura> findByFechaBetweenOrderByFechaAsc(Timestamp inicio, Timestamp fin);
}
