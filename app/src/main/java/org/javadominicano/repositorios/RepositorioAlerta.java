package org.javadominicano.repositorios;

import org.javadominicano.entidades.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioAlerta extends JpaRepository<Alerta, Long> {
    Alerta findByNombreAndEstacionId(String nombre, String estacionId);
    Alerta findByNombre(String nombre); // compatible con código existente
    long countByActiva(boolean activa);
}