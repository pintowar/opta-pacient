/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates.
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

package com.github.pintowar.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import lombok.Data;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.drools.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardmediumsoft.HardMediumSoftScore;
import org.optaplanner.persistence.xstream.api.score.buildin.hardmediumsoft.HardMediumSoftScoreXStreamConverter;

import java.util.List;

@Data
@PlanningSolution
@XStreamAlias("PatientAdmissionSchedule")
public class PatientAdmissionSchedule {
    @PlanningId
    private Long id;

    @ProblemFactCollectionProperty
    private List<Specialism> specialismList;
    @ProblemFactCollectionProperty
    private List<Equipment> equipmentList;
    @ProblemFactCollectionProperty
    private List<Department> departmentList;
    @ProblemFactCollectionProperty
    private List<DepartmentSpecialism> departmentSpecialismList;
    @ProblemFactCollectionProperty
    private List<Room> roomList;
    @ProblemFactCollectionProperty
    private List<RoomSpecialism> roomSpecialismList;
    @ProblemFactCollectionProperty
    private List<RoomEquipment> roomEquipmentList;
    @ValueRangeProvider(id = "bedRange")
    @ProblemFactCollectionProperty
    private List<Bed> bedList;
    @ProblemFactCollectionProperty
    private List<Night> nightList;
    @ProblemFactCollectionProperty
    private List<Patient> patientList;
    @ProblemFactCollectionProperty
    private List<AdmissionPart> admissionPartList;
    @ProblemFactCollectionProperty
    private List<RequiredPatientEquipment> requiredPatientEquipmentList;
    @ProblemFactCollectionProperty
    private List<PreferredPatientEquipment> preferredPatientEquipmentList;
    @PlanningEntityCollectionProperty
    private List<BedDesignation> bedDesignationList;

    @XStreamConverter(HardMediumSoftScoreXStreamConverter.class)
    @PlanningScore
    private HardMediumSoftScore score;

}
