package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Day06 {

//    public static final int PACKET_SIZE = 4; // part 1
    public static final int PACKET_SIZE = 14; // part 2

    public static void main(String[] args) throws Exception {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/Day06.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        }

        String elfBuffer = data.get(0);
        Character[] marker = new Character[PACKET_SIZE];
        Arrays.fill(marker, '0');
        int indx = 0;
        for (char c : elfBuffer.toCharArray()) {
            marker[indx % PACKET_SIZE] = c;
            if (allCharsAreDifferent(marker)) {
                break;
            }
            indx++;
        }
        System.out.println("Part 1: " + (indx + 1));
    }

    private static boolean allCharsAreDifferent(Character[] charArray) {
        Set<Character> charSet = new HashSet<>();
        for (Character c : charArray) {
            if (charSet.contains(c)) {
                return false;
            }
            charSet.add(c);
        }
        if (charSet.contains('0')) {
            return false;
        }
        return true;
    };
}
