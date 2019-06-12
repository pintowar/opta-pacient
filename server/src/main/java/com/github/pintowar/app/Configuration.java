package com.github.pintowar.app;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import io.micronaut.jackson.ObjectMapperFactory;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.optaplanner.persistence.jackson.api.OptaPlannerJacksonModule;

@Slf4j
@Factory
@Replaces(ObjectMapperFactory.class)
public class Configuration implements BeanCreatedEventListener<ObjectMapper> {

    @Bean
    @Context
    public KieContainer kieContainer() {
        KieServices kieServices = KieServices.get();

        val kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory
                .newClassPathResource("com/github/pintowar/app/solver/patientAdmissionScheduleScoreRules.drl"));
        val kb = kieServices.newKieBuilder(kieFileSystem);
        kb.buildAll();

        return kieServices.newKieContainer(kb.getKieModule().getReleaseId());
    }

    @Override
    public ObjectMapper onCreated(BeanCreatedEvent<ObjectMapper> event) {
        // look at: https://stackoverflow.com/questions/53195071/how-to-configure-jackson-to-use-snake-case-in-micronaut
        final ObjectMapper mapper = new ObjectMapper();//event.getBean();
        return mapper.registerModule(OptaPlannerJacksonModule.createModule());
    }
}
