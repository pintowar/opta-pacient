package com.github.pintowar.app.service;

import com.github.pintowar.app.model.PatientAdmissionSchedule;
import lombok.extern.slf4j.Slf4j;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class PasService {
    private SolverFactory<PatientAdmissionSchedule> solverFactory = SolverFactory
            .createFromXmlResource("com/github/pintowar/app/solver/patientAdmissionScheduleSolverConfig.xml");


    public Mono<PatientAdmissionSchedule> solve(PatientAdmissionSchedule pas) {
        Solver<PatientAdmissionSchedule> solver = solverFactory.buildSolver();
        log.info("Solving PAS with id {}", pas.getId());
        return Mono.just(solver.solve(pas));
    }
}
