package server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.event.BeanCreatedEvent;
import io.micronaut.context.event.BeanCreatedEventListener;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.optaplanner.persistence.jackson.api.OptaPlannerJacksonModule;

@Slf4j
@Factory
public class Configuration {

    @Bean
    @Context
    public KieContainer kieContainer() {
        val kieServices = KieServices.Factory.get();

        val kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory
                .newClassPathResource("com/github/pintowar/app/solver/patientAdmissionScheduleScoreRules.drl"));
        val kb = kieServices.newKieBuilder(kieFileSystem);
        kb.buildAll();

        log.info("Criando os paranaue");
        return kieServices.newKieContainer(kb.getKieModule().getReleaseId());
    }

    @Bean
    @Context
    public BeanCreatedEventListener<ObjectMapper> onCreated() {
        // look at: https://stackoverflow.com/questions/53195071/how-to-configure-jackson-to-use-snake-case-in-micronaut
        return event -> {
            val mapper = event.getBean();
            mapper.registerModule(OptaPlannerJacksonModule.createModule());
            return mapper;
        };
    }
}
