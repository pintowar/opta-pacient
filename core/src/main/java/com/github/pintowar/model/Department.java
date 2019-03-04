/*
 * Copyright 2010 Red Hat, Inc. and/or its affiliates.
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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.util.List;

@Data
@XStreamAlias("Department")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Department {
    @PlanningId
    private Long id;

    private String name;
    private Integer minimumAge = null;
    private Integer maximumAge = null;

    private List<Room> roomList;

    public int countHardDisallowedAdmissionPart(AdmissionPart admissionPart) {
        return countDisallowedPatientAge(admissionPart.getPatient());
    }

    public int countDisallowedPatientAge(Patient patient) {
        int count = 0;
        if (minimumAge != null && patient.getAge() < minimumAge) {
            count += 100;
        }
        if (maximumAge != null && patient.getAge() > maximumAge) {
            count += 100;
        }
        return count;
    }

    public String getLabel() {
        String label = name;
        if (minimumAge != null) {
            label += "(≥" + minimumAge + ")";
        }
        if (maximumAge != null) {
            label += "(≤" + maximumAge + ")";
        }
        return label;
    }

    @Override
    public String toString() {
        return name;
    }

}
