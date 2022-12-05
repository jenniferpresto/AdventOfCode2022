package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

public class Day05 {
    public static void main(String[] args) throws Exception {

        List<String> data = new ArrayList<String>();
        try (final Scanner scanner = new Scanner(new File("data/Day05.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        }

        //  create the stacks of crates
        List<Stack<String>> crateStacks = new ArrayList<>(); // part 1
        List<String> crateStrings = new ArrayList<>(); // part 2

        int indexOfEmptyLine = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).equals("")) {
                indexOfEmptyLine = i;
                break;
            }
        }

        String trimmed = data.get(indexOfEmptyLine - 1).trim();
        int largestCrate = Integer.parseInt(String.valueOf(trimmed.charAt(trimmed.length()-1)));

        for (int j = 0; j < largestCrate; j++) {
            Stack<String> crateStack = new Stack<>();
            crateStacks.add(crateStack);
        }

        for (int i = indexOfEmptyLine - 2; i >= 0; i--) {
            for (int j = 0; j < data.get(i).length(); j++) {
                if (data.get(i).charAt(j) >= 'A' && data.get(i).charAt(j) <= 'Z') {
                    int stackNum;
                    if (j == 2) {
                        stackNum = 0;
                    } else {
                        stackNum = (j - 1) / 4;
                    }
                    crateStacks.get(stackNum).add(String.valueOf(data.get(i).charAt(j)));
                }
            }
        }

        //  create the strings
        for (Stack stack : crateStacks) {
            String letterStack = "";
            Iterator it = stack.iterator();
            while(it.hasNext()) {
                letterStack += it.next().toString();
            }
            crateStrings.add(letterStack);
        }

        //  run through the instructions
        for (int i = indexOfEmptyLine + 1; i < data.size(); i++) {
            String[] instructions = data.get(i).split(" ");

            int num = Integer.parseInt(instructions[1]);
            int fromStack = Integer.parseInt(instructions[3]) - 1;
            int toStack = Integer.parseInt(instructions[5]) - 1;

            //  part 1: move one by one from stack to stack
            for (int j = 0; j < num; j++) {
                crateStacks.get(toStack).add(crateStacks.get(fromStack).pop());
            }

            //  part 2: eh... just use strings
            String newTallerStack = crateStrings.get(toStack);
            String newShorterStack = crateStrings.get(fromStack).substring(0, crateStrings.get(fromStack).length() - num);
            String cratesToMove = crateStrings.get(fromStack).substring(crateStrings.get(fromStack).length() - num);
            newTallerStack += cratesToMove;
            crateStrings.set(toStack, newTallerStack);
            crateStrings.set(fromStack, newShorterStack);
        }
        String msgPt1 = "";
        String msgPt2 = "";
        for (int i = 0; i < largestCrate; i++) {
            msgPt1 += crateStacks.get(i).peek();
            msgPt2 += crateStrings.get(i).charAt(crateStrings.get(i).length() - 1);
        }
        System.out.println("Part 1: " + msgPt1);
        System.out.print("Part 2: " + msgPt2);
    }
}
