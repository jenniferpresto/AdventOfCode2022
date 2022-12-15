package aoc;

import java.io.File;
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
        try (final Scanner scanner = new Scanner(new File("testData/Day15.txt"))) {
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

        long lowestKnownX = Long.MAX_VALUE;
        long lowestKnownY = Long.MAX_VALUE;
        long highestKnownX = Long.MIN_VALUE;
        long highestKnownY = Long.MIN_VALUE;

        Map<Long, List<CoordinateRange>> knownRows = new HashMap<>();

        //  fill out known locations
        for (Sensor sensor : sensors) {
//            System.out.println("Testing sensor: " + sensor);
            Loc testLoc = new Loc(8, 7);
            if (sensor.pos.equals(testLoc)) {
                long manhattanDist = sensor.getManhattanDistanceToBeacon();
                long startingX = sensor.pos.x - manhattanDist;
                long endingX = sensor.pos.x + manhattanDist + 1;
                long startingY = sensor.pos.y - manhattanDist;
                long endingY = sensor.pos.y + manhattanDist + 1;
                if (startingX < lowestKnownX) {
                    lowestKnownX = startingX;
                }
                if (endingX > highestKnownX) {
                    highestKnownX = endingX;
                }
                if (startingY < lowestKnownY) {
                    lowestKnownY = startingY;
                }
                if (endingY > highestKnownY) {
                    highestKnownY = endingY;
                }

                //  brute force, pos by pos
//                for (long i = startingX; i < endingX; i++) {
//                    for (long j = startingY; j < endingY; j++) {
//                        Loc loc = new Loc(i, j);
//                        long dist = sensor.pos.getManhattanDistance(loc);
//                        if (!knownLocs.containsKey(loc)) {
//                            if (dist < sensor.getManhattanDistanceToBeacon() + 1) {
//                                knownLocs.put(loc, "#");
//                            }
//                        }
//                    }
//                }
            }
        }
//        printSectionOfMap(knownLocs, new Loc(-2, -2), new Loc(26, 23));
        printSectionOfMap(sensors, new Loc(-2, -2), new Loc(26, 23));

        long testRow = 10;
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

    static void getNonBeaconSpotsInRow(Set<Sensor> sensors, long rowY) {

    }

    static void printSectionOfMap(Set<Sensor> sensors, Loc topLeft, Loc bottomRight) {
        Sensor testSensor = new Sensor(new Loc(8, 7), new Loc(2, 10));
        for (long y = topLeft.y; y < bottomRight.y; y++) {
            CoordinateRange sensorRange = getKnownRangeForRowAndSensor(y, testSensor);
            int jennifer = 9;
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
//                    KnownRowRange sensorRange = getKnownRangeForRowAndSensor(y, sensor);
                    if (sensorRange != null && sensorRange.containsCoord(x)) {
                        locOutput = "#";
                    }
                }
                System.out.print(locOutput);
            }
            System.out.print("\n");
        }
    }

    static void printSectionOfMap(Map<Loc, String> areaMap, Loc topLeft, Loc bottomRight) {
        for (long y = topLeft.y; y < bottomRight.y; y++) {
            for (long x = topLeft.x; x < bottomRight.x; x++) {
                Loc testLoc = new Loc(x, y);
                if (areaMap.containsKey(testLoc)) {
                    System.out.print(areaMap.get(testLoc));
                } else {
                    System.out.print(".");
                }
            }
            System.out.print("\n");
        }
    }
}
