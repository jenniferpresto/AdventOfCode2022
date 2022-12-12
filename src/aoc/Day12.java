package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Day12 {
    private static class Loc {
        final int x;
        final int y;
        final char label;
        final int value;
        long shortestDistance = Long.MAX_VALUE;
        List<Loc> shortestPath; // unnecessary for challenge, but nice for debugging

        Loc(int x, int y, char label, char value) {
            this.x = x;
            this.y = y;
            this.label = label;
            this.value = value;
        }

        public String printLabel() {
            return String.valueOf(this.label);
        }

        @Override
        public String toString() {
            return "(" + this.x + "," + this.y + "): "
                    + String.valueOf(this.label)
                    + ", height " + this.value
                    + ", dist: " + this.shortestDistance;
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

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/Day12.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }

        final int width = data.get(0).length();
        final int height = data.size();
        final Loc[][] heightmap = new Loc[width][height];
        Loc originalStartLoc = null;
        Loc endLoc = null;

        //  initialize map
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                char label = data.get(y).charAt(x);
                if (label == 'S') {
                    heightmap[x][y] = new Loc(x, y, label, 'a');
                    originalStartLoc = heightmap[x][y];
                } else if (label == 'E') {
                    heightmap[x][y] = new Loc(x, y, label, 'z');
                    endLoc = heightmap[x][y];
                } else {
                    heightmap[x][y] = new Loc(x, y, label, label);
                }
            }
        }

        //  Part 1
        calculateShortestPathBetweenLocations(originalStartLoc, endLoc, heightmap);
        System.out.println("Part 1: Shortest dist to end location: " + endLoc.shortestDistance);

        //  Part 2
        long shortestDist = Long.MAX_VALUE;
        for (int y = 0; y < heightmap[0].length; y++) {
            for (int x = 0; x < heightmap.length; x++) {
                clearDistancesAndPathsInMap(heightmap);
                if (heightmap[x][y].label == 'a') {
                    long dist = calculateShortestPathBetweenLocations(heightmap[x][y], endLoc, heightmap);
                    if (dist < shortestDist) {
                        shortestDist = dist;
                    }
                }
            }
        }
        System.out.println("Part 2: Shortest distance from best starting point: " + shortestDist);
    }

    static void clearDistancesAndPathsInMap(Loc[][] grid) {
        for (int y = 0; y < grid[0].length; y++) {
            for (int x = 0; x < grid.length; x++) {
                grid[x][y].shortestDistance = Long.MAX_VALUE;
                grid[x][y].shortestPath = null;
            }
        }
    }

    static long calculateShortestPathBetweenLocations(final Loc startLoc, final Loc endLoc, final Loc[][] heightMap) {
        final Set<Loc> settledLocations = new HashSet<>();
        final Set<Loc> unsettledLocations = new HashSet<>();
        startLoc.shortestPath = new LinkedList<>();
        startLoc.shortestPath.add(startLoc);
        startLoc.shortestDistance = 0;
        unsettledLocations.add(startLoc);
        while(!unsettledLocations.isEmpty()) {
            final Loc currentLoc = getShortestDistanceLoc(unsettledLocations);
            unsettledLocations.remove(currentLoc);
            final Set<Loc> adjacentLocs = getAdjacentLocations(currentLoc, heightMap);
            for (Loc adjacentLoc : adjacentLocs) {
                if (!settledLocations.contains(adjacentLoc)) {
                    calculateDist(adjacentLoc, currentLoc);
                    unsettledLocations.add(adjacentLoc);
                }
            }
            settledLocations.add(currentLoc);
        }
        return endLoc.shortestDistance;
    }

    static void calculateDist(final Loc evaluationLoc, final Loc sourceLoc) {
        final long sourceDist = sourceLoc.shortestDistance;
        if (sourceDist + 1 < evaluationLoc.shortestDistance) {
            List<Loc> shortestPath = new LinkedList<>(sourceLoc.shortestPath);
            shortestPath.add(evaluationLoc);
            evaluationLoc.shortestDistance = sourceDist + 1;
            evaluationLoc.shortestPath = shortestPath;
        }
    }
    static Loc getShortestDistanceLoc(final Set<Loc> unsettledLocations) {
        Loc shortestDistLoc = null;
        long testedShortestDist = Long.MAX_VALUE;
        for (Loc loc : unsettledLocations) {
            if (loc.shortestDistance < testedShortestDist) {
                shortestDistLoc = loc;
                testedShortestDist = loc.shortestDistance;
            }
        }
        return shortestDistLoc;
    }

    static Set<Loc> getAdjacentLocations(final Loc loc, final Loc[][] fullMap) {
        final Set<Loc> adjacentSet = new HashSet<>();
        if (loc.x > 0 && fullMap[loc.x-1][loc.y].value - 1 <= loc.value) {
            adjacentSet.add(fullMap[loc.x-1][loc.y]);
        }
        if (loc.x < fullMap.length - 1 && fullMap[loc.x+1][loc.y].value - 1 <= loc.value) {
            adjacentSet.add(fullMap[loc.x+1][loc.y]);
        }
        if (loc.y > 0 && fullMap[loc.x][loc.y-1].value - 1 <= loc.value) {
            adjacentSet.add(fullMap[loc.x][loc.y-1]);
        }
        if (loc.y < fullMap[0].length - 1 && fullMap[loc.x][loc.y+1].value - 1  <= loc.value) {
            adjacentSet.add(fullMap[loc.x][loc.y+1]);
        }
        return adjacentSet;
    }

    static void printAllLoc(final Loc[][] map) {
        final int width = map.length;
        final int height = map[0].length;
        for (int i = 0; i < 9; i++) {
            System.out.print("#");
        }
        System.out.print("\n");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.println(map[x][y]);
            }
        }
        for (int i = 0; i < 9; i++) {
            System.out.print("#");
        }
        System.out.print("\n");
    }

    static void printMap(final Loc[][] map) {
        final int width = map.length;
        final int height = map[0].length;
        for (int i = 0; i < width; i++) {
            System.out.print("#");
        }
        System.out.print("\n");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(map[x][y].printLabel());
            }
            System.out.print("\n");
        }
        for (int i = 0; i < width; i++) {
            System.out.print("#");
        }
        System.out.print("\n");
    }
}
