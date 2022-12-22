package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;

public class Day21 {

    public abstract interface MonkeyOperation {
        long operate(long val1, long val2);
    }

    public static class MonkeyAdd implements MonkeyOperation {
        @Override
        public long operate(long val1, long val2) {
            return val1 + val2;
        }
    }

    public static class MonkeySubtract implements MonkeyOperation {
        @Override
        public long operate(long val1, long val2) {
            return val1 - val2;
        }
    }

    public static class MonkeyMultiply implements MonkeyOperation {
        @Override
        public long operate(long val1, long val2) {
            return val1 * val2;
        }
    }

    public static class MonkeyDivide implements MonkeyOperation {
        @Override
        public long operate(long val1, long val2) {
            return val1 / val2;
        }
    }

    private static MonkeyOperation ADDER = new MonkeyAdd();
    private static MonkeyOperation SUBTRACTOR = new MonkeySubtract();
    private static MonkeyOperation MULTIPLIER = new MonkeyMultiply();
    private static MonkeyOperation DIVIDER = new MonkeyDivide();

    private static class Monkey {
        final public String name;
        Long value = null;
        final public MonkeyOperation operation;
        boolean isDone;
        Long val1 = null;
        Long val2 = null;
        final String monkey1;
        final String monkey2;

        Monkey(String name, Long value) {
            this.name = name;
            this.value = value;
            this.operation = null;
            this.isDone = true;
            this.monkey1 = null;
            this.monkey2 = null;
        }

        Monkey(String name, MonkeyOperation operation, String monkey1, String monkey2) {
            this.name = name;
            this.operation = operation;
            this.monkey1 = monkey1;
            this.monkey2 = monkey2;
            this.isDone = false;
        }

        @Override
        public String toString() {
            String operator = "?";
            if (operation != null) {
                if (operation.getClass().equals(MonkeyAdd.class)) {
                    operator = "+";
                } else if (operation.getClass().equals(MonkeySubtract.class)) {
                    operator = "-";
                } else if (operation.getClass().equals(MonkeyMultiply.class)) {
                    operator = "*";
                } else if (operation.getClass().equals(MonkeyDivide.class)) {
                    operator = "/";
                }
            }
            return "Monkey " + name + ": " + val1 + " " + operator + " " + val2;
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.name);
        }
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/Day21.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }

        List<Monkey> barrelOfMonkeys = new ArrayList<>();
        Map<String, Monkey> finishedMonkeys = new HashMap<>();
        Map<String, Monkey> unfinishedMonkeys = new HashMap<>();

        for (String line : data) {
            String monkeyName = line.substring(0, 4);
            String[] monkeyJob = line.substring(6).split(" ");
            Monkey newMonkey;
            if (monkeyJob.length == 1) {
                newMonkey = new Monkey(monkeyName, Long.valueOf(monkeyJob[0]));
                finishedMonkeys.put(monkeyName, newMonkey);
            } else {
                MonkeyOperation operation;
                switch(monkeyJob[1]) {
                    case "+":
                        operation = ADDER;
                        break;
                    case "-":
                        operation = SUBTRACTOR;
                        break;
                    case "*":
                        operation = MULTIPLIER;
                        break;
                    case "/":
                        operation = DIVIDER;
                        break;
                    default:
                        operation = ADDER;
                        System.out.println("Something's not right");
                }
                newMonkey = new Monkey(monkeyName, operation, monkeyJob[0], monkeyJob[2]);
                unfinishedMonkeys.put(monkeyName, newMonkey);
            }
            barrelOfMonkeys.add(newMonkey);
        }

        boolean stillLooking = true;
        while(stillLooking) {
            List<Monkey> monkeysToRemove = new ArrayList<>();
            for (Map.Entry<String, Monkey> monkeySet : unfinishedMonkeys.entrySet()) {
                Monkey monkeyToTest = monkeySet.getValue();
                if (monkeyToTest.val1 == null) {
                    if (finishedMonkeys.containsKey(monkeyToTest.monkey1)) {
                        monkeyToTest.val1 = finishedMonkeys.get(monkeyToTest.monkey1).value;
                    }
                }

                if (monkeyToTest.val2 == null) {
                    if (finishedMonkeys.containsKey(monkeySet.getValue().monkey2)) {
                        monkeyToTest.val2 = finishedMonkeys.get(monkeyToTest.monkey2).value;
                    }
                }

                if (monkeyToTest.val1 != null && monkeyToTest.val2 != null) {
                    monkeyToTest.value = monkeyToTest.operation.operate(monkeyToTest.val1, monkeyToTest.val2);
                    finishedMonkeys.put(monkeyToTest.name, monkeyToTest);
                    monkeysToRemove.add(monkeyToTest);
                }
            }

            for (Monkey monkeyToRemove : monkeysToRemove) {
                unfinishedMonkeys.remove(monkeyToRemove.name);
            }
            if (finishedMonkeys.containsKey("root")) {
                stillLooking = false;
            }
        }
        Monkey part1Monkey = finishedMonkeys.get("root");
        System.out.println("Part 1: " + part1Monkey.value);
        int jennifer = 9;
    }
}
