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
//        Long val1 = null;
//        Long val2 = null;
        final String monkey1Name;
        final String monkey2Name;

        long desiredMonkeyValue;

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

        public boolean includesHumanInValue() {
            if (value == null) {
                return monkey1.includesHumanInValue() || monkey2.includesHumanInValue();
            } else {
                return name.equals("humn");
            }
        }

        public long getMonkeyValue() {
            if (value != null) { return value; }
            return(operation.operate(monkey1.getMonkeyValue(), monkey2.getMonkeyValue()));
        }

        public void setDesiredValues(long desiredValue) {
            //  we shouldn't hit these cases
            boolean isHuman = name.equals("humn");
            if (!includesHumanInValue()
            || (value != null && !name.equals("humn"))) {
                System.out.println("Something has gone wrong");
                return;
            }

            if (value != null && name.equals("humn")) {
                this.desiredMonkeyValue = desiredValue;
                return;
            }

            if (monkey1 == null || monkey2 == null) {
                System.out.println("Something has gone wrong");
                return;
            }

            if (monkey1.includesHumanInValue() && monkey2.includesHumanInValue()) {
                System.out.println("This is more complicated than expected");
                return;
            }

            if (monkey1.includesHumanInValue()) {
                if (operation.getClass() == MonkeyAdd.class) {
                    monkey1.setDesiredValues(desiredValue - monkey2.getMonkeyValue());
                } else if (operation.getClass() == MonkeySubtract.class) {
                    monkey1.setDesiredValues(desiredValue + monkey2.getMonkeyValue());
                } else if (operation.getClass() == MonkeyMultiply.class) {
                    monkey1.setDesiredValues(desiredValue / monkey2.getMonkeyValue());
                } else if (operation.getClass() == MonkeyDivide.class) {
                    monkey1.setDesiredValues(desiredValue * monkey2.getMonkeyValue());
                } else {
                    System.out.println("oops");
                }
            } else {
                if (operation.getClass() == MonkeyAdd.class) {
                    monkey2.setDesiredValues(desiredValue - monkey1.getMonkeyValue());
                } else if (operation.getClass() == MonkeySubtract.class) {
                    monkey2.setDesiredValues(monkey1.getMonkeyValue() - desiredValue);
                } else if (operation.getClass() == MonkeyMultiply.class) {
                    monkey2.setDesiredValues(desiredValue / monkey1.getMonkeyValue());
                } else if (operation.getClass() == MonkeyDivide.class) {
                    monkey2.setDesiredValues(desiredValue / monkey1.getMonkeyValue());
                } else {
                    System.out.println("oops");
                }
            }
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
            if (value == null) {
                return "Monkey " + name + ": " + monkey1Name + " " + operator + " " + monkey2Name;
            } else {
                return "Monkey " + name + ": " + value;
            }
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

        List<Monkey> barrelOfMonkeys = getMonkeysFromData(data);
        Map<String, Monkey> monkeyMap = new HashMap<>();
        for (Monkey monkey : barrelOfMonkeys) {
            monkeyMap.put(monkey.name, monkey);
        }

        //  connect the monkeys to their sub-monkeys
        for (Monkey monkey : barrelOfMonkeys) {
            if (monkey.monkey1Name != null) {
                monkey.monkey1 = monkeyMap.get(monkey.monkey1Name);
            }
            if (monkey.monkey2Name != null) {
                monkey.monkey2 = monkeyMap.get(monkey.monkey2Name);
            }
        }

        long rootValue = monkeyMap.get("root").getMonkeyValue();
        System.out.println("Part 1: Root value: " + rootValue);

        Monkey rootMonkey = monkeyMap.get("root");

        if (rootMonkey.monkey1.includesHumanInValue()) {
            rootMonkey.monkey1.setDesiredValues(rootMonkey.monkey2.getMonkeyValue());
        } else {
            rootMonkey.monkey2.setDesiredValues(rootMonkey.monkey1.getMonkeyValue());
        }
        Monkey me = monkeyMap.get("humn");
        System.out.println("Part 2: Desired value for human: " + me.desiredMonkeyValue);

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
