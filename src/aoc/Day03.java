package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day03 {
    public static void main(String[] args) throws Exception {
        List<String> data = new ArrayList<String>();
        try (final Scanner scanner = new Scanner(new File("data/Day03.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        }

        //  Part 1: item appears in both sides
        long total = 0;
        for (String line : data) {
            total += parseBackpack(line);
        }
        System.out.println("Total for part 1: " + total);

        //  Part 2: Item common in groups of 3
        long totalPart2 = 0;
        for (int i = 0; i < data.size(); i+=3) {
            if (i+2 > data.size()) {
                System.out.println("Something is wrong");
            }
            totalPart2 += compareThreeBackpacks(data.get(i), data.get(i+1), data.get(i+2));
        }
        System.out.println("Total for part 2: " + totalPart2);
    }

    private static long getPriorityVal(char x) {
        long val = x;
        if (val > 96) {
            return val - 96;
        }
        return val - 38;
    }

    private static long parseBackpack(String line) {
        String left = line.substring(0, (line.length()/2));
        String right = line.substring(line.length()/2);
        for (char x : left.toCharArray()) {
            if (right.indexOf(x) > -1) {
                return getPriorityVal(x);
            }
        }
        System.out.println("Shouldn't get here");
        return 0;
    }

    private static long compareThreeBackpacks(String pack1, String pack2, String pack3) {
        for (char x : pack1.toCharArray()) {
            if (pack2.indexOf(x) > -1 && pack3.indexOf(x) > -1) {
                return getPriorityVal(x);
            }
        }
        System.out.println("We shouldn't get here");
        return 0;
    }
}
