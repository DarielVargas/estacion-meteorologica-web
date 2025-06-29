package org.javadominicano.repositorios;

import org.javadominicano.entidades.EstacionMeteorologica;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioEstacionMeteorologica extends JpaRepository<EstacionMeteorologica, String> {
}