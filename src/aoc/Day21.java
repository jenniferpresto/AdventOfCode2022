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
        final String monkey1Name;
        final String monkey2Name;

        Monkey monkey1 = null;
        Monkey monkey2 = null;

        Monkey(String name, Long value) {
            this.name = name;
            this.value = value;
            this.operation = null;
            this.isDone = true;
            this.monkey1Name = null;
            this.monkey2Name = null;
        }

        Monkey(String name, MonkeyOperation operation, String monkey1, String monkey2) {
            this.name = name;
            this.operation = operation;
            this.monkey1Name = monkey1;
            this.monkey2Name = monkey2;
            this.isDone = false;
        }

        public long getMonkeyValue() {
            if (name.equals("humn")) {
                System.out.println("Human!");
            }
            if (value != null) { return value; }
            return(operation.operate(monkey1.getMonkeyValue(), monkey2.getMonkeyValue()));
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
        try (final Scanner scanner = new Scanner(new File("testData/Day21.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }

//        List<Monkey> barrelOfMonkeysPart1 = new ArrayList<>();
//        Map<String, Monkey> finishedMonkeys = new HashMap<>();
//        Map<String, Monkey> unfinishedMonkeys = new HashMap<>();
//
//        for (String line : data) {
//            String monkeyName = line.substring(0, 4);
//            String[] monkeyJob = line.substring(6).split(" ");
//            Monkey newMonkey;
//            if (monkeyJob.length == 1) {
//                newMonkey = new Monkey(monkeyName, Long.valueOf(monkeyJob[0]));
//                finishedMonkeys.put(monkeyName, newMonkey);
//            } else {
//                MonkeyOperation operation;
//                switch(monkeyJob[1]) {
//                    case "+":
//                        operation = ADDER;
//                        break;
//                    case "-":
//                        operation = SUBTRACTOR;
//                        break;
//                    case "*":
//                        operation = MULTIPLIER;
//                        break;
//                    case "/":
//                        operation = DIVIDER;
//                        break;
//                    default:
//                        operation = ADDER;
//                        System.out.println("Something's not right");
//                }
//                newMonkey = new Monkey(monkeyName, operation, monkeyJob[0], monkeyJob[2]);
//                unfinishedMonkeys.put(monkeyName, newMonkey);
//            }
//            barrelOfMonkeysPart1.add(newMonkey);
//        }
//
//        boolean stillLooking = true;
//        while(stillLooking) {
//            List<Monkey> monkeysToRemove = new ArrayList<>();
//            for (Map.Entry<String, Monkey> monkeySet : unfinishedMonkeys.entrySet()) {
//                Monkey monkeyToTest = monkeySet.getValue();
//                if (monkeyToTest.val1 == null) {
//                    if (finishedMonkeys.containsKey(monkeyToTest.monkey1Name)) {
//                        monkeyToTest.val1 = finishedMonkeys.get(monkeyToTest.monkey1Name).value;
//                    }
//                }
//
//                if (monkeyToTest.val2 == null) {
//                    if (finishedMonkeys.containsKey(monkeySet.getValue().monkey2Name)) {
//                        monkeyToTest.val2 = finishedMonkeys.get(monkeyToTest.monkey2Name).value;
//                    }
//                }
//
//                if (monkeyToTest.val1 != null && monkeyToTest.val2 != null) {
//                    monkeyToTest.value = monkeyToTest.operation.operate(monkeyToTest.val1, monkeyToTest.val2);
//                    finishedMonkeys.put(monkeyToTest.name, monkeyToTest);
//                    monkeysToRemove.add(monkeyToTest);
//                }
//            }
//
//            for (Monkey monkeyToRemove : monkeysToRemove) {
//                unfinishedMonkeys.remove(monkeyToRemove.name);
//            }
//            if (finishedMonkeys.containsKey("root")) {
//                stillLooking = false;
//            }
//        }
//        Monkey part1Monkey = finishedMonkeys.get("root");
//        System.out.println("Part 1: " + part1Monkey.value);

        //  Part 2
        //  Get a fresh list of monkeys
        List<Monkey> barrelOfMonkeysPart2 = getMonkeysFromData(data);
        Map<String, Monkey> part2MonkeyMap = new HashMap<>();
        for (Monkey monkey : barrelOfMonkeysPart2) {
            part2MonkeyMap.put(monkey.name, monkey);
        }

        for (Monkey monkey : barrelOfMonkeysPart2) {
            if (monkey.monkey1Name != null) {
                monkey.monkey1 = part2MonkeyMap.get(monkey.monkey1Name);
            }
            if (monkey.monkey2Name != null) {
                monkey.monkey2 = part2MonkeyMap.get(monkey.monkey2Name);
            }
        }
//        long rootValue = part2MonkeyMap.get("root").getMonkeyValue();
        //  oh, this is a lot better for part 1
//        System.out.println("Root value: " + rootValue);

        Monkey rootMonkey = part2MonkeyMap.get("root");
        System.out.println("Monkey 1: " + rootMonkey.monkey1.getMonkeyValue());
        System.out.println("Monkey 2: " + rootMonkey.monkey2.getMonkeyValue());

    }

    private static List<Monkey> getMonkeysFromData(List<String> data) {
        List<Monkey> barrelOfMonkeys = new ArrayList<>();
        for (String line : data) {
            String monkeyName = line.substring(0, 4);
            String[] monkeyJob = line.substring(6).split(" ");
            Monkey newMonkey;
            if (monkeyJob.length == 1) {
                newMonkey = new Monkey(monkeyName, Long.valueOf(monkeyJob[0]));
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
            }
            barrelOfMonkeys.add(newMonkey);
        }
        return barrelOfMonkeys;
    }
}
