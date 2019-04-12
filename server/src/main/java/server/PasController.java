package server;


import com.github.pintowar.app.model.PatientAdmissionSchedule;
import com.github.pintowar.app.util.PasReader;
import com.google.common.collect.ImmutableMap;
import io.micronaut.core.io.ResourceLoader;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.core.io.scan.ClassPathResourceLoader;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.reactivex.Flowable;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import javax.inject.Inject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

import static hu.akarnokd.rxjava2.interop.FlowableInterop.fromOptional;
import static hu.akarnokd.rxjava2.interop.FlowableInterop.fromStream;

@Slf4j
@Controller("/pas")
public class PasController {

    private PasService service;
    private ResourceResolver resourceResolver = new ResourceResolver();
    private ResourceLoader loader = resourceResolver.getLoader(ClassPathResourceLoader.class).get();

    @Inject
    public PasController(PasService service) {
        this.service = service;
    }

    @Get(produces = MediaType.APPLICATION_JSON)
    public Flowable<Map<String, String>> index() {
        val resource = loader.getResources("classpath:data/xml/");
        return fromStream(resource).map(it -> Paths.get(it.toURI()))
                .flatMap(it -> fromStream(Files.list(it)))
                .map(it ->
                        ImmutableMap.of("instance", it.getFileName().toString().replace(".xml", ""))
                );
    }

    @Get(value = "/{id}", produces = {MediaType.APPLICATION_JSON})
    public Single<PatientAdmissionSchedule> findByName(@PathVariable String id) {
        val path = String.format("classpath:data/xml/%s.xml", id);
        return fromOptional(loader.getResource(path).map(PasReader::readFromXml)).singleOrError();
    }

    @Put(value = "/pas/", produces = {MediaType.APPLICATION_JSON}, consumes = {MediaType.APPLICATION_JSON})
    public Single<PatientAdmissionSchedule> solve(@Body PatientAdmissionSchedule pas) {
        return service.solve(pas);
    }
}
