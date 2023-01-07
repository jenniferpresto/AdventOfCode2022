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

public class Day18 {
    private static int MIN_X = Integer.MAX_VALUE;
    private static int MIN_Y = Integer.MAX_VALUE;
    private static int MIN_Z = Integer.MAX_VALUE;
    private static int MAX_X = Integer.MIN_VALUE;
    private static int MAX_Y = Integer.MIN_VALUE;
    private static int MAX_Z = Integer.MIN_VALUE;

    private static int[] X_TRANSLATIONS = {0, 0, 0, 0, 1, -1};
    private static int[] Y_TRANSLATIONS = {0, 0, 1, -1, 0, 0};
    private static int[] Z_TRANSLATIONS = {1, -1, 0, 0, 0, 0};

    private static class Loc {
        final int x;
        final int y;
        final int z;
        Loc(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public String toString() {
            return "Loc: (" + x + "," + y + "," + z + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;
            Loc other = (Loc)o;
            return this.x == other.x && this.y == other.y && this.z == other.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.y, this.z);
        }
    }
    private static class Cube {
//        final int id;
//        final int x;
//        final int y;
//        final int z;
        final Loc loc;

        int numAdjacentCubes = 0;
        boolean isLava = true;
        boolean isAirPocket = false;
        boolean isTested = false;

        Cube(Loc loc) {
            this.loc = loc;
//            this.x = x;
//            this.y = y;
//            this.z = z;
        }

        public boolean isAdjacent(Cube other) {
            for (int i = 0; i < 6; i++) {
                if (other.loc.x == this.loc.x + X_TRANSLATIONS[i]
                && other.loc.y == this.loc.y + Y_TRANSLATIONS[i]
                && other.loc.z == this.loc.z + Z_TRANSLATIONS[i]) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String toString() {
            return "(" + this.loc.x + "," + this.loc.y + "," + this.loc.z + "), tested? " + isTested;
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;
            Cube other = (Cube)o;
            return this.loc.equals(other.loc)
                    && this.isLava == other.isLava
                    && this.isTested == other.isTested;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.loc, this.isLava, this.isTested);
        }
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("testData/Day18.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }

        List<Cube> lavaList = new ArrayList<>();
        Set<Cube> lavaSet = new HashSet<>();
        for (String line : data) {
            String[] coords = line.split(",");
            Loc loc = new Loc(Integer.valueOf(coords[0]), Integer.valueOf(coords[1]), Integer.valueOf(coords[2]));
            Cube cube = new Cube(loc);
            lavaList.add(cube);
            lavaSet.add(cube);

            if (loc.x < MIN_X) {
                MIN_X = loc.x;
            }
            if (loc.y < MIN_Y) {
                MIN_Y = loc.y;
            }
            if (loc.z < MIN_Z) {
                MIN_Z = loc.z;
            }
            if (loc.x > MAX_X) {
                MAX_X = loc.x;
            }
            if (loc.y > MAX_Y) {
                MAX_Y = loc.y;
            }
            if (loc.z > MAX_Z) {
                MAX_Z = loc.z;
            }
        }

        for (int i = 0; i < lavaList.size(); i++) {
            for (int j = i+1; j < lavaList.size(); j++) {
                if (lavaList.get(i).isAdjacent(lavaList.get(j))) {
                    lavaList.get(i).numAdjacentCubes++;
                    lavaList.get(j).numAdjacentCubes++;
                }
            }
        }

        //  create barriers one cube larger than the lava blob
        Map<Loc, Cube> airMap = new HashMap();
        int numDupes = 0;
        //  fill in the missing cubes within the barrier
        for (int x = MIN_X - 1; x < MAX_X + 2; x++) {
            for (int y = MIN_Y - 1; y < MAX_Y + 2; y++) {
                for (int z = MIN_Z - 1; z < MAX_Z + 2; z++) {
                    Loc loc = new Loc(x, y, z);
                    Cube cube = new Cube(loc);
                    if (!lavaSet.contains(cube)) {
                        cube.isLava = false;
                        airMap.put(loc, cube);
                    } else {
                        numDupes++;
                    }
                }
            }
        }
        System.out.println("Dupes: " + numDupes);


        int totalExposedSides = 0;
        for (Cube cube : lavaList) {
            totalExposedSides += (6 - cube.numAdjacentCubes);
        }
        System.out.println("Number exposed sides: " + totalExposedSides);

        Set<Cube> allOutsideAirCubes = new HashSet<>();

        Loc startLoc = new Loc(MIN_X - 1, MIN_Y - 1, MIN_Z - 1);
        Cube startCube = airMap.get(startLoc);
        if (startCube == null) {
            System.out.println("Something has gone wrong");
        }
        floodFill(startCube, airMap);

        Set<Cube> airCubesOutsideLavaBlob = new HashSet<>();
        for (Map.Entry<Loc, Cube> airCubeEntry : airMap.entrySet()) {
            if (airCubeEntry.getValue().isTested) {
                airCubesOutsideLavaBlob.add(airCubeEntry.getValue());
            }
        }
        System.out.println("Num air cubes in air pockets: " + (airMap.size() - airCubesOutsideLavaBlob.size()));
    }

    //  flood-fill to find all air cubes touches the outside of the lava blob
    static private void floodFill(Cube startCube, Map<Loc, Cube> airCubes) {
        //  if we're outside the barrier, stop
        if (!airCubes.containsKey(startCube.loc)) {
            System.out.println("We shouldn't hit this any more");
            return;
        }
        //  if we've already tested this cube, stop
        if (startCube.isTested) return;

        System.out.println("Testing: " + startCube);
        startCube.isTested = true;

        for (int i = 0; i < 6; i++) {
            Loc newStartLoc = new Loc(startCube.loc.x + X_TRANSLATIONS[i], startCube.loc.y + Y_TRANSLATIONS[i], startCube.loc.z + Z_TRANSLATIONS[i]);
            if (!airCubes.containsKey(newStartLoc)) continue;
            Cube newStart = airCubes.get(newStartLoc);

            floodFill(newStart, airCubes);
        }
    }
}
