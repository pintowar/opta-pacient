package com.github.pintowar.app;


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
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
        val testData = IntStream.range(1, 14)
                .mapToObj(it -> String.format("classpath:data/xml/testdata%02d.xml", it));
        val allFiles = Stream.concat(testData, Stream.of("classpath:data/xml/overconstrained01.xml"));
        return fromStream(allFiles).map(loader::getResource)
                .flatMap(it -> fromOptional(it))
                .map(it -> it.getFile().split("/"))
                .map(it -> it[it.length - 1].replace(".xml", ""))
                .sorted()
                .map(it -> ImmutableMap.of("instance", it));
    }

    @Get(value = "/{id}", produces = {MediaType.APPLICATION_JSON})
    public Single<PatientAdmissionSchedule> findByName(@PathVariable String id) {
        val path = String.format("classpath:data/xml/%s.xml", id);
        return fromOptional(loader.getResource(path).map(PasReader::readFromXml)).singleOrError();
    }

    @Put(produces = {MediaType.APPLICATION_JSON}, consumes = {MediaType.APPLICATION_JSON})
    public Single<PatientAdmissionSchedule> solve(@Body Single<PatientAdmissionSchedule> pas) {
        return pas.flatMap(service::solve);
    }
}
