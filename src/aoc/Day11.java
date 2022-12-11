package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Day11 {

    public static final boolean PART_1 = false;
    public static final int NUM_ROUNDS = PART_1 ? 20 : 10000;
    public static long LEAST_COMMON_MULT = 1;
    private interface MonkeyOperator {
        public long operate(long a, long b);
    }

    private static class Adder implements MonkeyOperator {
        @Override
        public long operate(long a, long b) {
            return a + b;
        }
    }

    private static class Multiplier implements MonkeyOperator {
        @Override
        public long operate(long a, long b) {
            return a * b;
        }
    }

    private static MonkeyOperator ADDER = new Adder();
    private static MonkeyOperator MULTIPLIER = new Multiplier();

    private static class Monkey {
        final int id;
        long numInspections = 0;
        Queue<Long> items = new LinkedList<>();
        MonkeyOperator operator;
        String valueA = "";
        String valueB = "";
        int testDivisor = 1;
        Monkey trueRecipient;
        Monkey falseRecipient;

        Monkey(int id) {
            this.id = id;
        }

        public long getNumInspections() { return numInspections; }
        public void catchItem(Long item) {
            items.add(item);
        }

        public void inspectOperateTestAndThrowTopItem() {
            this.numInspections++;
            //  pick up the item
            Long top = items.remove();
            if (top > LEAST_COMMON_MULT) {
                top = top % LEAST_COMMON_MULT;
            }
            Long firstValue = valueA.equals("old") ? top.longValue() : Long.valueOf(valueA);
            Long secondValue = valueB.equals("old") ? top.longValue() : Long.valueOf(valueB);
            top = operator.operate(firstValue, secondValue);
            if (PART_1) {
                //  get bored, mitigate worry
                top = Math.floorDiv(top, 3);
            }
           //  test and throw
            Monkey recipientMonkey = top % testDivisor == 0 ? trueRecipient : falseRecipient;
            recipientMonkey.catchItem(top);
        }
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/Day11.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }

        //  create the monkeys
        int numMonkeys = 0;
        for (String line : data) {
            if (line.startsWith("Monkey")) {
                numMonkeys++;
            }
        }
        List<Monkey> monkeys = new ArrayList<>();
        for (int i = 0; i < numMonkeys; i++) {
            Monkey monkey = new Monkey(i);
            monkeys.add(monkey);
        }
        int currentMonkey = 0;
        for (String line : data) {
            String trimmed = line.trim();
            String[] splitLine = trimmed.split(" ");
            if (trimmed.startsWith("Starting items:")) {
                String itemStr = trimmed.substring(16);
                String[] parsedItems = itemStr.split(", ");
                for (String item : parsedItems) {
                    monkeys.get(currentMonkey).items.add(Long.valueOf(item));
                }
            } else if (trimmed.startsWith("Operation")) {
                monkeys.get(currentMonkey).valueA = splitLine[3];
                monkeys.get(currentMonkey).valueB = splitLine[5];
                monkeys.get(currentMonkey).operator = splitLine[4].equals("+") ? ADDER : MULTIPLIER;
            } else if (trimmed.startsWith("Test")) {
                monkeys.get(currentMonkey).testDivisor = Integer.valueOf(splitLine[3]);
            } else if (trimmed.startsWith("If true:")) {
                monkeys.get(currentMonkey).trueRecipient = monkeys.get(Integer.valueOf(splitLine[5]));
            } else if (trimmed.startsWith("If false:")) {
                monkeys.get(currentMonkey).falseRecipient = monkeys.get(Integer.valueOf(splitLine[5]));
            } else if (line.isBlank()) {
                currentMonkey++;
            }
        }

        //  get smallest multiple of all divisors
        for (int i = 0; i < monkeys.size(); i++) {
            LEAST_COMMON_MULT = getLeastCommonMultiple(LEAST_COMMON_MULT, monkeys.get(i).testDivisor);
        }

        for (int i = 0; i < NUM_ROUNDS; i++) {
            for (int j = 0; j < monkeys.size(); j++) {
                while(monkeys.get(j).items.size() > 0) {
                    monkeys.get(j).inspectOperateTestAndThrowTopItem();
                }
            }
        }

        for (Monkey monkey : monkeys) {
            System.out.println("Monkey " + monkey.id + " inspected items " + monkey.getNumInspections() + " times");
        }

        //  sort the monkeys by level of activity
        monkeys.sort(Comparator.comparingLong(Monkey::getNumInspections).reversed());

        System.out.println("Level of monkey business: " + (monkeys.get(0).getNumInspections() * monkeys.get(1).getNumInspections()));
    }

    //  no zeroes or negative values
    //  turned out to be unnecessary for this input, but hey ho!
    public static long getLeastCommonMultiple(long a, long b) {
        long higher = a > b ? a : b;
        long lower = higher == a ? b : a;
        long lcm = higher;
        while (lcm  % lower != 0) {
            lcm += higher;
        }
        return lcm;
    }
}
