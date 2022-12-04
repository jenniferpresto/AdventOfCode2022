package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Day02 {
    static HashMap<String, String> elfToYou = new HashMap<>();
    static HashMap<String, String> youToElf = new HashMap<>();

    public static void main(String[] args) throws Exception {
        elfToYou.put("A", "X");
        elfToYou.put("B", "Y");
        elfToYou.put("C", "Z");
        final List<String> data = new ArrayList<>();

        try (final Scanner scanner = new Scanner(new File("data/Day02.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        }
        int totalScore = 0;
        int secondTotal = 0;
        for (String move : data) {
            String elf = String.valueOf(move.charAt(0));
            String you = String.valueOf(move.charAt(2));
            //  Part 1
            int choiceScore = getValueOfChoice(you);
            int outcomeScore = getValueOfOutcome(elf, you);
            totalScore += (choiceScore + outcomeScore);

            //  Part 2
            String yourMove = getYourMove(elf, you);
            int secondChoiceScore = getValueOfChoice(yourMove);
            int secondOutcomeScore = getValueOfOutcome(elf, yourMove);
            secondTotal += (secondChoiceScore + secondOutcomeScore);

        }
        System.out.println("Part 1: " + totalScore);
        System.out.println("Part 2: " + secondTotal);
    }

    private static int getValueOfChoice(String you) {
        int valueOfChoice = 0;
        if (you.equals("X")) {
            valueOfChoice = 1;
        } else if (you.equals("Y")) {
            valueOfChoice = 2;
        } else if (you.equals("Z")) {
            valueOfChoice = 3;
        }

        return valueOfChoice;
    }

    private static int getValueOfOutcome(String elf, String you) {
        //  draw state
        if (you.equals(elfToYou.get(elf))) {
            return 3;
        }

        //  win state
        if (elf.equals("A") && you.equals("Y")
                || (elf.equals("B") && you.equals("Z")
                || (elf.equals("C") && you.equals("X")))) {
            return 6;
        }

        //  loss state
        else if (elf.equals("A") && you.equals("Z")
                || elf.equals("B") && you.equals("X")
                || elf.equals("C") && you.equals("Y")) {
            return 0;
        }

        System.out.println("We shouldn't be here");
        return 0;
    }

    private static String getYourMove(String elf, String you) {
        if (you.equals("Y")) {
            return elfToYou.get(elf);
        }

        //  need to win
        if (you.equals("Z")) {
            if (elf.equals("A")) {
                return "Y";
            } else if (elf.equals("B")) {
                return "Z";
            } else if (elf.equals("C")) {
                return "X";
            }
        }

        //  need to lose
        if (you.equals("X")) {
            if (elf.equals("A")) {
                return "Z";
            } else if (elf.equals("B")) {
                return "X";
            } else if (elf.equals("C")) {
                return "Y";
            }
        }
        System.out.println("We shouldn't get this far");
        return "";
    }
}
