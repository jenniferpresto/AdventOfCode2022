package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Day13 {
    public static class Packet {
        String definingString = "";
        int beginningIndex = 0;
        int endIndex = 0;
//        List<Packet> subPackets = new ArrayList<>();
        LinkedList<Packet> subPackets = new LinkedList<>();

        Integer value;

        public boolean isValuePacket() {
            if (!subPackets.isEmpty() && value != null) {
                System.out.println("There's been a terrible mistake");
            }
            return value != null;
        }

        @Override
        public String toString() {
            String string = "";
            if (!subPackets.isEmpty() && value != null) {
                string += "SOMETHING IS WRONG HERE";
            }
            if (!subPackets.isEmpty()) {
                string += "[";
                for (int i = 0; i < subPackets.size(); i++) {
                    String comma = i == (subPackets.size() - 1) ? "" : ",";

                    string += subPackets.get(i).toString() + comma;
                }
                string += "]";
            } else {
                if (value == null) {
                    string += "-";
                } else {
                    string += value.toString();
                }
            }
            return string;
        }
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("testData/Day13.txt"))) {
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
                if (allPackets.size() == 10) {
                    int jennifer = 9;
                }
                Packet root = new Packet();
                allPackets.add(createPacketFromString(root, line, 1));
            }
        }
        boolean rightOrder = packetsAreInRightOrder(allPackets.get(6), allPackets.get(7));

        int pairIndex = 1;
        int sumOfIndices = 0;
        for (int i = 0; i < allPackets.size(); i+=2) {
//            System.out.println("*****************************");
//            System.out.println("Pair " + pairIndex + ": Comparing " + i + " to " + (i + 1) + ": ");
//            if (packetsAreInRightOrder(allPackets.get(i), allPackets.get(i+1))) {
//                sumOfIndices += pairIndex;
//                System.out.println("true");
//            } else {
//                System.out.println("false");
//            }
//            pairIndex++;
        }
        System.out.println("Part 1: " + sumOfIndices);
//
//        int jennifer = 9;
    }

    static Boolean packetsAreInRightOrder(Packet left, Packet right) {
        System.out.println("Compare: " + left + " vs. " + right);
//        int i = 0;
        //  both are integers
        if (left.isValuePacket() && right.isValuePacket()) {
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
        //  both are lists
        else if (!left.subPackets.isEmpty() && !right.subPackets.isEmpty()) {
            Packet newLeft = left.subPackets.pop();
            Packet newRight = right.subPackets.pop();
//            System.out.println("Both are packet lists, now comparing: (" + newLeft + " vs. " + newRight + ")");
            Boolean isOrder = packetsAreInRightOrder(newLeft, newRight);
            if (isOrder == null) {
                return packetsAreInRightOrder(left, right);
            } else {
                return isOrder;
            }
        }
        else if (left.isValuePacket() && !right.subPackets.isEmpty()) {
            left = getConvertedValuePacket(left);
            System.out.println("Mixed types; convert left to " + left + " and retry comparison");
            return packetsAreInRightOrder(left, right);
        } else if (!left.subPackets.isEmpty() && right.isValuePacket()) {
            right = getConvertedValuePacket(right);
            System.out.println("Mixed types; convert right to " + right + " and retry comparison");
            return packetsAreInRightOrder(left, right);
        }
        //  still no answer
//        System.out.println("What exactly is the situation here::");
        if (!left.subPackets.isEmpty() && !right.subPackets.isEmpty()) {
            Packet newLeft = left.subPackets.pop();
            Packet newRight = right.subPackets.pop();
            System.out.println("No answer yet, go to next one: (" + newLeft + " vs. " + newRight + ")");
            return packetsAreInRightOrder(newLeft, newRight);
        } else if (left.subPackets.isEmpty() && !right.subPackets.isEmpty()) {
            System.out.println("Left side ran out of items, returning true");
            return true;
        } else if (!left.subPackets.isEmpty() && right.subPackets.isEmpty()) {
            System.out.println("Right side ran out of items, returning false");
            return false;
        } else {
//            System.out.println("We seem to have a tie, returning null: " + left + "vs. " + right);
            return null;
        }
//        while(!left.subPackets.isEmpty() && !right.subPackets.isEmpty()) {
//            if (i < left.subPackets.size() && i < right.subPackets.size()) {
//                Packet leftSub = left.subPackets.get(i);
//                Packet rightSub = right.subPackets.get(i);
//                //  two plain straight values
//                if (leftSub.isValuePacket() && rightSub.isValuePacket()) {
//                    if (leftSub.value < rightSub.value) {
//                        System.out.println("Two value packets: left wins (" +  left.subPackets.get(i).value + " vs. " + right.subPackets.get(i) + ")");
//                        return true;
//                    } else if (leftSub.value > rightSub.value) {
//                        System.out.println("Two value packets: right wins (" +  left.subPackets.get(i).value + " vs. " + right.subPackets.get(i) + ")");
//                        return false;
//                    } else {
//                        System.out.println("Two value packets: match (" +  left.subPackets.get(i).value + " vs. " + right.subPackets.get(i) + ")");
//
//                    }
//                } else if (!left.subPackets.get(i).isValuePacket() && !right.subPackets.get(i).isValuePacket()) {
//                    System.out.println("Both are packet lists: (" + left.subPackets.get(i) + " vs. " + right.subPackets.get(i) + ")");
//                    return packetsAreInRightOrder(left.subPackets.get(i), right.subPackets.get(i));
////                    if (left.subPackets.get(i).subPackets.size() < right.subPackets.get(i).subPackets.size()) {
////                        System.out.println("Left packet list shorter than right (" + left.subPackets.get(i) + " vs. " + right.subPackets.get(i) + ")");
////                        return true;
////                    } else if (left.subPackets.get(i).subPackets.size() > right.subPackets.get(i).subPackets.size()) {
////                        System.out.println("Left packet list longer than right (" + left.subPackets.get(i) + " vs. " + right.subPackets.get(i) + ")");
////                        return false;
////                    } else if (left.subPackets.get(i).subPackets.size() == right.subPackets.get(i).subPackets.size()) {
////                        System.out.println("Packets have same length: (" + left.subPackets.get(i) + " vs. " + right.subPackets.get(i) + ")");
////                        return packetsAreInRightOrder(left.subPackets.get(i), right.subPackets.get(i));
////                    }
//                } else if (left.subPackets.get(i).isValuePacket()) {
//                    System.out.println("Converting left packet");
//                    left.subPackets.set(i, getConvertedValuePacket(left.subPackets.get(i)));
//                    return packetsAreInRightOrder(left.subPackets.get(i), right.subPackets.get(i));
//                } else if (right.subPackets.get(i).isValuePacket()) {
//                    System.out.println("Converting right packet");
//                    right.subPackets.set(i, getConvertedValuePacket(right.subPackets.get(i)));
//                    return packetsAreInRightOrder(left.subPackets.get(i), right.subPackets.get(i));
//                }
//            } else {
//                if (i > left.subPackets.size() - 1) {
//                    if (!right.subPackets.get(i).isValuePacket()) {
//                        left = getConvertedValuePacket(left);
//                        return packetsAreInRightOrder(left.subPackets.get(0), right.subPackets.get(i));
//                    } else {
//                        System.out.println("Ran out on left side");
//                        return true;
//                    }
//                } else if (i > right.subPackets.size() - 1) {
//                    if (!left.isValuePacket()) {
//                        right = getConvertedValuePacket(right);
//                        return packetsAreInRightOrder(left, right);
//                    } else {
//                        System.out.println("Ran out on right side");
//                        return false;
//                    }
//                } else {
//                    System.out.println("Something went wrong, again...");
//                    return false;
//                }
//            }
//            i++;
//        }
    }

    static Packet getConvertedValuePacket(Packet packet) {
        if (!packet.isValuePacket()) {
            System.out.println("Not a value packet!");
            return packet;
        }
        Packet convertedPacket = new Packet();
        Packet newSub = new Packet();
        newSub.value = packet.value;
        convertedPacket.subPackets.add(newSub);
        return convertedPacket;
    }

    static Packet createPacketFromString(Packet parent, String input, int startIndex) {
        for (int i = startIndex; i < input.length(); ) {
            if (input.charAt(i) == '[') {
                Packet newRoot = new Packet();
                newRoot.beginningIndex = i + 1;
                Packet newPacket = createPacketFromString(newRoot, input, i + 1);
                parent.subPackets.add(newPacket);
                i = newPacket.endIndex + 1;
            } else if (input.charAt(i) == ',') {
                i++;
            } else if (input.charAt(i) == ']') {
                parent.endIndex = i;
                return parent;
            }
            else if (Character.isDigit(input.charAt(i))) {
                String relevantString = input.substring(i);
                String[] digits = relevantString.split("\\,|]");
                int commaIdx = relevantString.indexOf(',');
                int bracketIdx = relevantString.indexOf(']');
                int endOfNumberIdx = commaIdx == -1 || bracketIdx < commaIdx ? bracketIdx : commaIdx;
                String digitStr = relevantString.substring(0, endOfNumberIdx);
                Integer value = Integer.valueOf(digitStr);
                Packet valuePacket = new Packet();
                valuePacket.value = value;
                parent.subPackets.add(valuePacket);
                i += endOfNumberIdx;
            }
        }
        return parent;
    }
}
