package org.javadominicano.visualizadorweb.repositorios;

import org.javadominicano.visualizadorweb.entidades.DatosTemperatura;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface RepositorioDatosTemperatura extends JpaRepository<DatosTemperatura, Integer> {
    List<DatosTemperatura> findAllByOrderByFechaDesc();
}
