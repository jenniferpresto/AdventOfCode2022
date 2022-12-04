package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day01 {
    public static void main(String[] args) throws Exception {
        final List<String> data = new ArrayList<>();
        final List<Integer> elfTotals = new ArrayList<>();

        try (final Scanner scanner = new Scanner(new File("data/Day01.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        }

        Integer totalForElf = 0;
        for (String str : data) {
            if (str.isEmpty()) {
                elfTotals.add(totalForElf);
                totalForElf = 0;
            } else {
                totalForElf += Integer.parseInt(str);
            }
        }
        final Integer mostCalories = elfTotals.stream().max(Integer::compare).get();
        System.out.println("Elf with most calories has " + mostCalories);

        Integer[] topThree = {0, 0, 0};
        for (Integer total : elfTotals) {
            boolean bigger = false;
            for (int i = 0; i < 3; i++) {
                if (bigger) {
                    continue;
                }
                if (total > topThree[i]) {
                    topThree[i] = total;
                    bigger = true;
                }
            }
        }
        final Integer topThreeTotal = topThree[0] + topThree[1] + topThree[2];
        System.out.println("Total for the top three: " + topThreeTotal);

        //  do this slightly more concisely
        Collections.sort(elfTotals, Collections.reverseOrder());
        Integer partTwoTotal = elfTotals.stream().limit(3).mapToInt(Integer::intValue).sum();
        System.out.println("Total for top three, again: " + partTwoTotal);
    }
}
