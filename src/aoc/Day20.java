package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Day20 {
    private static class Element {
        int id;
        int value;

        Element(int id, int value) {
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

        System.out.println("Starting with");
        for (Element element : originalOrderList) {
            int currentIndex = mixedList.indexOf(element);
            mixedList.remove(element);
            int newIndex = (currentIndex + element.value);
            if (newIndex < 0) {
                while(newIndex < 0) {
                    newIndex += mixedList.size();
                }
            } else {
                newIndex %= mixedList.size();
            }
            mixedList.add(newIndex, element);
//            System.out.println("Moving: " + element.value + ", list is now:");
//            System.out.println(mixedList);
        }

        //  get answers to part 1
        //  get index of 0
        int startIdx = 0;
        for (int i = 0; i < mixedList.size(); i++) {
            if (mixedList.get(i).value == 0) {
                startIdx = i;
                break;
            }
        }
        int oneThousand = mixedList.get((startIdx + 1000) % mixedList.size()).value;
        int twoThousand = mixedList.get((startIdx + 2000) % mixedList.size()).value;
        int threeThousand = mixedList.get((startIdx + 3000) % mixedList.size()).value;
        int sum = oneThousand + twoThousand + threeThousand;
        System.out.println("Part 1: Sum is " + sum);
    }

}
