package aoc;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;

public class Day16 {
    static final int NUM_MINUTES = 8;
    static AtomicInteger HIGHEST_FLOW = new AtomicInteger();
    private static class CaveRoom {
        final String valveName;
        final int flow;
        final List<String> childNames;
        final List<CaveRoom> children;
        boolean valveOpen = false;

        CaveRoom(String valveName, int flow) {
            this.valveName = valveName;
            this.flow = flow;
            this.childNames = new ArrayList<>();
            this.children = new ArrayList<>();
        }

        @Override
        public String toString() {
            String openString = this.valveOpen ? "open" : "shut";
            String output = valveName + "(" + openString + "): flow: " + flow + ", children: ";
            for (String childName : childNames) {
                output += childName + " ";
            }
            return output;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;
            CaveRoom other = (CaveRoom)o;
            return this.valveName.equals(other.valveName) &&
                    this.flow == other.flow &&
                    this.childNames.equals(other.childNames) &&
                    this.children.equals(other.children);
        }

        @Override
        public int hashCode() {
            return Objects.hash(this.valveName, this.flow, this.childNames);
        }
    }

    public static void main(String[] args) {
        List<String> data = new ArrayList<>();
        try (final Scanner scanner = new Scanner(new File("testData/Day16.txt"))) {
            while (scanner.hasNext()) {
                data.add(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Oops... " + e.getMessage());
            return;
        }

        //  map out cave system
        List<CaveRoom> caveRooms = new ArrayList<>();
        Map<String, Integer> flowMap = new HashMap<>();
        for (String line : data) {

            String[] instr = line.split(" ");
            String name = instr[1];
            String flowStr = instr[4].split("=")[1];
            int flow = Integer.valueOf(flowStr.substring(0, flowStr.length() - 1));
            flowMap.put(name, flow);
            CaveRoom newRoom = new CaveRoom(name, flow);

            for (int i = 9; i < instr.length; i++) {
                if (instr[i].endsWith(",")) {
                    newRoom.childNames.add(instr[i].substring(0, instr[i].length() -1));
                } else {
                    newRoom.childNames.add(instr[i]);
                }
            }
            caveRooms.add(newRoom);
        }
        //  link 'em up (there's a more efficient way to do this)
        for (CaveRoom room : caveRooms) {
            for (String childName : room.childNames) {
                for (CaveRoom subRoom : caveRooms) {
                    if (subRoom.valveName.equals( childName)) {
                        room.children.add(subRoom);
                        break;
                    }
                }
            }
        }


        //  Tree traversal
        CaveRoom head = caveRooms.get(0);
        String[] path = new String[NUM_MINUTES];
        traverseRooms(head, path, 0, 0, caveRooms);
        System.out.println("Highest flow: " + HIGHEST_FLOW);
    }

    static void traverseRooms(CaveRoom currentRoom, String[] path, int idx, int totalFlow, List<CaveRoom> caveRooms) {
        //  when we start over, shut all the valves
        if (path[0] != null && path[0].equals("-> DD")
            && path[1] != null && path[1].equals("open DD")
            && path[2] != null &&  path[2].equals("-> CC")
            && path[3] != null && path[3].equals("-> BB")
            && path[4] != null && path[4].equals("open BB")
        ){
            int grace = 0;
        }
        if (idx + 1 > NUM_MINUTES) {
            if (totalFlow > HIGHEST_FLOW.get()) {
                HIGHEST_FLOW.set(totalFlow);
            }
            return;
        }
        //  reset all the valves for this path; add to flow accordingly
        for (CaveRoom freshRoom : caveRooms) {
            freshRoom.valveOpen = false;
            for (int i = 0; i < idx; i++) {
                if (path[i].startsWith("open")) {
                    String openCave = path[i].split(" ")[1];
                    CaveRoom cave = getCaveByName(openCave, caveRooms);
                    cave.valveOpen = true;
                }
            }
        }

        //  add up flow for open caves
        for (CaveRoom caveRoom : caveRooms) {
            if (caveRoom.valveOpen) {
                totalFlow += caveRoom.flow;
            }
        }

        //  continue traversing rooms
        if (!currentRoom.valveOpen && currentRoom.flow > 0) {
            path[idx] = "open " + currentRoom.valveName;
            currentRoom.valveOpen = true;
            traverseRooms(currentRoom, path, idx + 1, totalFlow, caveRooms);
        }

        for (CaveRoom child : currentRoom.children) {
            path[idx] = "-> " + child.valveName;
            traverseRooms(child, path, idx + 1, totalFlow, caveRooms);
        }
    }

    static CaveRoom getCaveByName(String name, List<CaveRoom> caveRooms) {
        for (CaveRoom room : caveRooms) {
            if (name.equals(room.valveName)) {
                return room;
            }
        }
        System.out.println("This shouldn't happen");
        return null;
    }
}
