package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class Day11 {
    public static final int NUM_ROUNDS = 20;
    private static class Monkey {
        final int id;
        int numInspections = 0;
        Queue<Integer> items = new LinkedList<>();
        String operation = "";
        int testDivisor = 1;
        Monkey trueRecipient;
        Monkey falseRecipient;

        Monkey(int id) {
            this.id = id;
        }

        public int getNumInspections() { return numInspections; }
        public void catchItem(Integer item) {
            items.add(item);
        }

        public void inspectOperateTestAndThrowTopItem() {
            this.numInspections++;
            //  pick up the item
            Integer top = items.remove();

            String[] splitOperation = operation.split(" ");
            Integer firstValue = splitOperation[0].equals("old") ? top.intValue() : Integer.valueOf(splitOperation[0]);
            Integer secondValue = splitOperation[2].equals("old") ? top.intValue() : Integer.valueOf(splitOperation[2]);
            //  inspect it and do operation
            if (splitOperation[1].equals("+")) {
                top = firstValue + secondValue;
            } else if (splitOperation[1].equals("*")) {
                top = firstValue * secondValue;
            } else {
                System.out.println("whoops...");
            }
            //  get bored
            top = Math.floorDiv(top, 3);
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
                    monkeys.get(currentMonkey).items.add(Integer.valueOf(item));
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

        for (int i = 0; i < NUM_ROUNDS; i++) {
            for (int j = 0; j < monkeys.size(); j++) {
                while(monkeys.get(j).items.size() > 0) {
                    monkeys.get(j).inspectOperateTestAndThrowTopItem();
                }
            }
            int jennifer = 9;
        }

        //  sort the monkeys by level of activity
        monkeys.sort(Comparator.comparingInt(Monkey::getNumInspections).reversed());

        System.out.println("Level of monkey business: " + (monkeys.get(0).getNumInspections() * monkeys.get(1).getNumInspections()));
    }
}
