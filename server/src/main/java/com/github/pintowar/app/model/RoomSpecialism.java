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

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import org.optaplanner.core.api.domain.lookup.PlanningId;

@Data
@XStreamAlias("RoomSpecialism")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class RoomSpecialism {
    @PlanningId
    private Long id;

    private Room room;
    private Specialism specialism;

    private int priority; // AKA choice

    @Override
    public String toString() {
        return room + "-" + specialism;
    }

}
