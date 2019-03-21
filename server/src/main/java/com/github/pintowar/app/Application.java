package com.github.pintowar.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.val;
import org.kie.api.KieServices;
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

    @Bean
    public KieContainer kieContainer() {
        val kieServices = KieServices.Factory.get();

        val kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory
                .newClassPathResource("com/github/pintowar/app/solver/patientAdmissionScheduleScoreRules.drl"));
        val kb = kieServices.newKieBuilder(kieFileSystem);
        kb.buildAll();

        return kieServices.newKieContainer(kb.getKieModule().getReleaseId());
    }

}
