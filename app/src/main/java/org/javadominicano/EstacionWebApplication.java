package org.javadominicano;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {
    "org.javadominicano",              // Controladores, entidades, repositorios
    "com.javadominicano.configuracion" // SeguridadConfig
})
@EnableScheduling
public class EstacionWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(EstacionWebApplication.class, args);
    }
}
