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

import com.fasterxml.jackson.annotation.*;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.optaplanner.core.api.domain.lookup.PlanningId;

import java.util.List;

@Data
@XStreamAlias("Room")
@JsonIdentityInfo(generator = ObjectIdGenerators.IntSequenceGenerator.class)
@EqualsAndHashCode(exclude = {"roomSpecialismList", "roomEquipmentList", "bedList"})
public class Room {
    @PlanningId
    private Long id;

    private String name;

    @JsonBackReference("roomDepartment")
    private Department department;
    private int capacity;
    private GenderLimitation genderLimitation;

    @JsonManagedReference("roomspecialism")
    private List<RoomSpecialism> roomSpecialismList;
    @JsonManagedReference("roomequipment")
    private List<RoomEquipment> roomEquipmentList;
    @JsonManagedReference("roombed")
    private List<Bed> bedList;

    public int countHardDisallowedAdmissionPart(AdmissionPart admissionPart) {
        return countMissingRequiredRoomProperties(admissionPart.getPatient())
                + department.countHardDisallowedAdmissionPart(admissionPart)
                + countDisallowedPatientGender(admissionPart.getPatient());
        // TODO preferredMaximumRoomCapacity and specialism
    }

    public int countMissingRequiredRoomProperties(Patient patient) {
        int count = 0;
        for (RequiredPatientEquipment requiredPatientEquipment : patient.getRequiredPatientEquipmentList()) {
            Equipment requiredEquipment = requiredPatientEquipment.getEquipment();
            boolean hasRequiredEquipment = false;
            for (RoomEquipment roomEquipment : roomEquipmentList) {
                if (roomEquipment.getEquipment().equals(requiredEquipment)) {
                    hasRequiredEquipment = true;
                }
            }
            if (!hasRequiredEquipment) {
                count += 100000;
            }
        }
        return count;
    }

    public int countDisallowedPatientGender(Patient patient) {
        switch (genderLimitation) {
            case ANY_GENDER:
                return 0;
            case MALE_ONLY:
                return patient.getGender() == Gender.MALE ? 0 : 4;
            case FEMALE_ONLY:
                return patient.getGender() == Gender.FEMALE ? 0 : 4;
            case SAME_GENDER:
                // scoreRules check this
                return 1;
            default:
                throw new IllegalStateException("The genderLimitation (" + genderLimitation + ") is not implemented.");
        }
    }

    public int countSoftDisallowedAdmissionPart(AdmissionPart admissionPart) {
        return countMissingPreferredRoomProperties(admissionPart.getPatient());
        // TODO preferredMaximumRoomCapacity and specialism
    }

    public int countMissingPreferredRoomProperties(Patient patient) {
        int count = 0;
        for (PreferredPatientEquipment preferredPatientEquipment : patient.getPreferredPatientEquipmentList()) {
            Equipment preferredEquipment = preferredPatientEquipment.getEquipment();
            boolean hasPreferredEquipment = false;
            for (RoomEquipment roomEquipment : roomEquipmentList) {
                if (roomEquipment.getEquipment().equals(preferredEquipment)) {
                    hasPreferredEquipment = true;
                }
            }
            if (!hasPreferredEquipment) {
                count += 20;
            }
        }
        return count;
    }

    @JsonIgnore
    public String getLabel() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
