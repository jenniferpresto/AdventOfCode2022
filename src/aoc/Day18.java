package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Day18 {
    private static class Cube {
        final int x;
        final int y;
        final int z;

        int numAdjacentCubes = 0;

        Cube(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public boolean isAdjacent(Cube other) {
            if (this.x == other.x && this.y == other.y) {
                return this.z == other.z + 1 || this.z == other.z - 1;
            }
            if (this.x == other.x && this.z == other.z) {
                return this.y == other.y + 1 || this.y == other.y - 1;
            }
            if (this.y == other.y && this.z == other.z) {
                return this.x == other.x + 1 || this.x == other.x - 1;
            }
            return false;
        }

        @Override
        public String toString() {
            return "(" + this.x + "," + this.y + "," + this.z + ")";
        }
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;
            Cube other = (Cube)o;
            return this.x == other.x && this.y == other.y && this.z == other.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.y, this.z);
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

        List<Cube> cubeList = new ArrayList<>();
        for (String line : data) {
            String[] coords = line.split(",");
            cubeList.add(new Cube(Integer.valueOf(coords[0]), Integer.valueOf(coords[1]), Integer.valueOf(coords[2])));

        }

        for (int i = 0; i < cubeList.size(); i++) {
            for (int j = i+1; j < cubeList.size(); j++) {
                if (cubeList.get(i).isAdjacent(cubeList.get(j))) {
                    cubeList.get(i).numAdjacentCubes++;
                    cubeList.get(j).numAdjacentCubes++;
                }
            }
        }

        int totalExposedSides = 0;
        for (Cube cube : cubeList) {
            totalExposedSides += (6 - cube.numAdjacentCubes);
        }
        System.out.println("Number exposed sides: " + totalExposedSides);

    }
}
