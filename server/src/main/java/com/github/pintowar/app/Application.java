package com.github.pintowar.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.optaplanner.persistence.jackson.api.OptaPlannerJacksonModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        val objectMapper = new ObjectMapper();
        objectMapper.registerModule(OptaPlannerJacksonModule.createModule());
        return objectMapper;
    }

}
