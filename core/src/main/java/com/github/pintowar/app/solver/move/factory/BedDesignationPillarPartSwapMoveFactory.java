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

package com.github.pintowar.app.solver.move.factory;

import com.github.pintowar.app.model.Bed;
import com.github.pintowar.app.model.BedDesignation;
import com.github.pintowar.app.model.PatientAdmissionSchedule;
import com.github.pintowar.app.solver.move.BedChangeMove;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.optaplanner.core.impl.heuristic.move.CompositeMove;
import org.optaplanner.core.impl.heuristic.move.Move;
import org.optaplanner.core.impl.heuristic.selector.move.factory.MoveListFactory;

import java.util.*;

public class BedDesignationPillarPartSwapMoveFactory implements MoveListFactory<PatientAdmissionSchedule> {

    @Override
    public List<Move<PatientAdmissionSchedule>> createMoveList(PatientAdmissionSchedule patientAdmissionSchedule) {
        Map<Bed, List<BedDesignation>> bedToBedDesignationList = new HashMap<>(
                patientAdmissionSchedule.getBedList().size());
        for (BedDesignation bedDesignation : patientAdmissionSchedule.getBedDesignationList()) {
            List<BedDesignation> bedDesignationListPerBed = bedToBedDesignationList.computeIfAbsent(bedDesignation.getBed(),
                    // Note: the initialCapacity is probably too high,
                    // which is bad for memory, but the opposite is bad for performance (which is worse)
                    k -> new ArrayList<>(patientAdmissionSchedule.getNightList().size()));
            bedDesignationListPerBed.add(bedDesignation);
        }
        for (List<BedDesignation> bedDesignationListPerBed : bedToBedDesignationList.values()) {
            Collections.sort(bedDesignationListPerBed, (a, b) -> {
                // This comparison is sameBedInSameNight safe.
                return new CompareToBuilder()
                        .append(a.getAdmissionPart().getFirstNight(), b.getAdmissionPart().getFirstNight())
                        .append(a.getAdmissionPart().getLastNight(), b.getAdmissionPart().getLastNight())
                        .append(a.getAdmissionPart(), b.getAdmissionPart())
                        .toComparison();
            });
        }

        List<Bed> bedList = patientAdmissionSchedule.getBedList();
        List<Move<PatientAdmissionSchedule>> moveList = new ArrayList<>();

        // For every 2 distinct beds
        for (ListIterator<Bed> leftBedIt = bedList.listIterator(); leftBedIt.hasNext(); ) {
            Bed leftBed = leftBedIt.next();
            for (ListIterator<Bed> rightBedIt = bedList.listIterator(leftBedIt.nextIndex());
                 rightBedIt.hasNext(); ) {
                Bed rightBed = rightBedIt.next();
                List<BedDesignation> leftBedDesignationList = bedToBedDesignationList.get(leftBed);
                if (leftBedDesignationList == null) {
                    leftBedDesignationList = Collections.emptyList();
                }
                List<BedDesignation> rightBedDesignationList = bedToBedDesignationList.get(rightBed);
                if (rightBedDesignationList == null) {
                    rightBedDesignationList = Collections.emptyList();
                }
                LowestFirstNightBedDesignationIterator lowestIt = new LowestFirstNightBedDesignationIterator(
                        leftBedDesignationList, rightBedDesignationList);
                // For every pillar part duo
                while (lowestIt.hasNext()) {
                    BedDesignation pillarPartBedDesignation = lowestIt.next();
                    // Note: the initialCapacity is probably too high,
                    // which is bad for memory, but the opposite is bad for performance (which is worse)
                    List<BedChangeMove> moveListByPillarPartDuo = new ArrayList<>(
                            leftBedDesignationList.size() + rightBedDesignationList.size());
                    int lastNightIndex = pillarPartBedDesignation.getAdmissionPart().getLastNight().getIndex();
                    Bed otherBed;
                    int leftMinimumFirstNightIndex = Integer.MIN_VALUE;
                    int rightMinimumFirstNightIndex = Integer.MIN_VALUE;
                    if (lowestIt.isLastNextWasLeft()) {
                        otherBed = rightBed;
                        leftMinimumFirstNightIndex = lastNightIndex;
                    } else {
                        otherBed = leftBed;
                        rightMinimumFirstNightIndex = lastNightIndex;
                    }
                    moveListByPillarPartDuo.add(new BedChangeMove(pillarPartBedDesignation, otherBed));
                    // For every BedDesignation in that pillar part duo
                    while (lowestIt.hasNextWithMaximumFirstNightIndexes(
                            leftMinimumFirstNightIndex, rightMinimumFirstNightIndex)) {
                        pillarPartBedDesignation = lowestIt.next();
                        lastNightIndex = pillarPartBedDesignation.getAdmissionPart().getLastNight().getIndex();
                        if (lowestIt.isLastNextWasLeft()) {
                            otherBed = rightBed;
                            leftMinimumFirstNightIndex = Math.max(leftMinimumFirstNightIndex, lastNightIndex);
                        } else {
                            otherBed = leftBed;
                            rightMinimumFirstNightIndex = Math.max(rightMinimumFirstNightIndex, lastNightIndex);
                        }
                        moveListByPillarPartDuo.add(new BedChangeMove(pillarPartBedDesignation, otherBed));
                    }
                    moveList.add(CompositeMove.buildMove(moveListByPillarPartDuo));
                }
            }
        }
        return moveList;
    }

    private static class LowestFirstNightBedDesignationIterator implements Iterator<BedDesignation> {

        private Iterator<BedDesignation> leftIterator;
        private Iterator<BedDesignation> rightIterator;

        private boolean leftHasNext = true;
        private boolean rightHasNext = true;

        private BedDesignation nextLeft;
        private BedDesignation nextRight;

        private boolean lastNextWasLeft;

        public LowestFirstNightBedDesignationIterator(
                List<BedDesignation> leftBedDesignationList, List<BedDesignation> rightBedDesignationList) {
            // Buffer the nextLeft and nextRight
            leftIterator = leftBedDesignationList.iterator();
            if (leftIterator.hasNext()) {
                nextLeft = leftIterator.next();
            } else {
                leftHasNext = false;
                nextLeft = null;
            }
            rightIterator = rightBedDesignationList.iterator();
            if (rightIterator.hasNext()) {
                nextRight = rightIterator.next();
            } else {
                rightHasNext = false;
                nextRight = null;
            }
        }

        @Override
        public boolean hasNext() {
            return leftHasNext || rightHasNext;
        }

        public boolean hasNextWithMaximumFirstNightIndexes(
                int leftMinimumFirstNightIndex, int rightMinimumFirstNightIndex) {
            if (!hasNext()) {
                return false;
            }
            boolean nextIsLeft = nextIsLeft();
            if (nextIsLeft) {
                int firstNightIndex = nextLeft.getAdmissionPart().getFirstNight().getIndex();
                // It should not be conflict in the same pillar and it should be in conflict with the other pillar
                return firstNightIndex > leftMinimumFirstNightIndex && firstNightIndex <= rightMinimumFirstNightIndex;
            } else {
                int firstNightIndex = nextRight.getAdmissionPart().getFirstNight().getIndex();
                // It should not be conflict in the same pillar and it should be in conflict with the other pillar
                return firstNightIndex > rightMinimumFirstNightIndex && firstNightIndex <= leftMinimumFirstNightIndex;
            }
        }

        @Override
        public BedDesignation next() {
            lastNextWasLeft = nextIsLeft();
            // Buffer the nextLeft or nextRight
            BedDesignation lowest;
            if (lastNextWasLeft) {
                lowest = nextLeft;
                if (leftIterator.hasNext()) {
                    nextLeft = leftIterator.next();
                } else {
                    leftHasNext = false;
                    nextLeft = null;
                }
            } else {
                lowest = nextRight;
                if (rightIterator.hasNext()) {
                    nextRight = rightIterator.next();
                } else {
                    rightHasNext = false;
                    nextRight = null;
                }
            }
            return lowest;
        }

        private boolean nextIsLeft() {
            boolean returnLeft;
            if (leftHasNext) {
                if (rightHasNext) {
                    int leftFirstNightIndex = nextLeft.getAdmissionPart().getFirstNight().getIndex();
                    int rightFirstNightIndex = nextRight.getAdmissionPart().getFirstNight().getIndex();
                    returnLeft = leftFirstNightIndex <= rightFirstNightIndex;
                } else {
                    returnLeft = true;
                }
            } else {
                if (rightHasNext) {
                    returnLeft = false;
                } else {
                    throw new NoSuchElementException();
                }
            }
            return returnLeft;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("The optional operation remove() is not supported.");
        }

        public boolean isLastNextWasLeft() {
            return lastNextWasLeft;
        }

    }

}
