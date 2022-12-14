package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Day23 {
    public static int TOP_LEFT_X = Integer.MAX_VALUE;
    public static int TOP_LEFT_Y = Integer.MAX_VALUE;
    public static int BOTTOM_RIGHT_X = Integer.MIN_VALUE;
    public static int BOTTOM_RIGHT_Y = Integer.MIN_VALUE;

    public static List<Integer> north = new ArrayList<>(Arrays.asList(0, 1, 2));
    public static List<Integer> south = new ArrayList<>(Arrays.asList(4, 5, 6));
    public static List<Integer> west = new ArrayList<>(Arrays.asList(2, 3, 4));
    public static List<Integer> east = new ArrayList<>(Arrays.asList(6, 7, 0));
    public static List<List<Integer>> DIRECTIONS = new ArrayList<>(Arrays.asList(north, south, west, east));


    private static class Loc {
        int x;
        int y;

        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + this.x + "," + this.y + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;
            Loc other = (Loc)o;
            return this.x == other.x && this.y == other.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.y);
        }
    }

    private static class Elf {
        Loc currentLoc;
        Loc proposedLoc;
        boolean willMove = true;

        Elf(Loc loc) {
            this.currentLoc = loc;
        }

        List<Loc> getAdjacentLocs() {
            return new ArrayList<>(
                List.of(
                    new Loc(currentLoc.x + 1, currentLoc.y - 1), // northeast (0)
                    new Loc(currentLoc.x, currentLoc.y - 1), // north (1)
                    new Loc(currentLoc.x - 1, currentLoc.y - 1), // northwest (2)
                    new Loc(currentLoc.x - 1, currentLoc.y), // west (3)
                    new Loc(currentLoc.x - 1, currentLoc.y + 1), // southwest (4)
                    new Loc(currentLoc.x, currentLoc.y + 1), // south (5)
                    new Loc(currentLoc.x + 1, currentLoc.y + 1), // southeast (6)
                    new Loc(currentLoc.x + 1, currentLoc.y) // east (7)
                )
            );
        }
    }

    public static void main(String[] args) {
        Date start = new Date();
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/Day23.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }

        List<Elf> elves = new ArrayList<>();

        for (int y = 0; y < data.size(); y++) {
            for (int x = 0; x < data.get(y).length(); x++) {
                if(data.get(y).charAt(x) == '#') {
                    Loc loc = new Loc(x, y);
                    Elf elf = new Elf(loc);
                    elves.add(elf);
                }
            }
        }

        int startingDir = 0;
        for (int i = 0; i < 10; i++) {
            calculateProposedLocations(elves, startingDir);
            moveElvesIfPossible(elves);
            startingDir = (startingDir + 1) % 4;
        }
        calculateBoundaries(elves);
        int numEmptyLocs = 0;
        Set<Loc> occupiedLocs = new HashSet<>();
        for (Elf elf : elves) {
            occupiedLocs.add(elf.currentLoc);
        }
        for (int y = TOP_LEFT_Y; y < BOTTOM_RIGHT_Y + 1; y++) {
            for (int x = TOP_LEFT_X; x < BOTTOM_RIGHT_X + 1; x++) {
                Loc testLoc = new Loc(x, y);
                if (!occupiedLocs.contains(testLoc)) {
                    numEmptyLocs++;
                }
            }
        }
        System.out.println("Part 1: Num empty spots: " + numEmptyLocs);
        int numRounds = 10; // note, assumes will take at least 10 rounds
        boolean haveMoved = true;
        while(haveMoved) {
            calculateProposedLocations(elves, startingDir);
            haveMoved = moveElvesIfPossible(elves);
            startingDir = (startingDir + 1) % 4;
            numRounds++;
        }
        System.out.println("Part 2: Finished on round: " + numRounds);
        Date end = new Date();
        System.out.println("Elapsed time = " + (end.getTime() - start.getTime()));
    }

    private static void calculateProposedLocations(List<Elf> elves, int proposedStartingDirection) {
        Set<Loc> currentLocations = new HashSet<>();

        for (Elf elf : elves) {
            currentLocations.add(elf.currentLoc);
            elf.willMove = true;
            elf.proposedLoc = null;
        }

        for (Elf elf : elves) {
            List<Loc> adjacentElfLocs = elf.getAdjacentLocs();
            //  see if the elf has only empty spaces around
            boolean noNeedToMove = true;
            for (Loc loc : adjacentElfLocs) {
                if (currentLocations.contains(loc)) {
                    noNeedToMove = false;
                    break;
                }
            }

            if (noNeedToMove) {
                elf.proposedLoc = elf.currentLoc;
                elf.willMove = false;
                continue;
            }

            for (int i = 0; i < 4; i++) {
                int idx = (proposedStartingDirection + i) % 4;
                //  check direction
                List<Integer> locIndices = DIRECTIONS.get(idx);
                boolean directionIsClear = true;
                for (int j = 0; j < locIndices.size(); j++) {
                    if(currentLocations.contains(adjacentElfLocs.get(locIndices.get(j)))) {
                        directionIsClear = false;
                    }
                }
                //  if the direction is clear, propose a new direction
                //  the new direction is always the middle index in the set of three
                if (directionIsClear) {
                    elf.proposedLoc = adjacentElfLocs.get(locIndices.get(1));
                    break;
                }
            }

            if (elf.proposedLoc == null) {
                elf.proposedLoc = elf.currentLoc;
            }
        }
    }

    private static boolean moveElvesIfPossible(List<Elf> elves) {
        boolean elvesDidMove = false;
        Set<Loc> proposedLocations = new HashSet<>();
        Set<Loc> collidingLocations = new HashSet<>();
        for (Elf elf : elves) {
            if (proposedLocations.contains(elf.proposedLoc)) {
                collidingLocations.add(elf.proposedLoc);
            } else {
                proposedLocations.add(elf.proposedLoc);
            }
        }

        for (Elf elf : elves) {
            if (collidingLocations.contains(elf.proposedLoc)) {
                elf.willMove = false;
            }

            if (elf.willMove) {
                elf.currentLoc = elf.proposedLoc;
                elvesDidMove = true;
            }
        }
        return elvesDidMove;
    }

    private static void calculateBoundaries(List<Elf> elves) {
        //  get boundaries
        for (Elf elf : elves) {
            if (elf.currentLoc.x < TOP_LEFT_X) {
                TOP_LEFT_X = elf.currentLoc.x;
            }
            if (elf.currentLoc.y < TOP_LEFT_Y) {
                TOP_LEFT_Y = elf.currentLoc.y;
            }
            if (elf.currentLoc.x > BOTTOM_RIGHT_X) {
                BOTTOM_RIGHT_X = elf.currentLoc.x;
            }
            if (elf.currentLoc.y > BOTTOM_RIGHT_Y) {
                BOTTOM_RIGHT_Y = elf.currentLoc.y;
            }
        }
    }

    private static void drawRegion(List<Elf> elves) {
        Set<Loc> currentElfLocations = new HashSet<>();
        calculateBoundaries(elves);
        for (Elf elf : elves) {
            currentElfLocations.add(elf.currentLoc);
        }

        System.out.println("*******************");
        for (int y = TOP_LEFT_Y; y < BOTTOM_RIGHT_Y + 1; y++) {
            for (int x = TOP_LEFT_X; x < BOTTOM_RIGHT_X + 1; x++) {
                Loc test = new Loc(x, y);
                if (currentElfLocations.contains(test)) {
                    System.out.print("#");
                }
                else {
                    System.out.print(".");
                }
            }
            System.out.print("\n");
        }
        System.out.println("******************");
    }
}
