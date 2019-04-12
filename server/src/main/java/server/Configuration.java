package server;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.context.annotation.Bean;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Replaces;
import io.micronaut.jackson.JacksonConfiguration;
import io.micronaut.jackson.ObjectMapperFactory;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.internal.io.ResourceFactory;
import org.optaplanner.persistence.jackson.api.OptaPlannerJacksonModule;

import javax.annotation.Nullable;
import javax.inject.Singleton;

@Slf4j
@Factory
@Replaces(ObjectMapperFactory.class)
public class Configuration extends ObjectMapperFactory {

    @Bean
    @Context
    public KieContainer kieContainer() {
        val kieServices = KieServices.Factory.get();

        val kieFileSystem = kieServices.newKieFileSystem();
        kieFileSystem.write(ResourceFactory
                .newClassPathResource("com/github/pintowar/app/solver/patientAdmissionScheduleScoreRules.drl"));
        val kb = kieServices.newKieBuilder(kieFileSystem);
        kb.buildAll();

        return kieServices.newKieContainer(kb.getKieModule().getReleaseId());
    }

    @Override
    @Singleton
    @Replaces(ObjectMapper.class)
    public ObjectMapper objectMapper(@Nullable JacksonConfiguration jacksonConfiguration, @Nullable JsonFactory jsonFactory) {
        // look at: https://stackoverflow.com/questions/53195071/how-to-configure-jackson-to-use-snake-case-in-micronaut
        final ObjectMapper mapper = super.objectMapper(jacksonConfiguration, jsonFactory);
        mapper.registerModule(OptaPlannerJacksonModule.createModule());
        return mapper;
    }
}
