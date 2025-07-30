package org.javadominicano;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import jakarta.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication
@EnableScheduling
@ComponentScan(basePackages = {
    "org.javadominicano",              // Controladores, entidades, repositorios
    "com.javadominicano.configuracion" // SeguridadConfig
})
public class EstacionWebApplication {
    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("America/Santo_Domingo"));
    }
    public static void main(String[] args) {
        SpringApplication.run(EstacionWebApplication.class, args);
    }
}