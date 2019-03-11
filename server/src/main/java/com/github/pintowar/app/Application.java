package com.github.pintowar.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
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
