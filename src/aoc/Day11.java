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
    private static class Monkey {
        final int id;
        long numInspections = 0;
        Queue<Long> items = new LinkedList<>();
        String operation = "";
        int testDivisor = 1;
        int commonDivisor = 1;
        Monkey trueRecipient;
        Monkey falseRecipient;

        Monkey(int id) {
            this.id = id;
        }

        public long getNumInspections() { return numInspections; }
        public void catchItem(Long item) {
//            System.out.println("Monkey " + id + " catching item " + item);
            items.add(item);
        }

        public void inspectOperateTestAndThrowTopItem() {
            this.numInspections++;
            //  pick up the item
            Long top = items.remove();
            if (top > commonDivisor) {
                top = top % commonDivisor;
            }

            String[] splitOperation = operation.split(" ");
            Long firstValue = splitOperation[0].equals("old") ? top.longValue() : Integer.valueOf(splitOperation[0]);
            Long secondValue = splitOperation[2].equals("old") ? top.longValue() : Integer.valueOf(splitOperation[2]);
            //  inspect it and do operation
            if (splitOperation[1].equals("+")) {
                top = firstValue + secondValue;
            } else if (splitOperation[1].equals("*")) {
                top = firstValue * secondValue;
            } else {
                System.out.println("whoops...");
            }
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
            if (line.startsWith("Monkey")) {
                continue;
            }
            String trimmed = line.trim();
            if (trimmed.startsWith("Starting items:")) {
                String itemStr = trimmed.substring(16);
                String[] parsedItems = itemStr.split(", ");
                for (String item : parsedItems) {
                    monkeys.get(currentMonkey).items.add(Long.valueOf(item));
                }
            }
            String[] splitLine = trimmed.split(" ");
            if (trimmed.startsWith("Operation")) {
                monkeys.get(currentMonkey).operation = line.substring(line.indexOf('=') + 2);
                continue;
            }
            if (trimmed.startsWith("Test")) {
                monkeys.get(currentMonkey).testDivisor = Integer.valueOf(splitLine[3]);
                continue;
            }
            if (trimmed.startsWith("If true:")) {
                monkeys.get(currentMonkey).trueRecipient = monkeys.get(Integer.valueOf(splitLine[5]));
                continue;
            }
            if (trimmed.startsWith("If false:")) {
                monkeys.get(currentMonkey).falseRecipient = monkeys.get(Integer.valueOf(splitLine[5]));
                continue;
            }
            if (line.isBlank()) {
                currentMonkey++;
            }
        }

        //  get least common multiple of all divisors
        int commonMult = 1;
        for (int i = 0; i < monkeys.size(); i++) {
            commonMult = getLeastCommonMultiple(commonMult, monkeys.get(i).testDivisor);
        }
        System.out.println("Common multiple is :" + commonMult);
        for (Monkey monkey : monkeys) {
            monkey.commonDivisor = commonMult;
        }
        System.out.println("LCM for 6, 15: " + getLeastCommonMultiple(1, 19));

        for (int i = 0; i < NUM_ROUNDS; i++) {
            for (int j = 0; j < monkeys.size(); j++) {
                while(monkeys.get(j).items.size() > 0) {
                    monkeys.get(j).inspectOperateTestAndThrowTopItem();
                }
            }
            int jennifer = 9;
        }
//
        for (Monkey monkey : monkeys) {
            System.out.println("Monkey " + monkey.id + " inspected items " + monkey.getNumInspections() + " times");
        }
        //  sort the monkeys by level of activity
        monkeys.sort(Comparator.comparingLong(Monkey::getNumInspections).reversed());

        System.out.println("Level of monkey business: " + (monkeys.get(0).getNumInspections() * monkeys.get(1).getNumInspections()));
    }

    //  no zero or negative values
    public static int getLeastCommonMultiple(int a, int b) {
        int higher = a > b ? a : b;
        int lower = higher == a ? b : a;
        int lcm = higher;
        while (lcm  % lower != 0) {
            lcm += higher;
        }
        return lcm;
    }
}
