package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;

public class Day09 {
    public static final int NUM_KNOTS = 10; // 2 for part 1; 10 for part 2
    public static GridPoint topLeft = new GridPoint(0, 0);
    public static GridPoint bottomRight = new GridPoint(4, 4);
    public static class GridPoint {
        int x;
        int y;
        GridPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
        @Override
        public String toString() {
            return "(" + x + "," + y + ")";
        }
        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (o.getClass() != this.getClass()) {
                return false;
            }
            GridPoint other = (GridPoint)o;
            return this.x == other.x && this.y == other.y;
        }
        @Override
        public int hashCode() {
            return Objects.hash(this.x, this.y);
        }
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/Day09.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }

        Set<GridPoint> tailTrail = new HashSet<>();
        List<GridPoint> rope = new ArrayList<>();

        //  Initialize values:
        //  starting values are all arbitrary; chosen to print sample data most easily
        for (int i = 0; i < NUM_KNOTS; i++) {
            rope.add(new GridPoint(0, 4));
        }
        addPointToTailTrail(rope.get(rope.size()-1), tailTrail);

        //  Execute the instructions
        //  Each one moves the head one space and makes the rest of the rope follow it
        for (String line : data) {
            String[] instruction = line.split(" ");
            switch(instruction[0]) {
                case "U":
//                    System.out.println("Up " + instruction[1]);
                    for (int j = 0; j < Integer.valueOf(instruction[1]); j++) {
                        rope.get(0).y--;
                        moveAndRecord(rope, tailTrail);
                    }
                    break;
                case "D":
//                    System.out.println("Down " + instruction[1]);
                    for (int j = 0; j < Integer.valueOf(instruction[1]); j++) {
                        rope.get(0).y++;
                        moveAndRecord(rope, tailTrail);
                    }
                    break;
                case "L":
//                    System.out.println("Left " + instruction[1]);
                    for (int j = 0; j < Integer.valueOf(instruction[1]); j++) {
                        rope.get(0).x--;
                        moveAndRecord(rope, tailTrail);
                    }
                    break;
                case "R":
//                    System.out.println("Right " + instruction[1]);
                    for (int j = 0; j < Integer.valueOf(instruction[1]); j++) {
                        rope.get(0).x++;
                        moveAndRecord(rope, tailTrail);
                    }
                    break;
                default:
                    System.out.println("That's unexpected");
            }
            adjustSmallestAndLargest(rope.get(0), topLeft, bottomRight); // only used for printing grid
        }
        System.out.println("How many spots for the tail? " + tailTrail.size());
    }

    public static void moveAndRecord(List<GridPoint> rope, Set<GridPoint> tailTrail) {
        moveRope(rope);
        addPointToTailTrail(rope.get(rope.size()-1), tailTrail);
        printGridAndRope(rope);
    }

    /**
     * Head is already at new point when this is called
     * @param rope
     */
    public static void moveRope(List<GridPoint> rope) {
        for (int i = 1; i < rope.size(); i++) {
            moveNextKnot(rope.get(i-1), rope.get(i));
        }
    }

    public static void moveNextKnot(GridPoint current, GridPoint next) {
        if (Math.abs(current.x - next.x) < 2 && Math.abs(current.y - next.y) < 2) {
//            System.out.println("don't need to do anything, turning a corner or just starting out");
            return;
        }
        if (current.x == next.x) {
            moveKnotLeftOrRight(current, next);
            return;
        }
        if (current.y == next.y) {
            moveKnotUpOrDown(current, next);
            return;
        }
        //  diagonal move
        moveKnotLeftOrRight(current, next);
        moveKnotUpOrDown(current, next);
    }

    static public void moveKnotLeftOrRight(GridPoint head, GridPoint tail) {
        if (head.y > tail.y) {
            tail.y++;
        } else {
            tail.y--;
        }
    }

    static public void moveKnotUpOrDown(GridPoint head, GridPoint tail) {
        if (head.x > tail.x) {
            tail.x++;
        } else {
            tail.x--;
        }
    }

    static public void addPointToTailTrail(GridPoint tail, Set<GridPoint> tailTrail) {
        if (!tailTrail.contains(tail)) {
            GridPoint tailCopy = new GridPoint(tail.x, tail.y);
            tailTrail.add(tailCopy);
        }
    }

    //  just for debugging/seeing
    public static void adjustSmallestAndLargest(GridPoint head, GridPoint topLeft, GridPoint bottomRight) {
        if (head.x < topLeft.x) {
            topLeft.x = head.x;
        }
        if (head.y < topLeft.y) {
            topLeft.y = head.y;
        }
        if (head.x > bottomRight.x) {
            bottomRight.x = head.x;
        }
        if (head.y > bottomRight.y) {
            bottomRight.y = head.y;
        }
    }

    //  just for debugging/seeing
    public static void printGridAndRope(List<GridPoint> rope) {
        for (int i = topLeft.x; i < bottomRight.x + 1; i++)  {
            System.out.print("*");
        }
        System.out.println("");
        for (int y = topLeft.y; y < bottomRight.y + 1; y++) {

            for (int x = topLeft.x; x < bottomRight.x + 1; x++) {
                if (x == rope.get(0).x && y == rope.get(0).y) {
                    System.out.print("H");
                } else {
                    String pointString = ".";
                    for (int i = rope.size()-1; i > 0; i-- ) {
                        if (x == rope.get(i).x && y == rope.get(i).y) {
                            if (NUM_KNOTS == 2) {
                                pointString = "T";
                            } else {
                                pointString = String.valueOf(i);
                            }
                        }
                    }
                    System.out.print(pointString);
                }
            }
            System.out.println("");
        }
        for (int i = topLeft.x; i < bottomRight.x + 1; i++)  {
            System.out.print("*");
        }
        System.out.println("");
    }
}
