package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

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
        final Loc loc;

        int numAdjacentCubes = 0;

        Cube(Loc loc) {
            this.loc = loc;
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

        public List<Loc> getAdjacentLocations() {
            List<Loc> adjacentLocs = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                Loc adjLoc = new Loc(this.loc.x + X_TRANSLATIONS[i], this.loc.y + Y_TRANSLATIONS[i], this.loc.z + Z_TRANSLATIONS[i]);
                adjacentLocs.add(adjLoc);
            }
            return adjacentLocs;
        }

        @Override
        public String toString() {
            return "(" + this.loc.x + "," + this.loc.y + "," + this.loc.z + ")";
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;
            Cube other = (Cube)o;
            return this.loc.equals(other.loc);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.loc);
        }
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/Day18.txt"))) {
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
            Loc loc = new Loc(Integer.parseInt(coords[0]), Integer.parseInt(coords[1]), Integer.parseInt(coords[2]));
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

        //  Part one: test for adjacent cubes
        for (int i = 0; i < lavaList.size(); i++) {
            for (int j = i+1; j < lavaList.size(); j++) {
                if (lavaList.get(i).isAdjacent(lavaList.get(j))) {
                    lavaList.get(i).numAdjacentCubes++;
                    lavaList.get(j).numAdjacentCubes++;
                }
            }
        }

        int totalExposedSides = 0;
        for (Cube cube : lavaList) {
            totalExposedSides += (6 - cube.numAdjacentCubes);
        }
        System.out.println("Part 1: Number exposed sides: " + totalExposedSides);

        //  Part two: test air pockets
        //  create a larger blob completely surrounding lava blob to fill with air

        Date startTime = new Date(); // just curious

        Map<Loc, Cube> allAirCubes = new HashMap<>();
        //  fill in the missing cubes within the larger cube
        Loc firstLoc = new Loc(MIN_X - 1, MIN_Y - 1, MIN_Z - 1);
        for (int x = firstLoc.x; x < MAX_X + 2; x++) {
            for (int y = firstLoc.y; y < MAX_Y + 2; y++) {
                for (int z = firstLoc.z; z < MAX_Z + 2; z++) {
                    Loc loc = new Loc(x, y, z);
                    Cube cube = new Cube(loc);
                    //  if it's not lava, it's air
                    if (!lavaSet.contains(cube)) {
                        allAirCubes.put(loc, cube);
                    }
                }
            }
        }

        Cube startCube = allAirCubes.get(firstLoc);
        Set<Cube> outsideAirCubes = new HashSet<>();
        Set<Cube> untestedOutsideAirCubes = new HashSet<>();
        untestedOutsideAirCubes.add(startCube);

        while(!untestedOutsideAirCubes.isEmpty()) {
            Cube testCube = untestedOutsideAirCubes.stream().findFirst().get();
            untestedOutsideAirCubes.remove(testCube);
            outsideAirCubes.add(testCube);

            for (Loc loc : testCube.getAdjacentLocations()) {
                if (!allAirCubes.containsKey(loc)) continue;
                if (outsideAirCubes.contains(allAirCubes.get(loc))) continue;
                Cube nextCube = allAirCubes.get(loc);
                untestedOutsideAirCubes.add(nextCube);
            }
        }

        Set<Loc> locOfOutsideAir = outsideAirCubes.stream().map(c -> c.loc).collect(Collectors.toSet());
        System.out.println("Num air cubes in air pockets: " + (allAirCubes.size() - locOfOutsideAir.size()));

        int totalOutsideSurfaceArea = 0;
        for (Cube lavaCube : lavaList) {
            for (Loc adjLoc : lavaCube.getAdjacentLocations()) {
                if (locOfOutsideAir.contains(adjLoc)) {
                    totalOutsideSurfaceArea++;
                }
            }
        }
        System.out.println("Outside surface area: " + totalOutsideSurfaceArea);
        Date endTime = new Date();
        long elapsed = endTime.getTime() - startTime.getTime();
        System.out.println("Elapsed time for part 2: " + elapsed);
    }
}
