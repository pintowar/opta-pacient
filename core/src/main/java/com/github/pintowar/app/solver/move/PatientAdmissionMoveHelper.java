/*
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.pintowar.app.solver.move;


import com.github.pintowar.app.model.Bed;
import com.github.pintowar.app.model.BedDesignation;
import com.github.pintowar.app.model.PatientAdmissionSchedule;
import org.optaplanner.core.impl.score.director.ScoreDirector;

public class PatientAdmissionMoveHelper {

    public static void moveBed(ScoreDirector<PatientAdmissionSchedule> scoreDirector, BedDesignation bedDesignation, Bed toBed) {
        scoreDirector.beforeVariableChanged(bedDesignation, "bed");
        bedDesignation.setBed(toBed);
        scoreDirector.afterVariableChanged(bedDesignation, "bed");
    }

    private PatientAdmissionMoveHelper() {
    }

}
