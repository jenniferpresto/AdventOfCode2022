package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class Day09 {
    public static final int NUM_KNOTS = 10; // 2 for part 1; 10 for part 2
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

//        Set<GridPoint> tailTrail = new HashSet<>();
//        Map<Integer, Set<Integer>> tailTrail = new HashMap<>();
        List<GridPoint> rope = new ArrayList<>();

        List<GridPoint> tailTrail = new ArrayList<>();

        GridPoint head = new GridPoint(0, 4);
        GridPoint tail = new GridPoint(0, 4);
        GridPoint topLeft = new GridPoint(0, 0);
        GridPoint bottomRight = new GridPoint(5, 4);
        for (int i = 0; i < NUM_KNOTS; i++) {
            rope.add(new GridPoint(0, 4));
        }
        addPointToTailTrail(tail, tailTrail);

        System.out.println("Starting: " + head + ", " + tail);
        for (String line : data) {
            String[] instruction = line.split(" ");
            switch(instruction[0]) {
                case "U":
                    System.out.println("Up " + instruction[1]);
                    for (int j = 0; j < Integer.valueOf(instruction[1]); j++) {
//                        head.y--;
//                        moveTail(head, tail, tailTrail);
//                        System.out.println("head: " + head + ", tail: " + tail);
//                        printCurrentGrid(topLeft, bottomRight, head, tail);
                        rope.get(0).y--;
                        moveRope(rope, tailTrail);
                        addPointToTailTrail(rope.get(rope.size()-1), tailTrail);
//                        printGridWithLongerRope(topLeft, bottomRight, rope);
                    }
                    break;
                case "D":
                    System.out.println("Down " + instruction[1]);
                    for (int j = 0; j < Integer.valueOf(instruction[1]); j++) {
//                        head.y++;
//                        moveTail(head, tail, tailTrail);
//                        printCurrentGrid(topLeft, bottomRight, head, tail);
//                        System.out.println("head: " + head + ", tail: " + tail);
                        rope.get(0).y++;
                        moveRope(rope, tailTrail);
                        addPointToTailTrail(rope.get(rope.size()-1), tailTrail);
//                        printGridWithLongerRope(topLeft, bottomRight, rope);
                    }
                    break;
                case "L":
                    System.out.println("Left " + instruction[1]);
                    for (int j = 0; j < Integer.valueOf(instruction[1]); j++) {
//                        head.x--;
//                        moveTail(head, tail, tailTrail);
//                        System.out.println("head: " + head + ", tail: " + tail);
//                        printCurrentGrid(topLeft, bottomRight, head, tail);
                        rope.get(0).x--;
                        moveRope(rope, tailTrail);
                        addPointToTailTrail(rope.get(rope.size()-1), tailTrail);
//                        printGridWithLongerRope(topLeft, bottomRight, rope);
                    }
                    break;
                case "R":
                    System.out.println("Right " + instruction[1]);
                    for (int j = 0; j < Integer.valueOf(instruction[1]); j++) {
//                        head.x++;
//                        moveTail(head, tail, tailTrail);
//                        System.out.println("head: " + head + ", tail: " + tail);
//                        printCurrentGrid(topLeft, bottomRight, head, tail);
                        rope.get(0).x++;
                        moveRope(rope, tailTrail);
                        addPointToTailTrail(rope.get(rope.size()-1), tailTrail);
//                        printGridWithLongerRope(topLeft, bottomRight, rope);
                    }
                    break;
                default:
                    System.out.println("That's unexpected");
            }
            adjustSmallestAndLargest(head, topLeft, bottomRight);
        }
        System.out.println("Top left: " + topLeft + ", bottom right: " + bottomRight);
        int numSpacesTailVisited = 0;
//        Iterator ttIterator = tailTrail.entrySet().iterator();
//        while(ttIterator.hasNext()) {
//            Map<Integer, Set<Integer>> column = (Map<Integer>)ttIterator.next();
//            numSpacesTailVisited += column.size();
//        }

//        for(Set<Integer> col : tailTrail.values()) {
//            numSpacesTailVisited += col.size();
//        }

        System.out.println("How many spots for the tail? " + tailTrail.size());
    }

    /**
     * Head is already at new point when this is called
     * @param rope
     * @param tailTrail
     */
    public static void moveRope(List<GridPoint> rope, List<GridPoint> tailTrail) {
        for (int i = 1; i < rope.size(); i++) {
            GridPoint head = rope.get(i-1);
            moveTail(head, rope.get(i), tailTrail);
        }
    }

    public static void moveTail(GridPoint head, GridPoint tail, List<GridPoint> tailTrail) {
        if (Math.abs(head.x - tail.x) < 2 && Math.abs(head.y - tail.y) < 2) {
//            System.out.println("don't need to do anything, turning a corner or just starting out");
            return;
        }
        if (head.x == tail.x) {
            moveTailLeftOrRight(head, tail, tailTrail);
//            addPointToTailTrail(tail, tailTrail);

            return;
//            if (head.y > tail.y) {
//                tail.y++;
//            } else {
//                tail.y--;
//            }
        }
        if (head.y == tail.y) {
            moveTailUpOrDown(head, tail, tailTrail);
//            addPointToTailTrail(tail, tailTrail);
            return;
//            if (head.x > tail.x) {
//                tail.x++;
//            } else {
//                tail.x--;
//            }
        }
        //  diagonal move
        moveTailLeftOrRight(head, tail, tailTrail);
        moveTailUpOrDown(head, tail, tailTrail);
//        addPointToTailTrail(tail, tailTrail);
//            tail.y += (head.y - tail.y);


    }

    static public void moveTailLeftOrRight(GridPoint head, GridPoint tail, List<GridPoint> tailTrail) {
        if (head.y > tail.y) {
            tail.y++;
        } else {
            tail.y--;
        }
//        addPointToTailTrail(tail, tailTrail);
    }

    static public void moveTailUpOrDown(GridPoint head, GridPoint tail, List<GridPoint> tailTrail) {
        if (head.x > tail.x) {
            tail.x++;
        } else {
            tail.x--;
        }
//        addPointToTailTrail(tail, tailTrail);
    }

    static public void addPointToTailTrail(GridPoint tail, List<GridPoint> tailTrail) {
        for (GridPoint point : tailTrail) {
            if (point.equals(tail)) {
                System.out.println("We already have this one: " + tail);
                return;
            }
        }
        GridPoint tailCopy = new GridPoint(tail.x, tail.y);
        tailTrail.add(tailCopy);
//        GridPoint newPoint = new GridPoint(tail.x, tail.y);
//        if (tailTrail.entrySet().contains(tail.x)) {
//            if (tailTrail.get(tail.x).contains(tail.y)) {
//                System.out.println("We already have this one" + tail);
//            } else {
//                tailTrail.get(tail.x).add(tail.y);
//            }
//        } else {
//            Set<Integer> newColumn = new HashSet<>();
//            newColumn.add(tail.y);
//            tailTrail.put(tail.x, newColumn);
//        }
//        if (tailTrail.contains(newPoint)) {
//            System.out.println("We already have this point");
//        } else {
//            tailTrail.add(newPoint);
//        }
    }

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

    public static void printCurrentGrid(GridPoint topLeft, GridPoint bottomRight, GridPoint head, GridPoint tail) {
        for (int i = topLeft.x; i < bottomRight.x + 1; i++)  {
            System.out.print("*");
        }
        System.out.println("");
        for (int y = topLeft.y; y < bottomRight.y + 1; y++) {

            for (int x = topLeft.x; x < bottomRight.x + 1; x++) {
                if (x == head.x && y == head.y) {
                    System.out.print("H");
                } else if (x == tail.x && y == tail.y) {
                    System.out.print("T");
                } else {
                    System.out.print(".");
                }
            }
            System.out.println("");
        }
        for (int i = topLeft.x; i < bottomRight.x + 1; i++)  {
            System.out.print("*");
        }
        System.out.println("");
    }

    public static void printGridWithLongerRope(GridPoint topLeft, GridPoint bottomRight, List<GridPoint> rope) {
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
                            pointString = String.valueOf(i);
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
