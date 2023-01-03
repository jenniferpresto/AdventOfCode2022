package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Day25 {
    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/Day25.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }
        long total = 0L;
        for (String line : data) {
            total += convertToDecimal(line);
        }
        String snafu = convertToSnafu(total);
        String base5 = convertToBaseFive(total);
        System.out.println("Answer is " + snafu + ", which, for fun, is " + base5 + " in base 5");
    }

    private static String convertToSnafu(final long normal) {
        Map<Long, String> snafuDigits = new HashMap<>();
        snafuDigits.put(2L, "2");
        snafuDigits.put(1L, "1");
        snafuDigits.put(0L, "0");
        snafuDigits.put(-1L, "-");
        snafuDigits.put(-2L, "=");
        long remainingValue = normal;
        String snafuNum = "";
        while (remainingValue > 0) {
            final long remainder = ((remainingValue + 2) % 5) - 2;
            remainingValue = (long)Math.floor((remainingValue + 2) / 5);
            snafuNum = snafuDigits.get(remainder) + snafuNum;
        }
        return snafuNum;
    }


    private static long convertToDecimal(final String snafu) {
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
        return value;
    }

    private static String convertToBaseFive(long normal) {
        String baseFive = "";
        while (normal > 0) {
            long remainder = normal % 5;
            normal = (long)Math.floor(normal / 5);
            baseFive = remainder + baseFive;
        }
        return baseFive;
    }
}
