package org.javadominicano.repositorios;

import org.javadominicano.entidades.Alarma;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorioAlarma extends JpaRepository<Alarma, Long> {
    List<Alarma> findByActivaTrue();
}
