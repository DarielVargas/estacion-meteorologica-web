package org.javadominicano.repositorios;

import org.javadominicano.entidades.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioNotificacion extends JpaRepository<Notificacion, Long> {
}