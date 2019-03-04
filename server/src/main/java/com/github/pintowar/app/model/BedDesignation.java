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

package com.github.pintowar.app.model;

import com.github.pintowar.app.model.solver.BedDesignationDifficultyWeightFactory;
import com.github.pintowar.app.model.solver.BedStrengthComparator;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

@Data
@PlanningEntity(difficultyWeightFactoryClass = BedDesignationDifficultyWeightFactory.class)
@XStreamAlias("BedDesignation")
public class BedDesignation {
    @PlanningId
    private Long id;

    private AdmissionPart admissionPart;
    @PlanningVariable(nullable = true, valueRangeProviderRefs = {"bedRange"},
            strengthComparatorClass = BedStrengthComparator.class)
    private Bed bed;

    // ************************************************************************
    // Complex methods
    // ************************************************************************

    public Patient getPatient() {
        return admissionPart.getPatient();
    }

    public Gender getPatientGender() {
        return admissionPart.getPatient().getGender();
    }

    public int getPatientAge() {
        return admissionPart.getPatient().getAge();
    }

    public Integer getPatientPreferredMaximumRoomCapacity() {
        return admissionPart.getPatient().getPreferredMaximumRoomCapacity();
    }

    public Specialism getAdmissionPartSpecialism() {
        return admissionPart.getSpecialism();
    }

    public int getFirstNightIndex() {
        return admissionPart.getFirstNight().getIndex();
    }

    public int getLastNightIndex() {
        return admissionPart.getLastNight().getIndex();
    }

    public int getAdmissionPartNightCount() {
        return admissionPart.getNightCount();
    }

    public Room getRoom() {
        if (bed == null) {
            return null;
        }
        return bed.getRoom();
    }

    public int getRoomCapacity() {
        if (bed == null) {
            return Integer.MIN_VALUE;
        }
        return bed.getRoom().getCapacity();
    }

    public Department getDepartment() {
        if (bed == null) {
            return null;
        }
        return bed.getRoom().getDepartment();
    }

    public GenderLimitation getRoomGenderLimitation() {
        if (bed == null) {
            return null;
        }
        return bed.getRoom().getGenderLimitation();
    }

    @Override
    public String toString() {
        return admissionPart.toString();
    }

}
