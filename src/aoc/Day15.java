package aoc;

import java.awt.*;
import java.io.File;
import java.lang.annotation.Native;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Day15 {
    private static class CoordinateRange {
        long low;
        long high;
        CoordinateRange(long low, long high) {
            this.low = low;
            this.high = high;
        }

        public boolean containsCoord(long coord) {
            return coord >= low && coord <= high;
        }

        public boolean isContiguous(CoordinateRange other) {
            return (this.low <= other.low &&
                        this.high >= other.low) ||
                    (this.high >= other.low &&
                        this.low <= other.high) ||
                    this.high == other.low - 1 ||
                    this.low == other.high + 1;
        }

        public void addRange(CoordinateRange other) {
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
            CoordinateRange other = (CoordinateRange) o;
            if (this.high == other.high && this.low == other.low) return true;
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.low, this.high);
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
        Sensor(Loc pos, Loc beaconPos) {
            this.pos = pos;
            this.beaconPos = beaconPos;
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
            if (this.pos.equals(other.pos) && this.beaconPos.equals(other.beaconPos)) return true;
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.pos, this.beaconPos);
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

        Set<Sensor> sensors = new HashSet<>();
        Map<Loc, String> knownLocs = new HashMap<>();

        //  parse data, save beacons and sensors
        for (String line : data) {
            String[] instructions = line.split(" ");
            long posX = Long.valueOf(instructions[2].substring(2, instructions[2].length() - 1));
            long posY = Long.valueOf(instructions[3].substring(2, instructions[3].length() - 1));
            long beaX = Long.valueOf(instructions[8].substring(2, instructions[8].length() - 1));
            long beaY = Long.valueOf(instructions[9].substring(2));
            Loc sensorPos = new Loc(posX, posY);
            Loc beaconPos = new Loc(beaX, beaY);
            Sensor sensor = new Sensor(sensorPos, beaconPos);
            sensors.add(sensor);
            knownLocs.put(sensorPos, "S");
            knownLocs.put(beaconPos, "B");
        }
//        printSectionOfMap(sensors, new Loc(-2, -2), new Loc(26, 23));

//        long testRow = 10;
        long testRow = 2000000;
        Set<CoordinateRange> ranges = getRangesForRow(sensors, testRow);
        Set<CoordinateRange> consolidatedRanges = consolidateRanges(ranges);
        long numTotalSpots = 0;
        for (CoordinateRange range : consolidatedRanges) {
            System.out.println("Range has this many spots: " + range.getNumSpotsInRange() + " range is " + range);
            numTotalSpots += range.getNumSpotsInRange();
        }
        Set<Loc> beaconLocationsInRange = new HashSet<>();
        for (Sensor sensor : sensors) {
            for (CoordinateRange range : consolidatedRanges) {
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

//        long testY = 2000000;
        int jennifer = 9;
    }

    static CoordinateRange getKnownRangeForRowAndSensor(long row, Sensor sensor) {
        long manDist = sensor.getManhattanDistanceToBeacon();
        long rowDistFromSensor = Math.abs(row - sensor.pos.y);
        if (rowDistFromSensor > manDist) { return null; }
        long leftX = sensor.pos.x - (manDist - rowDistFromSensor);
        long rightX = sensor.pos.x + (manDist - rowDistFromSensor);
        return new CoordinateRange(leftX, rightX);
    }

    static Set<CoordinateRange> getRangesForRow(Set<Sensor> sensors, long rowY) {
        Set<CoordinateRange> rangesForRow = new HashSet<>();
        System.out.println("Ranges for row: " + rowY);
        for (Sensor sensor : sensors) {
            CoordinateRange range = getKnownRangeForRowAndSensor(rowY, sensor);
            if (range != null) {
                rangesForRow.add(range);
                System.out.println(range);
            }
        }
        return rangesForRow;
    }

    static Set<CoordinateRange> consolidateRanges(Set<CoordinateRange> startingSet) {
        boolean stillConsolidating = true;
        Set<CoordinateRange> rangeSet = new HashSet<>(startingSet);
        //  compare all the ranges against each other until we've consolidated them all
        while(stillConsolidating) {
            List<CoordinateRange> rangeList = new ArrayList<>(rangeSet);
            List<CoordinateRange> rangesToRemove = new ArrayList<>();
            boolean didRemove = false;
            for (int i = 0; i < rangeList.size(); i++) {
                if (didRemove) break;
                for (int j = i + 1; j < rangeList.size(); j++) {
                    if (rangeList.get(i).isContiguous(rangeList.get(j))) {
                        System.out.println("ranges are contiguous: " + rangeList.get(i) + " and " + rangeList.get(j));
                        rangeList.get(i).addRange(rangeList.get(j));
                        rangesToRemove.add(rangeList.get(j));
                    }
                }
                if (rangesToRemove.size() > 0) {
                    for (CoordinateRange range : rangesToRemove) {
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

    static void printSectionOfMap(Set<Sensor> sensors, Loc topLeft, Loc bottomRight) {
        Sensor testSensor = new Sensor(new Loc(8, 7), new Loc(2, 10));
        for (long y = topLeft.y; y < bottomRight.y; y++) {
//            CoordinateRange sensorRange = getKnownRangeForRowAndSensor(y, testSensor);
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
                    CoordinateRange sensorRange = getKnownRangeForRowAndSensor(y, sensor);
                    if (sensorRange != null && sensorRange.containsCoord(x)) {
                        locOutput = "#";
                    }
                }
                System.out.print(locOutput);
            }
            System.out.print("\n");
        }
    }
}
