package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.SortedMap;
import java.util.TreeMap;

public class Day09 {

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
    }
    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("testData/Day09.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }

//        //  create grids
//        SortedMap<Integer, SortedMap<Integer, Integer>> liveGrid = new TreeMap<>();
//        for(int x = 0; x < 5; x++) {
//            SortedMap<Integer, Integer> column = new TreeMap();
//            for (int y = 0; y < 3; y++) {
//
//            }
//        }

        GridPoint head = new GridPoint(0, 4);
        GridPoint tail = new GridPoint(0, 4);
        GridPoint topLeft = new GridPoint(0, 0);
        GridPoint bottomRight = new GridPoint(5, 4);

        System.out.println("Starting: " + head + ", " + tail);
        for (String line : data) {
            String[] instruction = line.split(" ");
            switch(instruction[0]) {
                case "U":
                    System.out.println("Up " + instruction[1]);
                    for (int j = 0; j < Integer.valueOf(instruction[1]); j++) {
                        head.y--;
                        moveTail(head, tail);
//                        System.out.println("head: " + head + ", tail: " + tail);
                        printCurrentGrid(topLeft, bottomRight, head, tail);
                    }
                    break;
                case "D":
                    System.out.println("Down " + instruction[1]);
                    for (int j = 0; j < Integer.valueOf(instruction[1]); j++) {
                        head.y++;
                        moveTail(head, tail);
                        printCurrentGrid(topLeft, bottomRight, head, tail);
//                        System.out.println("head: " + head + ", tail: " + tail);
                    }
                    break;
                case "L":
                    System.out.println("Left " + instruction[1]);
                    for (int j = 0; j < Integer.valueOf(instruction[1]); j++) {
                        head.x--;
                        moveTail(head, tail);
//                        System.out.println("head: " + head + ", tail: " + tail);
                        printCurrentGrid(topLeft, bottomRight, head, tail);
                    }
                    break;
                case "R":
                    System.out.println("Right " + instruction[1]);
                    for (int j = 0; j < Integer.valueOf(instruction[1]); j++) {
                        head.x++;
                        moveTail(head, tail);
//                        System.out.println("head: " + head + ", tail: " + tail);
                        printCurrentGrid(topLeft, bottomRight, head, tail);
                    }
                    break;
                default:
                    System.out.println("That's unexpected");
            }
            adjustSmallestAndLargest(head, topLeft, bottomRight);
        }
        System.out.println("Top left: " + topLeft + ", bottom right: " + bottomRight);
    }

    public static void moveTail(GridPoint head, GridPoint tail) {
        if (Math.abs(head.x - tail.x) < 2 && Math.abs(head.y - tail.y) < 2) {
            System.out.println("don't need to do anything, turning a corner or just starting out");
            return;
        }
        if (head.x == tail.x) {
            moveTailLeftOrRight(head, tail);
            return;
//            if (head.y > tail.y) {
//                tail.y++;
//            } else {
//                tail.y--;
//            }
        }
        if (head.y == tail.y) {
            moveTailUpOrDown(head, tail);
            return;
//            if (head.x > tail.x) {
//                tail.x++;
//            } else {
//                tail.x--;
//            }
        }
        //  diagonal move
        moveTailLeftOrRight(head, tail);
        moveTailUpOrDown(head, tail);
//            tail.y += (head.y - tail.y);

    }

    static public void moveTailLeftOrRight(GridPoint head, GridPoint tail) {
        if (head.y > tail.y) {
            tail.y++;
        } else {
            tail.y--;
        }
    }

    static public void moveTailUpOrDown(GridPoint head, GridPoint tail) {
        if (head.x > tail.x) {
            tail.x++;
        } else {
            tail.x--;
        }
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
}
