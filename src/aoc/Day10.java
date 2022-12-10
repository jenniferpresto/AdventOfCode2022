package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day10 {
    private static class IntWrapper {
        int value = 0;

        IntWrapper() {}
        IntWrapper(int i) {
            this.value = i;
        }

        public void addValue(int s) {
            this.value += s;
        }

        public void increment() {
            this.value++;
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

        IntWrapper cycleNum = new IntWrapper();
        IntWrapper signalStrength = new IntWrapper();
        IntWrapper spritePos = new IntWrapper(1);
        for (String line : data) {
            String[] instructions = line.split(" ");
            if (instructions[0].startsWith("addx")) {
                executeCycle(cycleNum, spritePos, signalStrength);
                executeCycle(cycleNum, spritePos, signalStrength);
                spritePos.addValue(Integer.valueOf(instructions[1]));
            } else if (instructions[0].startsWith("noop")) {
                executeCycle(cycleNum, spritePos, signalStrength);
            }
        }
        System.out.println("");
        System.out.println("Total signal strength is " + signalStrength.value);

    }

    private static void executeCycle(IntWrapper cycleNum, IntWrapper spritePos, IntWrapper signalStrength) {
        cycleNum.increment();
        drawCycle(cycleNum, spritePos);
        calculateStrength(cycleNum, spritePos, signalStrength);
    }

    private static void calculateStrength(IntWrapper cycle, IntWrapper spritePos, IntWrapper s) {
        if ((cycle.value + 20) % 40 == 0) {
            s.addValue((cycle.value * spritePos.value));
        }
    }

    private static void drawCycle(IntWrapper cycleNum, IntWrapper spritePos) {
        int drawPos = (cycleNum.value-1) % 40;
        if (spritePos.value >= drawPos - 1 && spritePos.value <= drawPos + 1) {
            System.out.print("#");
        } else {
            System.out.print(".");
        }
        if (drawPos == 39) {
            System.out.print("\n");
        }
    }
}
