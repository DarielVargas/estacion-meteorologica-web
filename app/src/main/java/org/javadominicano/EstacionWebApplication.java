package org.javadominicano;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
    "org.javadominicano.controladores",
    "org.javadominicano.entidades"
})
@EnableJpaRepositories(basePackages = "org.javadominicano.repositorios")
public class EstacionWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(EstacionWebApplication.class, args);
    }
}

