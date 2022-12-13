package aoc;

import java.awt.image.AreaAveragingScaleFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day13 {
    public static class Packet {
        String definingString = "";
        int beginningIndex = 0;
        int endIndex = 0;
        List<Packet> subPackets;
        List<Integer> values;
        Integer value;

        @Override
        public String toString() {
            String string = "";
            if (subPackets != null) {
                string += "[";
                for (int i = 0; i < subPackets.size(); i++) {
                    String comma = i == (subPackets.size() - 1) ? "" : ",";
                    string += subPackets.get(i).toString() + comma;
                }
                string += "]";
            }
            else if (value != null) {
                string += value.toString();
            } else if (value == null && subPackets == null) {
                string += "[]";
            } else {
                string += "SOMETHING IS WRONG; SHOULD NOT HAVE NON-NULL VALUES AND SUBPACKETS";
            }
//            if (values != null) {
//                for (int i = 0; i < values.size(); i++) {
//                    String comma = i == (values.size() - 1) ? "" : ",";
//                    string += values.get(i).toString() + comma;
//                }
//            }
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

        int flag = 0;
        List<Packet> allPackets = new ArrayList<>();
//        Packet root = new Packet();
//        allPackets.add(createPacketFromString(root, data.get(4), 1));
        for (String line : data) {
            if (!line.isBlank()) {
                Packet root = new Packet();
                allPackets.add(createPacketFromString(root, line, 1));
            }
//            flag++;
//            if (flag > 1) break;
        }
        int jennifer = 9;
    }

    static Packet createPacketFromString(Packet parent, String input, int startIndex) {
        for (int i = startIndex; i < input.length(); ) {
            if (input.charAt(i) == '[') {
                Packet newRoot = new Packet();
                newRoot.beginningIndex = i + 1;

                if (parent.subPackets == null) {
                    parent.subPackets = new ArrayList<>();
                }
                Packet newPacket = createPacketFromString(newRoot, input, i + 1);
                parent.subPackets.add(newPacket);
                i = newPacket.endIndex + 1;
            } else if (input.charAt(i) == ',') {
                System.out.println("comma");
                i++;
            } else if (input.charAt(i) == ']') {
                parent.endIndex = i;
                return parent;
            }
            else if (Character.isDigit(input.charAt(i))) {
                System.out.println("This is a digit " + input.charAt(i));
                String relevantString = input.substring(i);
                String[] digits = relevantString.split("\\,|]");
                int commaIdx = relevantString.indexOf(',');
                int bracketIdx = relevantString.indexOf(']');
                int endOfNumberIdx = commaIdx == -1 || bracketIdx < commaIdx ? bracketIdx : commaIdx;


//                Integer value = Integer.valueOf(digits[0]);
                String digitStr = relevantString.substring(0, endOfNumberIdx);
                Integer value = Integer.valueOf(digitStr);
                if (parent.values == null) {
                    parent.values = new ArrayList<>();
                }
                if (parent.subPackets == null) {
                    parent.subPackets = new ArrayList<>();
                }
                Packet valuePacket = new Packet();
                valuePacket.value = value;
                parent.subPackets.add(valuePacket);
                parent.values.add(value);
                i += endOfNumberIdx;
                int jennifer = 9;
            }

        }
        return parent;
    }
}
