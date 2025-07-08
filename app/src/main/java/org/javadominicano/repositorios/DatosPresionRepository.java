package org.javadominicano.visualizadorweb.repositorios;

import org.javadominicano.visualizadorweb.entidades.DatosPresion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DatosPresionRepository extends JpaRepository<DatosPresion, Integer> {
    List<DatosPresion> findTop30ByOrderByFechaDesc();
}
