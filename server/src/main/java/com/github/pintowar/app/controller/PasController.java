package com.github.pintowar.app.controller;

import com.github.pintowar.app.model.PatientAdmissionSchedule;
import com.github.pintowar.app.service.PasService;
import com.github.pintowar.app.util.PasReader;
import com.google.common.collect.ImmutableMap;
import lombok.val;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Stream;

@RestController
public class PasController {

    private ResourceLoader resourceLoader;
    private PasService service;

    public PasController(ResourceLoader resourceLoader, PasService service) {
        this.resourceLoader = resourceLoader;
        this.service = service;
    }

    @GetMapping(value = "/pas/", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Flux<Map<String, String>> index() throws IOException {
        val path = "classpath:data/xml/*.xml";
        val resources = Stream.of(ResourcePatternUtils.getResourcePatternResolver(resourceLoader)
                .getResources(path));
        return Flux.fromStream(resources.map(it ->
                ImmutableMap.of("instance", it.getFilename().replace(".xml", "")))
        );
    }

    @GetMapping(value = "/pas/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<PatientAdmissionSchedule> findByName(@PathVariable String id) throws IOException {
        val path = String.format("classpath:data/xml/%s.xml", id);
        val pas = PasReader.readFromXml(ResourceUtils.getURL(path));

        return Mono.justOrEmpty(pas);
    }

    @PutMapping(value = "/pas/", produces = {MediaType.APPLICATION_JSON_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    public Mono<PatientAdmissionSchedule> solve(@RequestBody PatientAdmissionSchedule pas) {
        return service.solve(pas);
    }
}
