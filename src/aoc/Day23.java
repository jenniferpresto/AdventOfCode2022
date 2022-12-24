package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Day23 {
    private static class Loc {
        int x;
        int y;

        Loc() {
            this.x = 0;
            this.y = 0;
        }
        Loc(int x, int y, String contents) {
            this.x = x;
            this.y = y;
        }

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
            if (this.x == other.x && this.y == other.y) return true;
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.y);
        }
    }

    private static class Elf {
        Loc currentLoc;
        Loc proposedLoc;
        boolean isProposalSuccessful = true;

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
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("testData/Day23.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }

        Map<Loc, Elf> regionMap = new HashMap<>();
        List<Elf> elves = new ArrayList<>();

        for (int y = 0; y < data.size(); y++) {
            for (int x = 0; x < data.get(y).length(); x++) {
                if(data.get(y).charAt(x) == '#') {
                    Loc loc = new Loc(x, y);
                    Elf elf = new Elf(loc);
                    regionMap.put(loc, elf);
                    elves.add(elf);
                }
            }
        }

        drawRegion(elves);
        calculateProposedLocations(elves, 0);
        moveElvesIfPossible(elves);
        drawRegion(elves);
        int jennifer = 9;
    }

    private static void calculateProposedLocations(List<Elf> elves, int proposedStartingDirection) {
        Set<Loc> currentLocations = new HashSet<>();
        List<Integer> north = new ArrayList<>(Arrays.asList(0, 1, 2));
        List<Integer> south = new ArrayList<>(Arrays.asList(4, 5, 6));
        List<Integer> west = new ArrayList<>(Arrays.asList(2, 3, 4));
        List<Integer> east = new ArrayList<>(Arrays.asList(6, 7, 0));
        List<List<Integer>> directions = new ArrayList<>(Arrays.asList(north, south, west, east));

        for (Elf elf : elves) {
            currentLocations.add(elf.currentLoc);
            elf.isProposalSuccessful = true;
            elf.proposedLoc = null;
        }

        for (Elf elf : elves) {
            List<Loc> adjacentElfLocs = elf.getAdjacentLocs();
            for (int i = proposedStartingDirection; i < proposedStartingDirection + 4; i++) {
                int idx = (proposedStartingDirection + i) % 4;
                //  check direction
                List<Integer> locIndices = directions.get(idx);
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
                    System.out.println("Found a direction for the elf");
                    break;
                }
            }
        }
    }

    private static void moveElvesIfPossible(List<Elf> elves) {
        //  compare all elves against each other
        //  if their proposed locations match, they can't move
        for (int i = 0; i < elves.size(); i++) {
            for (int j = i+1; j < elves.size(); j++) {
                if (elves.get(i).proposedLoc.equals(elves.get(j).proposedLoc)) {
                    elves.get(i).isProposalSuccessful = false;
                    elves.get(j).isProposalSuccessful = false;
                }
            }
        }

        for (Elf elf : elves) {
            if (elf.isProposalSuccessful) {
                elf.currentLoc = elf.proposedLoc;
            }
        }
    }

    private static void drawRegion(List<Elf> elves) {
        int topLeftX = Integer.MAX_VALUE;
        int topLeftY = Integer.MAX_VALUE;
        int bottomRightX = Integer.MIN_VALUE;
        int bottomRightY = Integer.MIN_VALUE;

        Set<Loc> currentElfLocations = new HashSet<>();

        //  get boundaries
        for (Elf elf : elves) {
            if (elf.currentLoc.x < topLeftX) {
                topLeftX = elf.currentLoc.x;
            }
            if (elf.currentLoc.y < topLeftY) {
                topLeftY = elf.currentLoc.y;
            }
            if (elf.currentLoc.x > bottomRightX) {
                bottomRightX = elf.currentLoc.x;
            }
            if (elf.currentLoc.y > bottomRightY) {
                bottomRightY = elf.currentLoc.y;
            }
            currentElfLocations.add(elf.currentLoc);
        }

        System.out.println("*******************");
        for (int y = topLeftY; y < bottomRightY + 1; y++) {
            for (int x = topLeftX; x < bottomRightX + 1; x++) {
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
