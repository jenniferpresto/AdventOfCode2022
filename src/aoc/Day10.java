package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day10 {
    private static class IntWrapper {
        int strength = 0;

        public void addStrength(int s) {
            this.strength += s;
        }
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/Day10.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }

        int cycle = 0;
        int x = 1;
        IntWrapper signalStrength = new IntWrapper();
        for (String line : data) {
            String[] instructions = line.split(" ");
            if (instructions[0].startsWith("addx")) {
                cycle++;
                drawCycle(cycle, x);
                calculateStrength(cycle, x, signalStrength);
                cycle++;
                drawCycle(cycle, x);
                calculateStrength(cycle, x, signalStrength);
                x += Integer.valueOf(instructions[1]);
            } else if (instructions[0].startsWith("noop")) {
                cycle++;
                drawCycle(cycle, x);
                calculateStrength(cycle, x, signalStrength);
            }
        }
        System.out.println("Total signal strength is " + signalStrength.strength);

    }

    private static void calculateStrength(int cycle, int x, IntWrapper s) {
        if ((cycle + 20) % 40 == 0) {
            s.addStrength((cycle * x));
        }
    }

    private static void drawCycle(int cycle, int x) {
        int drawPos = (cycle-1) % 40;
        if (x >= drawPos - 1 && x <= drawPos + 1) {
            System.out.print("#");
        } else {
            System.out.print(".");
        }
        if (drawPos == 39) {
            System.out.print("\n");
        }
    }

}
