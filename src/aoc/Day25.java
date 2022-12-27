package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day25 {
    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("testData/Day25.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }
//        for (String line : data) {
//            convertToDigital(line);
//        }
        convertToBaseFive(24);
    }

    private static long convertToDigital(final String snafu) {
        long place = 1L;
        long value = 0L;
        for (int i = snafu.length() - 1; i > -1; i--) {
            char digit = snafu.charAt(i);
            long mult;
            if (digit == '=') {
                mult = -2;
            } else if (digit == '-') {
                mult = -1;
            } else {
                mult = Long.valueOf(String.valueOf(digit));
            }
            value += mult * place;
            place *= 5;
        }
        System.out.println(value);
        return value;
    }

    private static String convertToBaseFive(long normal) {
        String baseFive = "";
        while (normal > 0) {
            long remainder = normal % 5;
            normal = (long)Math.floor(normal / 5);
            baseFive = remainder + baseFive;
        }
        System.out.println(baseFive);
        return baseFive;
    }
}
