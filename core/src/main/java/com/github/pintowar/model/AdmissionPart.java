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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import org.optaplanner.core.api.domain.lookup.PlanningId;

@Data
@XStreamAlias("AdmissionPart")
public class AdmissionPart {
    @PlanningId
    private Long id;

    private Patient patient;
    private Night firstNight;
    private Night lastNight;
    private Specialism specialism;

    public int getNightCount() {
        return lastNight.getIndex() - firstNight.getIndex() + 1;
    }

    public int calculateSameNightCount(AdmissionPart other) {
        int firstNightIndex = Math.max(getFirstNight().getIndex(), other.getFirstNight().getIndex());
        int lastNightIndex = Math.min(getLastNight().getIndex(), other.getLastNight().getIndex());
        return Math.max(0, lastNightIndex - firstNightIndex + 1);
    }

    @Override
    public String toString() {
        return patient + "(" + firstNight + "-" + lastNight + ")";
    }

}
