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
        try (final Scanner scanner = new Scanner(new File("testData/Day25.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }
//        for (String line : data) {
////            convertToDecimal(line);
//            convertToBaseFive(convertToDecimal(line));
//        }
        convertToSnafu(4L);
    }

    private static void convertToSnafu(final long normal) {
        long power = 0;
        double log = Math.ceil(Math.log(125.0) / Math.log(5.0));
        System.out.println(log);
        //  find the left-most place of the snafu number
        long placeMax = 0L;
        while(true) {
            placeMax = 2 * (long)Math.pow(5, power);
            if (normal > placeMax) {
                power++;
            } else {
                break;
            }
        }
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
        System.out.println(power);
        System.out.println(snafuNum);
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
        System.out.println(baseFive);
        return baseFive;
    }
}
