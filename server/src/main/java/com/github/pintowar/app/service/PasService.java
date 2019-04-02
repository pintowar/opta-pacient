package com.github.pintowar.app.service;

import com.github.pintowar.app.model.PatientAdmissionSchedule;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.kie.api.runtime.KieContainer;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.optaplanner.core.api.solver.event.BestSolutionChangedEvent;
import org.optaplanner.core.api.solver.event.SolverEventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.function.Consumer;

@Slf4j
@Service
public class PasService {

    private SolverFactory<PatientAdmissionSchedule> solverFactory;

    public PasService(KieContainer kieContainer) {
        this.solverFactory = SolverFactory
                .createFromKieContainerXmlResource(kieContainer,
                        "com/github/pintowar/app/solver/patientAdmissionScheduleSolverConfig.xml");
    }

    public Mono<PatientAdmissionSchedule> solve(PatientAdmissionSchedule pas) {
        Solver<PatientAdmissionSchedule> solver = solverFactory.buildSolver();
        log.info("Solving PAS with id {}", pas.getId());
        return Mono.just(solver.solve(pas));
    }
}
