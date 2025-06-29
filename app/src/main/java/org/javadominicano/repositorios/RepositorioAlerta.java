package org.javadominicano.repositorios;

import org.javadominicano.entidades.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioAlerta extends JpaRepository<Alerta, Long> {
    Alerta findByNombre(String nombre);
    long countByActiva(boolean activa);
}