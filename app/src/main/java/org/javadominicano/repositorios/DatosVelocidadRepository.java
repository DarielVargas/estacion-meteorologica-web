package org.javadominicano.repositorios;

import org.javadominicano.entidades.DatosVelocidad;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DatosVelocidadRepository extends JpaRepository<DatosVelocidad, Long> {
    List<DatosVelocidad> findTop30ByOrderByFechaDesc();
}
