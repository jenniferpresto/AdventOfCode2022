package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Day13 {
    public static class Packet implements Comparable<Packet> {
        int beginningIndex = 0;
        int endIndex = 0;
        List<Packet> subPacketsList = new ArrayList<>();

        Integer value;

        public boolean isValuePacket() {
            if (!subPacketsList.isEmpty() && value != null) {
                System.out.println("There's been a terrible mistake");
            }
            return value != null;
        }

        @Override
        public String toString() {
            String string = "";
            if (!subPacketsList.isEmpty() && value != null) {
                string += "SOMETHING IS WRONG HERE";
            }

            if (value == null) {
                string += "[";
                for (int i = 0; i < subPacketsList.size(); i++) {
                    String comma = i == (subPacketsList.size() - 1) ? "" : ",";

                    string += subPacketsList.get(i).toString() + comma;
                }
                string += "]";
            } else {
                string += value.toString();
            }
            return string;
        }

        @Override
        public int compareTo(Packet other) {
            Boolean inOrder = comparePackets(this, other);
            if (inOrder != null && inOrder.equals(true)) {
                return -1;
            }
            return 1;
        }
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("data/Day13.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }

        List<Packet> allPackets = new ArrayList<>();
        for (String line : data) {
            if (!line.isBlank()) {
                Packet root = new Packet();
                allPackets.add(createPacketFromString(root, line, 1));
            }
        }

        int pairIndex = 1;
        int sumOfIndices = 0;
        for (int i = 0; i < allPackets.size(); i+=2) {
//            System.out.println("*****************************");
//            System.out.println("Pair " + pairIndex + ": Comparing " + i + " vs " + (i + 1) + ": ");
//            System.out.println("Left: " + allPackets.get(i));
//            System.out.println("Right: " + allPackets.get(i+1));
//            System.out.println("*****************************");
            if (comparePackets(allPackets.get(i), allPackets.get(i+1))) {
                sumOfIndices += pairIndex;
                System.out.println("Pair " + pairIndex + " is true!");
            } else {
                System.out.println("false");
            }
            pairIndex++;
        }
        System.out.println("Part 1: " + sumOfIndices);

        //  part 2
        Packet divider1 = createPacketFromString(new Packet(), "[2]", 0);
        Packet divider2 = createPacketFromString(new Packet(), "[6]", 0);
        allPackets.add(divider1);
        allPackets.add(divider2);
        Collections.sort(allPackets);
        int productOfIndices = 1;
        for (int i = 0; i < allPackets.size(); i++) {
            if (allPackets.get(i).equals(divider1) || allPackets.get(i).equals(divider2)) {
                productOfIndices *= (i + 1);
            }
        }
        System.out.println("Part 2: product of indices: " + productOfIndices);
    }


    static Boolean compareLists(List<Packet> left, List<Packet> right) {
        System.out.println("Compare " + left + " vs. " + right);
        int limit = Math.min(left.size(), right.size());
        for (int i = 0; i < limit; i++) {
            Boolean isRightOrder = comparePackets(left.get(i), right.get(i));
            if (isRightOrder != null) {
                return isRightOrder;
            }
        }
        if (left.size() < right.size()) {
            System.out.println("Left side ran out of items, so inputs are in the right order");
            return true;
        } else if (left.size() > right.size()) {
            System.out.println("Right side ran out of items, so inputs are not in the right order");
            return false;
        }

        System.out.println("Lists are equivalent: " + left + ", " + right);
        return null;
    }

    static Boolean compareValuePackets(Packet left, Packet right) {
        System.out.println("Compare " + left + " vs. " + right);
        if (!left.isValuePacket() || !right.isValuePacket()) {
            System.out.println("There's been a mistake!");
        }
        if (left.value < right.value) {
//                System.out.println("Two value packets: returning true (" +  left.value + " vs. " + right.value + ")");
            return true;
        } else if (left.value > right.value) {
//                System.out.println("Two value packets: returning false (" +  left.value + " vs. " + right.value + ")");
            return false;
        } else {
//                System.out.println("Two value packets: match (" +  left.value + " vs. " + right.value + ")");
            return null;
        }
    }



    static Boolean comparePackets(Packet left, Packet right) {
        //  both are integers
        if (left.isValuePacket() && right.isValuePacket()) {
            Boolean isRightOrder = compareValuePackets(left, right);
            if (isRightOrder == null) {
                //  keep going
            } else {
                return isRightOrder;
            }
        }
        //  both are lists
        else if (!left.isValuePacket() && !right.isValuePacket()) {
            Boolean isRightOrder = compareLists(left.subPacketsList, right.subPacketsList);
            if (isRightOrder == null) {
                //  keep going
            } else {
                return isRightOrder;
            }
        }
        else if (left.isValuePacket() && !right.isValuePacket()) {
            left = getConvertedValuePacket(left);
            System.out.println("Mixed types; convert left to " + left + " and retry comparison");
            Boolean isRightOrder = compareLists(left.subPacketsList, right.subPacketsList);
            if (isRightOrder == null) {
                //  keep going
            } else {
                return isRightOrder;
            }
        } else if (!left.isValuePacket() && right.isValuePacket()) {
            right = getConvertedValuePacket(right);
            System.out.println("Mixed types; convert right to " + right + " and retry comparison");
            Boolean isRightOrder = compareLists(left.subPacketsList, right.subPacketsList);
            if (isRightOrder == null) {
                //  keep going
            } else {
                return isRightOrder;
            }
        }
        return null;
    }

    static Packet getConvertedValuePacket(Packet packet) {
        if (!packet.isValuePacket()) {
            System.out.println("Error: Not a value packet!");
            return packet;
        }
        Packet convertedPacket = new Packet();
        Packet newSub = new Packet();
        newSub.value = packet.value;
        convertedPacket.subPacketsList.add(newSub);
        return convertedPacket;
    }

    static Packet createPacketFromString(Packet parent, String input, int startIndex) {
        for (int i = startIndex; i < input.length(); ) {
            if (input.charAt(i) == '[') {
                Packet newRoot = new Packet();
                newRoot.beginningIndex = i + 1;
                Packet newPacket = createPacketFromString(newRoot, input, i + 1);
                parent.subPacketsList.add(newPacket);
                i = newPacket.endIndex + 1;
            } else if (input.charAt(i) == ',') {
                i++;
            } else if (input.charAt(i) == ']') {
                parent.endIndex = i;
                return parent;
            }
            else if (Character.isDigit(input.charAt(i))) {
                String relevantString = input.substring(i);
                int commaIdx = relevantString.indexOf(',');
                int bracketIdx = relevantString.indexOf(']');
                int endOfNumberIdx = commaIdx == -1 || bracketIdx < commaIdx ? bracketIdx : commaIdx;
                String digitStr = relevantString.substring(0, endOfNumberIdx);
                Integer value = Integer.valueOf(digitStr);
                Packet valuePacket = new Packet();
                valuePacket.value = value;
                parent.subPacketsList.add(valuePacket);
                i += endOfNumberIdx;
            }
        }
        return parent;
    }
}
