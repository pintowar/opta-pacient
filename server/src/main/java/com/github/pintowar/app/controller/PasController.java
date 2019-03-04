package com.github.pintowar.app.controller;

import com.github.pintowar.app.model.PatientAdmissionSchedule;
import com.github.pintowar.app.util.PasReader;
import com.google.common.collect.ImmutableMap;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.security.AnyTypePermission;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.stream.Stream;

@RestController
public class PasController {

    @Autowired
    private ResourceLoader resourceLoader;

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

}
