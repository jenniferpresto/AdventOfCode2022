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
    static final int NUM_MINUTES = 30;
    static AtomicInteger HIGHEST_FLOW = new AtomicInteger();

    private static class RoomPair {
        final String room1;
        final String room2;
        RoomPair(String room1, String room2) {
            this.room1 = room1;
            this.room2 = room2;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || o.getClass() != getClass()) return false;
            RoomPair other = (RoomPair) o;
            return this.room1.equals(other.room1) && this.room2.equals(other.room2);
        }
        boolean isEquivalent(RoomPair other) {
            return (this.room1.equals(other.room1) && this.room2.equals(other.room2)) ||
                    (this.room1.equals(other.room2) && this.room2.equals(other.room1));
        }
    }
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
        if (path[0] != null && path[0].equals("->DD")
            && path[1] != null && path[1].equals("open DD")
            && path[2] != null &&  path[2].equals("->CC")
            && path[3] != null && path[3].equals("->BB")
            && path[4] != null && path[4].equals("open BB")
        ){
//            System.out.println(getPathString(path, idx));
            int grace = 0;
        }
        //  check the path
//        if (doesBacktrack(path, idx)) {
//            System.out.println("Abandoning at " + idx + ": " + getPathString(path, idx));
//            return;
//        }
        if (isInefficient(path, idx)) {
//            System.out.println("Abandoning at " + idx + ": " + getPathString(path, idx));
            return;
        }

        //  see if all valves are already open; if so, can stop traversing rooms
        boolean allOpen = true;
        for (CaveRoom room : caveRooms) {
            if(room.flow > 0 && !room.valveOpen) {
                allOpen = false;
            }
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
        int flowPerMinute = 0;
        for (CaveRoom caveRoom : caveRooms) {
            if (caveRoom.valveOpen) {
                flowPerMinute += caveRoom.flow;
            }
        }

        totalFlow += flowPerMinute;

        if (allOpen) {
            //  add the remaining flow and call it a day
            System.out.println("ALL OPEN!!!!");
            totalFlow += (NUM_MINUTES - idx) * flowPerMinute;
            return;
        }

        //  continue traversing rooms
        if (!currentRoom.valveOpen && currentRoom.flow > 0) {
            path[idx] = "open " + currentRoom.valveName;
            currentRoom.valveOpen = true;
            traverseRooms(currentRoom, path, idx + 1, totalFlow, caveRooms);
        }

        for (CaveRoom child : currentRoom.children) {
            path[idx] = "->" + child.valveName;
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

    static boolean doesBacktrack(String[] path, int idx) {
        if (idx < 4) {
            return false;
        }
        if (path[idx-1].startsWith("->")
                && path[idx-2].startsWith("->")
                && path[idx-3].startsWith("->")
                && path[idx-1].equals(path[idx-3])
        ) {
//            System.out.println("We backtrack: " + getPathString(path, idx));
            return true;
        }
        return false;
    }

    //  if we walk between the same two rooms without opening any valves,
    //  we're being inefficient -- no time for that!
    static boolean isInefficient(String[] path, int idx) {
        int end = idx - 1;
        int start = idx - 1;
        List<String> roomsSinceLastOpenedValve = new ArrayList<>();
        List<RoomPair> pairs  = new ArrayList<>();
        //  go backwards down the path until we last opened a valve
        for (int i = end; i > -1; i--) {
            if(path[i].startsWith("open")) {
                start = i + 1;
                String roomName = path[i].split(" ")[1];
                if (roomsSinceLastOpenedValve.contains(roomName)) {
                    return true;
                }
                roomsSinceLastOpenedValve.add(roomName);
                break;
            } else {
                String roomName = path[i].substring(2);
                if (roomsSinceLastOpenedValve.contains(roomName)) {
                    return true;
                }
                roomsSinceLastOpenedValve.add(path[i].substring(2));
            }
            if (i == 0) {
                start = i;
            }
        }

        if (end - start < 2) {
            return false;
        }
        for (int i  = start; i < end; i++) {
            pairs.add(new RoomPair(path[i], path[i+1]));
        }
        //  test all pairs for inefficiency
        for (int i = 0; i < pairs.size(); i++) {
            for (int j = i+1; j < pairs.size(); j++) {
                if (pairs.get(i).isEquivalent(pairs.get(j))) {
                    System.out.println("Inefficient! " + getPathString(path, idx));
                    return true;
                }
            }
        }
        return false;
    }

    static String getPathString(String[] path, int idx) {
        String pathStr = "";
        for (int i = 0; i < idx; i++) {
            pathStr += path[i];
            if (i < idx - 1) {
                pathStr +=", ";
            }
        }
        return pathStr;
    }
}
