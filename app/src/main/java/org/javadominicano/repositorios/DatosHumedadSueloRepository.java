package org.javadominicano.visualizadorweb.repositorios;

import org.javadominicano.visualizadorweb.entidades.DatosHumedadSuelo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DatosHumedadSueloRepository extends JpaRepository<DatosHumedadSuelo, Integer> {
    List<DatosHumedadSuelo> findTop30ByOrderByFechaDesc();
}
