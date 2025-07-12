package org.javadominicano.repositorios;

import org.javadominicano.entidades.AlertaMedicion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioAlertaMedicion extends JpaRepository<AlertaMedicion, Long> {
}
