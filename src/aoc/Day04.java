package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day04 {
    private static class RangePair {
        final private int beg1;
        final private int end1;
        final private int beg2;
        final private int end2;
        RangePair(int beg1, int end1, int beg2, int end2) {
            this.beg1 = beg1;
            this.end1 = end1;
            this.beg2 = beg2;
            this.end2 = end2;
        }

        public boolean getFullyContains() {
            return beg1 <= beg2 && end1 >= end2
                    || beg2 <= beg1 && end2 >= end1;
        }

        public boolean getOverlaps() {
            return (end1 >= beg2 && beg1 <= end2)
                    || (end2 >= beg1 && beg2 <= end1);
        }

        @Override
        public String toString() {
            return beg1 + "-" + end1 + "," + beg2 + "-" + end2;
        }
    }

    public static void main(String[] args) throws Exception{
        List<String> data = new ArrayList<String>();
        try (final Scanner scanner = new Scanner(new File("data/Day04.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        }
        List<RangePair> rangePairs = new ArrayList<>();
        for (String line : data) {
            String[] pairs = line.split(",");
            String[] pair1 = pairs[0].split("-");
            String[] pair2 = pairs[1].split("-");
            RangePair rangePair = new RangePair(
                    Integer.parseInt(pair1[0]),
                    Integer.parseInt(pair1[1]),
                    Integer.parseInt(pair2[0]),
                    Integer.parseInt(pair2[1]));
            rangePairs.add(rangePair);
        }

        //  Part 1: One range includes another
        //  Part 2: Overlap at all
        int totalFullyContainingPairs = 0;
        int totalOverlappingPairs = 0;
        for (RangePair rangePair : rangePairs) {
            if (rangePair.getFullyContains()) {
                totalFullyContainingPairs++;
            }
            if (rangePair.getOverlaps()) {
                totalOverlappingPairs++;
            }
        }

        System.out.println("Answer to part 1 (one contains another): " + totalFullyContainingPairs);
        System.out.println("Answer to part 2 (overlap at all): " + totalOverlappingPairs);
    }
}
