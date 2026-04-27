package com.semy.proje;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackages = {"com.semy"})
@EnableJpaRepositories(basePackages = "com.semy.repository")
@SpringBootApplication
@ComponentScan(basePackages = {"com.semy"})

// Ana uygulama buradan başlatılıyor
public class ProjeApplication {
    public static ConfigurableApplicationContext startSpringApplication() {
        return SpringApplication.run(ProjeApplication.class);
    }
}
