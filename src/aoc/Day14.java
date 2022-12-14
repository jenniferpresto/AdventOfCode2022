package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Day14 {

    private static class Loc {
        int x;
        int y;
        String contents = "";

        Loc(int x, int y, String contents) {
            this.x = x;
            this.y = y;
            this.contents = contents;
        }

        Loc(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public String toString() {
            return "(" + this.x + "," + this.y + "): " + this.contents;
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

//        @Override
//        public int compare(Loc o1, Loc o2) {
//            if (o1.x != o2.x) {
//                return o1.x - o2.x;
//            } else {
//                return o1.y - o2.y;
//            }
//        }
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/Day14.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }

        Set<Loc> cavePt1 = new HashSet<>();

        //  create map
        int largestY = 0;
        //  make the cave, keep track of bottom point
        for (String line : data) {
            System.out.println("Hello");
            String[] rockDesc = line.split(" -> ");

            for (int i = 0; i < rockDesc.length - 1; i++) {
                System.out.println("Pair: " + rockDesc[i] + " to " + rockDesc[i+1]);
                String[] coordsStart = rockDesc[i].split(",");
                String[] coordsEnd = rockDesc[i+1].split(",");
                int startX = Integer.valueOf(coordsStart[0]);
                int startY = Integer.valueOf(coordsStart[1]);
                int endX = Integer.valueOf(coordsEnd[0]);
                int endY = Integer.valueOf(coordsEnd[1]);
                if (startY > largestY) {
                    largestY = startY;
                }
                if (endY > largestY) {
                    largestY = endY;
                }

                if (startX == endX) {
                    int smallerY = Math.min(startY, endY);
                    int biggerY = Math.max(startY, endY);
                    for (int j = smallerY; j <= biggerY; j++) {
                        Loc rock = new Loc(startX, j, "#");
                        cavePt1.add(rock);
                    }
                } else if (startY == endY) {
                    int smallerX = Math.min(startX, endX);
                    int biggerX = Math.max(startX, endX);
                    for (int j = smallerX; j <= biggerX; j++) {
                        Loc rock = new Loc(j, startY, "#");
                        cavePt1.add(rock);
                    }
                }
            }
        }
        Set<Loc> cavePt2 = new HashSet<>(cavePt1);
//        int part1 = dropSandIntoCavePart1(cavePt1, largestY);
        int part2 = dropSandIntoCavePart2(cavePt2, largestY + 2);
//        System.out.println("Part 1: " + part1 + " grains of sand" );
        System.out.println("Part 2: " + part2 + " grains of sand" );


    }

    static int dropSandIntoCavePart2(Set<Loc> cave, int largestY) {
        boolean caveIsFilled = false;
        int numSand = 0;
        while (!caveIsFilled) {
            boolean grainStillFalling = true;
            Loc sand = new Loc(500, 0, "o");
            Loc originalSand = new Loc(500, 0);


            while(grainStillFalling) {
                Loc down = new Loc(sand.x, sand.y + 1);
                Loc downLeft = new Loc(sand.x - 1, sand.y + 1);
                Loc downRight = new Loc(sand.x + 1, sand.y + 1);
                if (cave.contains(originalSand)) {
                     caveIsFilled = true;
                     grainStillFalling = false;
                    System.out.println("We've filled the cave");
                }
                //  build out the infinite floor
                if (sand.y == largestY - 1) {
                    System.out.println("Hitting the floor with grain: " + sand);
                    Loc floorLeft = new Loc(sand.x - 1, sand.y + 1, "#");
                    Loc floorDown = new Loc(sand.x, sand.y + 1, "#");
                    Loc floorRight = new Loc(sand.x + 1, sand.y + 1, "#");

                    if (!cave.contains(floorLeft)) {
                        cave.add(floorLeft);
                    }
                    if (!cave.contains(floorDown)) {
                        cave.add(floorDown);
                    }
                    if (!cave.contains(floorRight)) {
                        cave.add(floorRight);
                    }
                    if (!cave.contains(sand)) {
                        cave.add(sand);
                        numSand++;
                        grainStillFalling = false;
//                        continue;
                    } else {
                        System.out.println("What's up with this sand");
                    }
                }
                if (!cave.contains(down)) {
                    sand.y++;
                    continue;
                } else if (!cave.contains(downLeft)) {
                    sand.y++;
                    sand.x--;
                    continue;
                } else if (!cave.contains(downRight)) {
                    sand.y++;
                    sand.x++;
                } else if (!cave.contains(sand)) {
                    cave.add(sand);
                    System.out.println("Adding sand at " + sand);
                    numSand++;
                    grainStillFalling = false;
                }
            }
        }
        return numSand;
    }

    static int dropSandIntoCavePart1(Set<Loc> cave, int largestY) {
        boolean inTheAbyss = false;
        int numSand = 0;
        while (!inTheAbyss) {
            boolean grainStillFalling = true;
            Loc sand = new Loc(500, 0, "o");
            Loc lastSand = new Loc(495, 8);
            Loc originalSand = new Loc(500, 0);

            while(grainStillFalling) {
                if (sand.equals(lastSand)) {
                    int jennifer = 9;
                }
                Loc down = new Loc(sand.x, sand.y + 1);
                Loc downLeft = new Loc(sand.x - 1, sand.y + 1);
                Loc downRight = new Loc(sand.x + 1, sand.y + 1);
                if (sand.y > largestY) {
                    inTheAbyss = true;
                    System.out.println("We've filled the cave");
                    grainStillFalling = false;
                }

                if (!cave.contains(down)) {
                    sand.y++;
                    continue;
                } else if (!cave.contains(downLeft)) {
                    sand.y++;
                    sand.x--;
                    continue;
                } else if (!cave.contains(downRight)) {
                    sand.y++;
                    sand.x++;
                } else if (!cave.contains(sand)) {
                    cave.add(sand);
                    System.out.println("Adding sand at " + sand);
                    numSand++;
                    grainStillFalling = false;
                }
            }
        }
        return numSand;
    }
}
