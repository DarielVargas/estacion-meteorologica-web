package org.javadominicano.repositorios;

import org.javadominicano.entidades.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioAlerta extends JpaRepository<Alerta, Long> {
    Alerta findByNombre(String nombre);
    Alerta findByNombreAndEstacionId(String nombre, String estacionId);
    long countByActiva(boolean activa);
}