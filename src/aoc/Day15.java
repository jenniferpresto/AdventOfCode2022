package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Day15 {
    private static class RangeSlice {
        long low;
        long high;
        RangeSlice(long low, long high) {
            this.low = low;
            this.high = high;
        }

        public boolean containsCoord(long coord) {
            return coord >= low && coord <= high;
        }

        public boolean isContiguous(RangeSlice other) {
            return (this.low <= other.low &&
                        this.high >= other.low) ||
                    (this.high >= other.low &&
                        this.low <= other.high) ||
                    this.high == other.low - 1 ||
                    this.low == other.high + 1;
        }

        public void addRange(RangeSlice other) {
            this.low = Math.min(this.low, other.low);
            this.high = Math.max(this.high, other.high);
        }

        public long getNumSpotsInRange() {
            return Math.abs(high - low) + 1;
        }

        @Override
        public String toString() {
            return low + " - " + high;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;
            RangeSlice other = (RangeSlice) o;
            if (this.high == other.high && this.low == other.low) return true;
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.low, this.high);
        }
    }

    private static class TwoDimensionalRange {
        Sensor sensor;
        Loc top;
        Loc bottom;
        Loc left;
        Loc right;

        TwoDimensionalRange(Sensor sensor) {
            this.sensor = sensor;
            this.top = new Loc(sensor.pos.x, sensor.pos.y - sensor.getManhattanDistanceToBeacon());
            this.bottom = new Loc(sensor.pos.x, sensor.pos.y + sensor.getManhattanDistanceToBeacon());
            this.left = new Loc(sensor.pos.x - sensor.getManhattanDistanceToBeacon(), sensor.pos.y);
            this.right = new Loc(sensor.pos.x + sensor.getManhattanDistanceToBeacon(), sensor.pos.y);
        }

        public Line getTopLeft() { return new Line(top, left); }
        public Line getTopRight() { return new Line(top, right); }
        public Line getBottomLeft() { return new Line(bottom, left); }
        public Line getBottomRight() { return new Line(bottom, right); }

        public List<Loc> getIntersectingPoints(TwoDimensionalRange other) {
            List<Loc> intersections = new ArrayList<>();
            List<Loc> possibilities = new ArrayList<>();
            //  eight possible line intersections;
            //  each intersecting pair of ranges should have two intersecting points
            possibilities.add(getTopLeft().intersectsAt(other.getTopRight()));
            possibilities.add(getTopLeft().intersectsAt(other.getBottomLeft()));
            possibilities.add(getTopRight().intersectsAt(other.getTopLeft()));
            possibilities.add(getTopRight().intersectsAt(other.getBottomRight()));
            possibilities.add(getBottomLeft().intersectsAt(other.getTopLeft()));
            possibilities.add(getBottomLeft().intersectsAt(other.getBottomRight()));
            possibilities.add(getBottomRight().intersectsAt(other.getTopRight()));
            possibilities.add(getBottomRight().intersectsAt(other.getBottomLeft()));

            for (Loc point : possibilities) {
                if (point != null) {
                    intersections.add(point);
                }
            }
            return intersections;
        }

        public boolean fullyContains(TwoDimensionalRange other) {
            if (!(this.top.y <= other.top.y) || !(this.bottom.y >= other.bottom.y)) {
                return false;
            }
            //  get slice of two-dimensional range where sensor is
            RangeSlice row = getRangeSliceForRowAndSensor(other.sensor.pos.y, this.sensor);

            return row.low <= other.left.x && row.high >= other.right.x;
        }

        public boolean containsLoc(Loc loc) {
            if (!(this.top.y <= loc.y) || !(this.bottom.y >= loc.y)) {
                return false;
            }
            RangeSlice row = getRangeSliceForRowAndSensor(loc.y, this.sensor);
            return row.low <= loc.x && row.high >= loc.x;
        }

        @Override
        public String toString() {
            return "Sensor: " + sensor.pos + ": Top: " + top + ", bot: " + bottom + ", left: " + left + ", right: " + right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;
            TwoDimensionalRange other = (TwoDimensionalRange) o;
            if (this.top.equals(other.top)
                && this.bottom.equals(other.bottom)
                && this.left.equals(other.left)
                && this.right.equals(other.right)
                && this.sensor.equals(other.sensor)) {
                return true;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.top, this.bottom, this.left, this.right);
        }
    }

    private static class Loc {
        long x;
        long y;
        Loc(long x, long y) {
            this.x = x;
            this.y = y;
        }

        public long getManhattanDistance(Loc other) {
            return Math.abs(this.x - other.x) + Math.abs(this.y - other.y);
        }

        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }

        public boolean equalsLocAt(long otherX, long otherY) {
            return this.x == otherX && this.y == otherY;
        }

        public boolean isInRectangleRange(Loc topLeft, Loc bottomRight) {
            return topLeft.x <= this.x &&
                    topLeft.y <= this.y &&
                    bottomRight.x >= this.x &&
                    bottomRight.y >= this.y;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;
            Loc other = (Loc)o;
            if (this.x == other.x && this.y == other.y) return true;
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.y);
        }
    }

    private static class Sensor {
        Loc pos;
        Loc beaconPos;
        TwoDimensionalRange range;

        Sensor(Loc pos, Loc beaconPos) {
            this.pos = pos;
            this.beaconPos = beaconPos;
            this.range = new TwoDimensionalRange(this);
        }

        public long getManhattanDistanceToBeacon() {
            return pos.getManhattanDistance(beaconPos);
        }

        @Override
        public String toString() {
            return "Pos: " + pos + ", closest beacon: " + beaconPos;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;
            Sensor other = (Sensor)o;
            return this.pos.equals(other.pos) &&
                    this.beaconPos.equals(other.beaconPos) &&
                    this.range.equals(other.range);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.pos, this.beaconPos);
        }
    }

    private static class Line {
        Loc a;
        Loc b;
        Line(Loc a, Loc b) {
            this.a = a;
            this.b = b;
        }

        public Loc intersectsAt(Line other) {
            //  See https://www.geeksforgeeks.org/program-for-point-of-intersection-of-two-lines/
            //  equation for this line
            double a1 = this.b.y  - this.a.y;
            double b1 = this.a.x - this.b.x;
            double c1 = a1 * (this.a.x) + b1 * this.a.y;

            //  equation for other line
            double a2 = other.b.y  - other.a.y;
            double b2 = other.a.x - other.b.x;
            double c2 = a2 * (other.a.x) + b2 * other.a.y;

            double determinant = a1 * b2 - a2 * b1;
            //  lines are parallel; we shouldn't hit this
            if (determinant == 0) {
                return null;
            }
            double x = (b2 * c1 - b1 * c2) / determinant;
            double y = (a1 * c2 - a2 * c1) / determinant;
            //  since all slopes are 1 or -1, all of our lines should intersect at integer points
            Loc intersection = new Loc((long)x, (long)y);
            //  see if intersection is on both line segments
            if ((Math.min(a.x, b.x) <= intersection.x && Math.max(a.x, b.x) >= intersection.x) &&
                    (Math.min(a.y, b.y) <= intersection.y && Math.max(a.y, b.y) >= intersection.y) &&
                    (Math.min(other.a.x, other.b.x) <= intersection.x && Math.max(other.a.x, other.b.x) >= intersection.x) &&
                    (Math.min(other.a.y, other.b.y) <= intersection.y && Math.max(other.a.y, other.b.y) >= intersection.y)) {
                return intersection;
            }
            return null;
        }

        @Override
        public String toString() {
            return a + " - " + b;
        }
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/Day15.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }

        Set<Sensor> sensors = createSensors(data);

        //  Part 1
        long testRow = 2000000; // change to 10 for test data; 2000000 for real data
        Set<RangeSlice> ranges = getRangesForRow(sensors, testRow);
        Set<RangeSlice> consolidatedRanges = consolidateOneDimensionalRanges(ranges);
        long numTotalSpots = 0;
        for (RangeSlice range : consolidatedRanges) {
            numTotalSpots += range.getNumSpotsInRange();
        }
        Set<Loc> beaconLocationsInRange = new HashSet<>();
        for (Sensor sensor : sensors) {
            for (RangeSlice range : consolidatedRanges) {
                if (sensor.beaconPos.y == testRow && range.containsCoord(sensor.beaconPos.x)) {
                    Loc beaconPos = new Loc(sensor.beaconPos.x, testRow);
                    if (!beaconLocationsInRange.contains(beaconPos)) {
                        beaconLocationsInRange.add(beaconPos);
                    }
                    break;
                }
            }
        }
        System.out.println("Part 1: Number spots without a beacon: " + (numTotalSpots - beaconLocationsInRange.size()));

        //  Part 2
        List<Sensor> sensorList = new ArrayList<>(sensors);
        //  Since there's only one possible location,
        //  we know it has to be adjacent to an intersection between sensor ranges.
        //  Get all the intersecting points
        Set<Loc> intersectingPoints = new HashSet<>();
        for (int i = 0; i < sensorList.size(); i++) {
            for (int j = i + 1; j < sensorList.size(); j++) {
                Sensor sensor1 = sensorList.get(i);
                Sensor sensor2 = sensorList.get(j);
                TwoDimensionalRange range1 = sensor1.range;
                TwoDimensionalRange range2 = sensor2.range;
                if (!range1.fullyContains(range2) && !range2.fullyContains(range1)) {
                    intersectingPoints.addAll(range1.getIntersectingPoints(range2));
                }
            }
        }

        //  get all points immediately around the intersections
        Set<Loc> possiblePoints = new HashSet<>();
        for (Loc point : intersectingPoints) {
            possiblePoints.add(new Loc(point.x - 1, point.y));
            possiblePoints.add(new Loc(point.x + 1, point.y));
            possiblePoints.add(new Loc(point.x, point.y - 1));
            possiblePoints.add(new Loc(point.x, point.y + 1));
        }

        //  the point will not be located within any of the sensors' ranges
        Set<Loc> orphanPoints = new HashSet<>();
        for (Loc point : possiblePoints) {
            boolean pointIsContainedSomewhere = false;
            for (Sensor sensor : sensors) {
                if (sensor.range.containsLoc(point)) {
                    pointIsContainedSomewhere = true;
                    break;
                }
            }
            if (!pointIsContainedSomewhere &&
                    point.isInRectangleRange(new Loc(0, 0), new Loc(4000000, 4000000))) {
                    orphanPoints.add(point);
            }
        }
        if (orphanPoints.size() > 1) {
            System.out.println("We have too many possibilities");
        }
        Loc winner = orphanPoints.iterator().next();
        long frequency = winner.x * 4000000 + winner.y;
        System.out.println("Part 2: Frequency of only point, " + winner + ": " + frequency);

//        for (Sensor sensor : sensors) {
////            if (sensor.pos.equals(testSensorPos1) || sensor.pos.equals(testSensorPos2) || sensor.pos.equals(testSensorPos3)) {
//            if (sensor.pos.equals(testSensorPos1) || sensor.pos.equals(testSensorPos2)) {
//                testSensors.add(sensor);
//            }
//        }
//        Loc testSensorPos1 = new Loc(12, 14);
//        Loc testSensorPos2 = new Loc(20, 14);
//
//        List<Sensor> testSensorList = new ArrayList<>(testSensors);
//        Sensor testSensor1 = testSensorList.get(0);
//        Sensor testSensor2 = testSensorList.get(1);
//        System.out.println("Sensor at " + testSensor1.pos + " contains " + testSensor2.pos + "? " + testSensor1.range.fullyContains(testSensor2.range));
//        System.out.println("Sensor at " + testSensor2.pos + " contains " + testSensor1.pos + "? " + testSensor2.range.fullyContains(testSensor1.range));
//
//        System.out.println("Intersecting points");
//        for (Loc point : testSensor1.range.getIntersectingPoints(testSensor2.range)) {
//            System.out.println(point);
//        }
////        printSectionOfMap(testSensors, new Loc(0, 0), new Loc(23, 23));
////        System.out.println("Whole thing");
////        printSectionOfMap(sensors, new Loc(0, 0), new Loc(20, 20));
    }

    static RangeSlice getRangeSliceForRowAndSensor(long row, Sensor sensor) {
        long manDist = sensor.getManhattanDistanceToBeacon();
        long rowDistFromSensor = Math.abs(row - sensor.pos.y);
        if (rowDistFromSensor > manDist) { return null; }
        long leftX = sensor.pos.x - (manDist - rowDistFromSensor);
        long rightX = sensor.pos.x + (manDist - rowDistFromSensor);
        return new RangeSlice(leftX, rightX);
    }

    static Set<RangeSlice> getRangesForRow(Set<Sensor> sensors, long rowY) {
        Set<RangeSlice> rangesForRow = new HashSet<>();
        for (Sensor sensor : sensors) {
            RangeSlice range = getRangeSliceForRowAndSensor(rowY, sensor);
            if (range != null) {
                rangesForRow.add(range);
            }
        }
        return rangesForRow;
    }

    static Set<RangeSlice> consolidateOneDimensionalRanges(Set<RangeSlice> startingSet) {
        boolean stillConsolidating = true;
        Set<RangeSlice> rangeSet = new HashSet<>(startingSet);
        //  compare all the ranges against each other until we've consolidated them all
        while(stillConsolidating) {
            List<RangeSlice> rangeList = new ArrayList<>(rangeSet);
            List<RangeSlice> rangesToRemove = new ArrayList<>();
            boolean didRemove = false;
            for (int i = 0; i < rangeList.size(); i++) {
                if (didRemove) break;
                for (int j = i + 1; j < rangeList.size(); j++) {
                    if (rangeList.get(i).isContiguous(rangeList.get(j))) {
                        rangeList.get(i).addRange(rangeList.get(j));
                        rangesToRemove.add(rangeList.get(j));
                    }
                }
                if (rangesToRemove.size() > 0) {
                    for (RangeSlice range : rangesToRemove) {
                        rangeSet.remove(range);
                    }
                    didRemove = true;
                }
            }
            if (!didRemove) {
                stillConsolidating = false;
            }

        }
        return rangeSet;
    }

    static Set<Sensor> createSensors(List<String> data) {
        Set<Sensor> sensors = new HashSet<>();

        //  parse data, save beacons and sensors
        for (String line : data) {
            String[] instructions = line.split(" ");
            long posX = Long.valueOf(instructions[2].substring(2, instructions[2].length() - 1));
            long posY = Long.valueOf(instructions[3].substring(2, instructions[3].length() - 1));
            long beaX = Long.valueOf(instructions[8].substring(2, instructions[8].length() - 1));
            long beaY = Long.valueOf(instructions[9].substring(2));
            Sensor sensor = new Sensor(new Loc(posX, posY), new Loc(beaX, beaY));
            sensors.add(sensor);
        }
        return sensors;
    }
    static void printSectionOfMap(Set<Sensor> sensors, Loc topLeft, Loc bottomRight) {
        for (long y = topLeft.y; y < bottomRight.y; y++) {
            for (long x = topLeft.x; x < bottomRight.x; x++) {
                Loc printLoc = new Loc(x, y);
                String locOutput = ".";
                for (Sensor sensor : sensors) {
                    if (sensor.pos.equals(printLoc)) {
                        locOutput = "S";
                        break;
                    }
                    if (sensor.beaconPos.equals(printLoc)) {
                        locOutput = "B";
                        break;
                    }
                    RangeSlice rangeSlice = getRangeSliceForRowAndSensor(y, sensor);
                    if (rangeSlice != null && rangeSlice.containsCoord(x)) {
                        locOutput = "#";
                    }
                }
                System.out.print(locOutput);
            }
            System.out.print("\n");
        }
    }
}
