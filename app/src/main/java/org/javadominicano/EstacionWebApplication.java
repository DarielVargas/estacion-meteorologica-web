package org.javadominicano;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
    "org.javadominicano",              // Controladores, entidades, repositorios
    "com.javadominicano.configuracion" // SeguridadConfig
})
public class EstacionWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(EstacionWebApplication.class, args);
    }
}
