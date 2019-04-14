package com.github.pintowar.app;

import com.github.pintowar.app.model.PatientAdmissionSchedule;
import io.reactivex.Single;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieContainer;
import org.optaplanner.core.api.solver.Solver;
import org.optaplanner.core.api.solver.SolverFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

@Slf4j
@Singleton
public class PasService {

    private SolverFactory<PatientAdmissionSchedule> solverFactory;

    @Inject
    public PasService(KieContainer kieContainer) {
        this.solverFactory = SolverFactory
                .createFromKieContainerXmlResource(kieContainer,
                        "com/github/pintowar/app/solver/patientAdmissionScheduleSolverConfig.xml");
    }

    public Single<PatientAdmissionSchedule> solve(PatientAdmissionSchedule pas) {
        Solver<PatientAdmissionSchedule> solver = solverFactory.buildSolver();
        log.info("Solving PAS with id {}", pas.getId());
        return Single.just(solver.solve(pas));
    }
}
