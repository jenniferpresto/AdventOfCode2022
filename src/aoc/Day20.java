package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Day20 {
    private static long DECRYPTION_KEY = 811589153;
    private static class Element {
        int id;
        long value;

        Element(int id, long value) {
            this.id = id;
            this.value = value;
        }

        @Override
        public boolean equals (Object o){
            if (this == o) return true;
            if (getClass() != o.getClass()) return false;
            Element other = (Element)o;
            return this.id == other.id && this.value == other.value;
        }

        @Override
        public String toString() {
            return String.valueOf(this.value);
        }
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/Day20.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }

        List<Element> originalOrderList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            originalOrderList.add(new Element(i, Integer.valueOf(data.get(i))));
        }
        List<Element> mixedList = new ArrayList<>(originalOrderList);

        for (Element element : originalOrderList) {
            int currentIndex = mixedList.indexOf(element);
            mixedList.remove(element);
            long newIndex = (currentIndex + element.value);
            if (newIndex < 0) {
                while(newIndex < 0) {
                    newIndex += mixedList.size();
                }
                System.out.println("Current index: " + currentIndex + ", new: " + newIndex);
            } else {
                newIndex %= mixedList.size();
            }
            mixedList.add((int)newIndex, element);
        }

//          get index of 0
        int startIdx = 0;
        for (int i = 0; i < mixedList.size(); i++) {
            if (mixedList.get(i).value == 0) {
                startIdx = i;
                break;
            }
        }

        long oneThousand = mixedList.get((startIdx + 1000) % mixedList.size()).value;
        long twoThousand = mixedList.get((startIdx + 2000) % mixedList.size()).value;
        long threeThousand = mixedList.get((startIdx + 3000) % mixedList.size()).value;
        long sum = oneThousand + twoThousand + threeThousand;
        System.out.println("Part 1: Sum is " + sum);

        //  part 2
        List<Element> mixedListWithKey = new ArrayList<>(originalOrderList);
        for (Element element : mixedListWithKey) {
            element.value *= DECRYPTION_KEY; // note, this changes values of elements in all arrays
        }
        for (int i = 0; i < 10; i++) {
            for (Element element : originalOrderList) {
                int currentIndex = mixedListWithKey.indexOf(element);

                mixedListWithKey.remove(element);
                long newIndex = (currentIndex + element.value);
                if (newIndex < 0) {
                    newIndex = (newIndex % mixedListWithKey.size()) + mixedListWithKey.size();
                } else {
                    newIndex %= mixedListWithKey.size();
                }
                mixedListWithKey.add((int)newIndex, element);
            }
        }

        //  get index of 0
        int startIdx2 = 0;
        for (int i = 0; i < mixedListWithKey.size(); i++) {
            if (mixedListWithKey.get(i).value == 0) {
                startIdx2 = i;
                break;
            }
        }

        long oneThousand2 = mixedListWithKey.get((startIdx2 + 1000) % mixedListWithKey.size()).value;
        long twoThousand2 = mixedListWithKey.get((startIdx2 + 2000) % mixedListWithKey.size()).value;
        long threeThousand2 = mixedListWithKey.get((startIdx2 + 3000) % mixedListWithKey.size()).value;
        long sum2 = oneThousand2 + twoThousand2 + threeThousand2;
        System.out.println("Part 2: Sum is " + sum2);
    }
}
